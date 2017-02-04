package org.usfirst.frc.team4330.sensors.distance;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team4330.sensors.canbus.ByteHelper;
import org.usfirst.frc.team4330.sensors.canbus.CanDevice;

import edu.wpi.first.wpilibj.can.CANMessageNotFoundException;

public class LeddarDistanceSensor extends CanDevice {
	
	public static final int LEDDAR_RX_BASE_ID_DEFAULT = 1856;
	public static final int LEDDAR_TX_BASE_ID_DEFAULT = 1872;
	
	public static final byte[] COMMAND_START_SENDING_DETECTIONS = new byte[] {2};
	public static final byte[] COMMAND_STOP_SENDING_DETECTIONS = new byte[] {3};
	
	private int receiveBaseMessageId = LEDDAR_RX_BASE_ID_DEFAULT;
	private int transmitBaseMessageId = LEDDAR_TX_BASE_ID_DEFAULT;
	
	// this is accessed by multiple threads and should only be read/modified within the 
	// synchronized getDistances/updateDistances methods
	private List<LeddarDistanceSensorData> distances = new ArrayList<LeddarDistanceSensorData>();
	
	// the following three receivedXXX attributes are not thread safe and should only be changed
	// by the update thread or by the startUp method prior to the update thread being started
	private int receivedSizeExpected = 0;
	private List<LeddarDistanceSensorData> receivedDistances = new ArrayList<LeddarDistanceSensorData>();
	
	// boolean values are safe to read/modify from multiple threads, so no worry about thread safety
	private boolean active = false;
	private Thread updateThread;
	
	/**
	 * Construct using default values
	 */
	public LeddarDistanceSensor() {
		
	}
	
	/**
	 * 
	 * @param receiveBaseMessageId message id the sensor uses to receive commands
	 * @param transmitBaseMessageId message id the sensor uses to transmit distance info
	 */
	public LeddarDistanceSensor(int receiveBaseMessageId, int transmitBaseMessageId) {
		this.receiveBaseMessageId = receiveBaseMessageId;
		this.transmitBaseMessageId = transmitBaseMessageId;
	}
	
	public int getSizeMessageId() {
		return transmitBaseMessageId + 1;
	}
	
	public int getDistanceMessageId() {
		return transmitBaseMessageId;
	}
	
	public void startUp() {
		
		if ( active ) {
			// already started, so noop
			return;
		}
	
		// initialize - it is important that the initializedReceivedState method is called
		// prior to the update thread being started
		initializeReceivedState();
		active = true;
		
		// start up thread to periodically check for received messages
		updateThread = new Thread() {

			@Override
			public void run() {
				while(active) {
					checkForMessages();
					try {
						long sleepTime = 5;
						if ( sleepTime < 1 ) sleepTime = 1;
						Thread.sleep(sleepTime);
					} catch ( InterruptedException e ) {
						break;
					}
				}
			}
			
		};
		updateThread.setName("LeddarDistanceSensorReader");
		updateThread.setDaemon(true);
		updateThread.start();
		
		// purge messages in order to let the CAN system know the message ids which are of interest
		purgeReceivedMessages();
		
		// tell sensor to start sending messages continuously
		sendData(new CANMessage(receiveBaseMessageId, COMMAND_START_SENDING_DETECTIONS));
	}
	
	public void shutDown() {
		
		if ( !active ) {
			// already shutdown, so noop
			return;
		}
		
		// tell sensor to stop sending messages
		sendData(new CANMessage(receiveBaseMessageId, COMMAND_STOP_SENDING_DETECTIONS));
		
		// set to inactive and shut down the update thread
		active = false;
		
		if ( updateThread != null ) {
			updateThread.interrupt();
		}
		
		// dump any queued received messages since we no longer care about them and
		// don't want to read stale messages if we restart later
		purgeReceivedMessages();
	}
	
	public synchronized List<LeddarDistanceSensorData> getDistances() {
		if ( active ) {
			return distances;
		} else {
			return new ArrayList<LeddarDistanceSensorData>();
		}
	}
	
	private synchronized void updateDistances(List<LeddarDistanceSensorData> updatedDistances) {
		distances = updatedDistances;
	}
	
	private synchronized void initializeReceivedState() {
		receivedDistances.clear();
		receivedSizeExpected = 0;
	}
	
