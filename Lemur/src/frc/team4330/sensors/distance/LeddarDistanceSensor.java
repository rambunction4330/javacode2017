package frc.team4330.sensors.distance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.wpi.first.wpilibj.can.CANMessageNotFoundException;
import frc.team4330.sensors.canbus.ByteHelper;
import frc.team4330.sensors.canbus.CanDevice;

public class LeddarDistanceSensor extends CanDevice {
	
	/**
	 * Transmit and receive is from the standpoint of the client, not the sensor
	 */
	public static final int LEDDAR_RX_BASE_ID_DEFAULT = 1872;
	public static final int LEDDAR_TX_BASE_ID_DEFAULT = 1856;
	
	public static final byte[] COMMAND_START_SENDING_DETECTIONS = new byte[] {2};
	public static final byte[] COMMAND_STOP_SENDING_DETECTIONS = new byte[] {3};
	
	private int receiveBaseMessageId = LEDDAR_RX_BASE_ID_DEFAULT;
	private int transmitBaseMessageId = LEDDAR_TX_BASE_ID_DEFAULT;
	private static final int NUMBER_SECTORS = 16;
	
	// this is accessed by multiple threads and should only be read/modified within the 
	// synchronized getDistances/updateClientData methods.  It will always contain 16 elements
	// in order for sectors 0 - 15, but the value may be null if not known
	private List<LeddarDistanceSensorData> distances = new ArrayList<LeddarDistanceSensorData>();
	
	// the receivedDistances attribute is not thread safe and should only be changed
	// by the update thread or by the startUp method prior to the update thread being started
	private List<ReceivedInfo> receivedDistances = new ArrayList<ReceivedInfo>();
	
	// boolean values are safe to read/modify from multiple threads, so no worry about thread safety
	private boolean active = false;
	
	private UpdateThread updateThread;
	private PrintStream recorder;
	private boolean recording = false;
	
	/**
	 * Construct using default values
	 */
	public LeddarDistanceSensor() {}
	
	/**
	 * 
	 * @param receiveBaseMessageId message id the client uses to receive distance info
	 * @param transmitBaseMessageId message id the client uses to transmit commands
	 */
	public LeddarDistanceSensor(int receiveBaseMessageId, int transmitBaseMessageId) {
		this.receiveBaseMessageId = receiveBaseMessageId;
		this.transmitBaseMessageId = transmitBaseMessageId;
	}
	
	public int getSizeMessageId() {
		return receiveBaseMessageId + 1;
	}
	
	public int getDistanceMessageId() {
		return receiveBaseMessageId;
	}
	
	public boolean isRecording() {
		return recording;
	}

	public void setRecording(boolean recording) {
		this.recording = recording;
	}

	public void startUp() {
		
		if ( active ) {
			// already started, so noop
			return;
		}
		
		if ( recording ) {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String fileName = "/home/lvuser/can_record_" 
					+ df.format(new Date()) + ".txt";
			try {
				recorder = new PrintStream(new FileOutputStream( new File(fileName)));
			} catch ( Exception e ) {
				System.out.println("Could not open file " + fileName);
			}
		} else {
			recorder = null;
		}
		
		if ( recorder != null ) {
			recorder.println(System.currentTimeMillis() + " Starting up");
		}
		
		// purge messages in order to let the CAN system know the message ids which are of interest
		// and to ensure we have no messages queued from previous sessions
		purgeReceivedMessages();
	
		// initialize - it is important that the initializedReceivedState method is called
		// prior to the update thread being started
		initializeReceivedState();
		active = true;
		
		// start up thread to periodically check for received messages
		updateThread = new UpdateThread();
		updateThread.setName("LeddarDistanceSensorReader");
		updateThread.setDaemon(true);
		updateThread.start();
		
		// tell sensor to start sending messages continuously
		CANMessage startMessage = new CANMessage(transmitBaseMessageId, COMMAND_START_SENDING_DETECTIONS);
		if ( recorder != null ) {
			recorder.println(System.currentTimeMillis() + " Sending: " + startMessage);
		}
		sendData(startMessage);
	}
	
	public void shutDown() {
		
		if ( !active ) {
			// already shutdown, so noop
			return;
		}
		
		if ( recorder != null ) {
			recorder.println(System.currentTimeMillis() + " Shutting down");
		}
		
		// tell sensor to stop sending messages
		CANMessage stopMessage = new CANMessage(transmitBaseMessageId, COMMAND_STOP_SENDING_DETECTIONS);
		if ( recorder != null ) {
			recorder.println(System.currentTimeMillis() + " Sending: " + stopMessage);
		}
		sendData(stopMessage);
		
		// set to inactive and shut down the update thread
		active = false;
		// clear out any received distances
		clearDistances();
		
		if ( updateThread != null ) {
			updateThread.shutdown();
		}
		
		// dump any queued received messages since we no longer care about them and
		// don't want to read stale messages if we restart later
		purgeReceivedMessages();
		
		if ( recorder != null ) {
			recorder.close();
		}
		
	}
	
	public synchronized List<LeddarDistanceSensorData> getDistances() {
		// make a copy of the array before giving out to clients because we will be updating
		// the distances array
		List<LeddarDistanceSensorData> copy = new ArrayList<LeddarDistanceSensorData>();
		copy.addAll(distances);
		return copy;
	}
	
	private synchronized void updateClientData() {
		// purge any stale values
		for ( int i = 0; i < NUMBER_SECTORS; i++ ) {
			if ( receivedDistances.get(i).isStale() ) {
				receivedDistances.set(i, new ReceivedInfo(null));
			}
		}
		for ( int i = 0; i < NUMBER_SECTORS; i++ ) {
			distances.set(i, receivedDistances.get(i).getData());
		}
	}
	
	private synchronized void clearDistances() {
		distances.clear();
		for ( int i = 0; i < NUMBER_SECTORS; i++ ) {
			distances.add(null);
		}
	}
	
	private synchronized void initializeReceivedState() {
		receivedDistances.clear();
		distances.clear();
		for ( int i = 0; i < NUMBER_SECTORS; i++ ) {
			receivedDistances.add(new ReceivedInfo(null));
			distances.add(null);
		}
	}
	