	private void purgeReceivedMessages() {
		while(true) {
			// loop till exhausted all messages from the sensor
			int[] messageIds = new int[] { getSizeMessageId(), getDistanceMessageId() };
			boolean wasMessageReceived = false;
			for ( int i = 0; i < messageIds.length; i++ ) {
				try {
					CANMessage message = receiveData(messageIds[i]);
					System.out.println("Purged " + message);
					wasMessageReceived = true;
				} catch ( CANMessageNotFoundException e ) {
					// do nothing since we want to possibly move on to the next messageId
				}
			}
			if ( !wasMessageReceived ) {
				break;
			}
		}
	}
	
	private void checkForMessages() {
		if ( !active ) {
			return;
		}
		
		try {
			// read as many messages as possible since many may be queued up
			// by looping until the CANMessageNotFoundException occurs
			while(true) {
				CANMessage message = pullNextSensorMessage();
				int messageId = message.getMessageId();
				byte[] data = message.getData();
				if ( messageId == getSizeMessageId() ) {
					// a size message has 1 byte of data
					handleSizeMessage(data[0]);
				} else if ( messageId == getDistanceMessageId() ){
					// a distance message has 8 bytes of data
					handleDistanceMessage(data);
				} else {
					throw new RuntimeException("Received unexpected " + message);
				}
			}
		} catch (CANMessageNotFoundException e) {
			// no problem since just means ran out of messages to process
		}
	}
	
	private CANMessage pullNextSensorMessage() throws CANMessageNotFoundException {
		
		CANMessage message = null;
		if ( receivedSizeExpected == 0 ) {
			// only ask for size messages since the size has not been established yet
			message = receiveData(getSizeMessageId());
		} else {
			// we have a size message, so only ask for distance messages
			message = receiveData(getDistanceMessageId());
		}
		
		return message;
	}
	
	private void handleSizeMessage(int size) {
		receivedSizeExpected = size;
		
		// we got a size message, so initialize our received data for distances
		receivedDistances.clear();	
	}
	
	private void handleDistanceMessage(byte[] sectorRawData) {
		if ( sectorRawData.length != 8 ) {
			throw new RuntimeException("Distance message should contain 8 bytes of data, but message was " +
				ByteHelper.bytesToHex(sectorRawData));
		}
		
		// we got a distance data packet, the format of which is 8 bytes with following pattern repeated twice
		// but if number of measurements is odd, the last data packet will contain 8 bytes but the last
		// four bytes will be zero filled.
		// pattern is: 
		// data bytes 0 and 1 contain the distance in centimeters little endian
		// data byte 2 and 4 LSB of byte 3 with byte 2 little endian.  The binary is 12 bit value
		// with 10 bits for whole number and 2 bits as fractional part.  So divide by four to
		// get the represented amplitude
		// 4 MSB of byte 3 is the segment number
		
		// always read the first 4 bytes
		int firstDistance = ByteHelper.readShort(sectorRawData, 0, true);
		int firstSegmentNumber = ByteHelper.getMSBValue(sectorRawData[3], 4);
		double firstAmplitude = (((ByteHelper.getLSBValue(sectorRawData[3], 4) << 8) | (sectorRawData[2] & 0xff)))/4.0; 
		receivedDistances.add(new LeddarDistanceSensorData(firstSegmentNumber, firstDistance, firstAmplitude));
			
		// conditionally read the second 4 bytes
		if ( receivedDistances.size() < receivedSizeExpected ) {
			int secondDistance = ByteHelper.readShort(sectorRawData, 4, true);
			int secondSegmentNumber = ByteHelper.getMSBValue(sectorRawData[7], 4); 
			double secondAmplitude = (((ByteHelper.getLSBValue(sectorRawData[7], 4) << 8) | (sectorRawData[6] & 0xff)))/4.0;
			receivedDistances.add(new LeddarDistanceSensorData(secondSegmentNumber, secondDistance, secondAmplitude));
		}
		
		if ( receivedDistances.size() == receivedSizeExpected ) {
			// have all of the distances, so update for the client with a copy
			// of the received distances
			List<LeddarDistanceSensorData> updatedDistances = new ArrayList<LeddarDistanceSensorData>();
			updatedDistances.addAll(receivedDistances);
			updateDistances(updatedDistances);
			initializeReceivedState();
		}
	}

}