	private void purgeReceivedMessages() {
		while(true) {
			// loop till exhausted all messages from the sensor
			int[] messageIds = new int[] { getSizeMessageId(), getDistanceMessageId() };
			boolean wasMessageReceived = false;
			for ( int i = 0; i < messageIds.length; i++ ) {
				try {
					CANMessage message = receiveData(messageIds[i]);

					if ( recorder != null ) {
						recorder.println(System.currentTimeMillis() + " Purged: " + message);
					}
					wasMessageReceived = true;
				} catch ( CANMessageNotFoundException e ) {
					// do nothing since we want to possibly move on to the next messageId
					if ( recorder != null ) {
						recorder.println(System.currentTimeMillis() + " CANMessageNotFoundException while purging message id " + messageIds[i]);
					}
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
				CANMessage message = pullNextDistanceMessage();
				
				byte[] data = message.getData();
				// a distance message has 8 bytes of data
				handleDistanceMessage(data);
			}
		} catch (CANMessageNotFoundException e) {
			// no problem since just means ran out of messages to process
			
			// do this to purge any stale data if we haven't received any messages in a while
			updateClientData();
		}
	}
	
	/**
	 * Try to get a distance message, if successful, then return the distance message.
	 * If can't get a distance message, try to get a size message and record the if we got one, but
	 * still throw a CANMessageNotFoundException exception since we don't have any distance messages
	 * @return the CANMessage for distance
	 * @throws CANMessageNotFoundException thrown if no distance CANMessages are available
	 */
	@SuppressWarnings("finally")
	private CANMessage pullNextDistanceMessage() throws CANMessageNotFoundException {
		
		try {
			CANMessage distanceMessage = receiveData(getDistanceMessageId());;		
			if ( recorder != null ) {
				recorder.println(System.currentTimeMillis() + " Received: " + distanceMessage);
			}
			return distanceMessage;
		} catch ( CANMessageNotFoundException e ) {
			
			// we didn't get any distance messages, so try to get a size message
			try {
				CANMessage sizeMessage = receiveData(getSizeMessageId());
				// we don't care about size messages, but go ahead and record them if received
				if ( recorder != null ) {
					recorder.println(System.currentTimeMillis() + " Received: " + sizeMessage);
				}
				
				return sizeMessage;
			
			} catch ( CANMessageNotFoundException e2 ) {
				// we didn't get any distance or size messages, so record that and let caller know 
				// that there are no distance messages
				if ( recorder != null ) {
					recorder.println(System.currentTimeMillis() + " CANMessageNotFoundException");
				}
			} finally {
				// throw the CANMessageNotFoundException since we don't have distance messages
				throw e;
			}
		}
		
	}
	
	private void handleDistanceMessage(byte[] sectorRawData) {
		if ( sectorRawData.length != 8 || (sectorRawData[0] == 0x00 && sectorRawData[1] == 0x00 
				&& sectorRawData[2] == 0x00 && sectorRawData[3] == 0x00 )) {
			if ( recorder != null ) {
				recorder.println(System.currentTimeMillis() + " Distance message was weird, data=" +
						ByteHelper.bytesToHex(sectorRawData));
			}
			return;
		}
		
		// we got a distance data packet, the format of which is 8 bytes with following pattern repeated twice
		// but the last four bytes will be zero filled if no data is present.
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
		LeddarDistanceSensorData d1 = new LeddarDistanceSensorData(firstSegmentNumber, firstDistance, firstAmplitude);
		LeddarDistanceSensorData d2 = null;
			
		// only process the last 4 bytes if they are not all zeros
		if ( sectorRawData[7] != 0x00 || sectorRawData[6] != 0x00 || sectorRawData[5] != 0x00 || sectorRawData[4] != 0x00 ) {
			int secondDistance = ByteHelper.readShort(sectorRawData, 4, true);
			int secondSegmentNumber = ByteHelper.getMSBValue(sectorRawData[7], 4); 
			double secondAmplitude = (((ByteHelper.getLSBValue(sectorRawData[7], 4) << 8) | (sectorRawData[6] & 0xff)))/4.0;
			d2 = new LeddarDistanceSensorData(secondSegmentNumber, secondDistance, secondAmplitude);
		}
		
		// update the received distances
		receivedDistances.set(d1.getSegmentNumber(), new ReceivedInfo(d1));
		if ( d2 != null ) {
			receivedDistances.set(d2.getSegmentNumber(), new ReceivedInfo(d2));
		}
		
		// update data for the client thread to read
		updateClientData();
	}
	
	private class ReceivedInfo {
		private long time;
		private LeddarDistanceSensorData data;
		
		public ReceivedInfo ( LeddarDistanceSensorData data ) {
			this.time = System.currentTimeMillis();
			this.data = data;
		}

		public LeddarDistanceSensorData getData() {
			return data;
		}
		
		/**
		 * Data is stale if > 250 msec
		 * @return
		 */
		public boolean isStale () {
			return System.currentTimeMillis() - time > 250;
		}
	}
	
	private class UpdateThread extends Thread {
		
		private boolean shutdown = false;

		@Override
		public void run() {
			System.out.println("Leddar update thread is running");
			while(!shutdown) {
				try {
					checkForMessages();
					long sleepTime = 5;
					if ( sleepTime < 1 ) sleepTime = 1;
					Thread.sleep(sleepTime);
				} catch ( InterruptedException e ) {
					break;
				} catch ( Throwable e ) {
					if ( recorder != null ) {
						recorder.println(System.currentTimeMillis() + " caught throwable");
						e.printStackTrace(recorder);
					}
				}
			}
			if ( recorder != null ) {
				recorder.println(System.currentTimeMillis() + " update thread has shutdown");
			}
			System.out.println("Leddar update thread has shutdown");
		}
		
		public void shutdown ( ) {
			this.shutdown = true;
			this.interrupt();
		}
		
	}

}
