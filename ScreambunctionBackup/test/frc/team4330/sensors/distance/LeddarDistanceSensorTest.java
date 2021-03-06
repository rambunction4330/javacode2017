package frc.team4330.sensors.distance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import frc.team4330.sensors.canbus.CanDevice.CANMessage;
import frc.team4330.sensors.distance.LeddarDistanceSensorData;

import edu.wpi.first.wpilibj.can.CANMessageNotFoundException;

public class LeddarDistanceSensorTest {
	
	private LeddarDistanceSensor testObject;
	private List<CANMessage> receiveBuffer = Collections.synchronizedList(new ArrayList<CANMessage>());
	private List<CANMessage> sendBuffer = Collections.synchronizedList(new ArrayList<CANMessage>());
	
	@Before
	public void setup() {
		
		testObject = new LeddarDistanceSensor() {

			@Override
			protected CANMessage receiveData(int messageId) throws CANMessageNotFoundException {
				if ( receiveBuffer.isEmpty() ) throw new CANMessageNotFoundException();
				int messageIndex = -1;
				for ( int i = 0; i < receiveBuffer.size(); i++ ) {
					CANMessage message = receiveBuffer.get(i);
					if ( message.getMessageId() == messageId ) {
						messageIndex = i;
						break;
					}
				}
				if ( messageIndex == -1 ) {
					throw new CANMessageNotFoundException();
				}
				CANMessage message = receiveBuffer.remove(messageIndex);
				return message;
			}

			@Override
			protected void sendData(CANMessage message) {
				sendBuffer.add(message);
			}
			
		};
		
		sendBuffer.clear();
		receiveBuffer.clear();
		
	}

	@Test
	public void testStartupAndShutdown() throws Exception {
		// verify that start and shutdown sends correct messages to device
		// and shutdown will purge any messages from the receive queue
		testObject.startUp();
		Thread.sleep(100);
		
		// send buffer should contain the message to device to startup
		Assert.assertEquals(1, sendBuffer.size());
		CANMessage message = sendBuffer.remove(0);
		Assert.assertEquals(LeddarDistanceSensor.LEDDAR_TX_BASE_ID_DEFAULT, message.getMessageId());
		Assert.assertEquals(LeddarDistanceSensor.COMMAND_START_SENDING_DETECTIONS, message.getData());
		
		addToReceiveBuffer(testObject.getSizeMessageId(), new byte[] {0x01});
		addToReceiveBuffer(testObject.getDistanceMessageId(), new byte[] {0x3e, 0x3f, 0x11, 0x23, 0x34, 0x45, 0x78, 0x69});
		
		testObject.shutDown();
		Thread.sleep(100);
		
		Assert.assertEquals(1, sendBuffer.size());
		message = sendBuffer.remove(0);
		Assert.assertEquals(LeddarDistanceSensor.LEDDAR_TX_BASE_ID_DEFAULT, message.getMessageId());
		Assert.assertEquals(LeddarDistanceSensor.COMMAND_STOP_SENDING_DETECTIONS, message.getData());
		
		Assert.assertEquals(0, receiveBuffer.size());
	}
	
	@Test
	public void testHappyPathMessage() throws Exception {
		testObject.startUp();
		Thread.sleep(100);
		
		// on initial startup there should be no distance measurements available
		List<LeddarDistanceSensorData> list = testObject.getDistances();
		Assert.assertEquals(16, list.size());
		for ( int i = 0; i < 16; i++) {
			Assert.assertNull(list.get(i));
		}
		
		// simulate the device sending a size message
		addToReceiveBuffer(testObject.getSizeMessageId(), new byte[] {0x03});
		
		list = testObject.getDistances();
		Assert.assertEquals(16, list.size());
		for ( int i = 0; i < 16; i++) {
			Assert.assertNull(list.get(i));
		}
		
		// simulate the device sending the first distance message
		// distance 30,000 stored little endian in bytes 0 and 1
		// amplitude 503.25 stored little endian in byte 2 and 4 LSB of byte 3
		// with the number being stored with 10 bits representing whole number and 2 bits
		// representing the fractional part
		// sector 3 stored in 4 MSB of byte 3
		// first binary: 00110000 01110101 11011101 00110111
		// distance 65,038 
		// amplitude 1022.50
		// sector 15
		// second binary: 00001110 11111110 11111010 11111111
		addToReceiveBuffer(testObject.getDistanceMessageId(), new byte[] {48, 117, -35, 55, 14, -2, -6, -1});
		
		Thread.sleep(100);
		double measurementError = 0.1;
		
		list = testObject.getDistances();
		Assert.assertEquals(16, list.size());
		for ( int i = 0; i < 16; i++) {
			if ( i == 3 ) {
				LeddarDistanceSensorData measurement = list.get(3);
				Assert.assertEquals(30000, measurement.getDistanceInCentimeters());
				Assert.assertEquals(3, measurement.getSegmentNumber());
				Assert.assertEquals(true, Math.abs(503.25 - measurement.getAmplitude()) < measurementError);
			} else if ( i == 15 ) {
				LeddarDistanceSensorData measurement = list.get(15);
				Assert.assertEquals(65038, measurement.getDistanceInCentimeters());
				Assert.assertEquals(15, measurement.getSegmentNumber());
				Assert.assertEquals(true, Math.abs(1022.5 - measurement.getAmplitude()) < measurementError);
			} else {
				Assert.assertNull(list.get(i));
			}
		}
		
		// distance 1
		// amplitude 1.25
		// sector 1
		// first binary: 00000001 00000000 00000101 00010000
		// second binary: 32 zeros since just padding (no 4th measurement)
		addToReceiveBuffer(testObject.getDistanceMessageId(), new byte[] {1, 0, 5, 16, 0, 0, 0, 0});
		Thread.sleep(100);
		
		list = testObject.getDistances();
		Assert.assertEquals(16, list.size());
		for ( int i = 0; i < 16; i++) {
			if ( i == 1 ) {
				LeddarDistanceSensorData measurement = list.get(1);
				Assert.assertEquals(1, measurement.getDistanceInCentimeters());
				Assert.assertEquals(1, measurement.getSegmentNumber());
				Assert.assertEquals(true, Math.abs(1.25 - measurement.getAmplitude()) < measurementError);
			} else if ( i == 3 ) {
				LeddarDistanceSensorData measurement = list.get(3);
				Assert.assertEquals(30000, measurement.getDistanceInCentimeters());
				Assert.assertEquals(3, measurement.getSegmentNumber());
				Assert.assertEquals(true, Math.abs(503.25 - measurement.getAmplitude()) < measurementError);
			} else if ( i == 15 ) {
				LeddarDistanceSensorData measurement = list.get(15);
				Assert.assertEquals(65038, measurement.getDistanceInCentimeters());
				Assert.assertEquals(15, measurement.getSegmentNumber());
				Assert.assertEquals(true, Math.abs(1022.5 - measurement.getAmplitude()) < measurementError);
			} else {
				Assert.assertNull(list.get(i));
			}
		}
		
		// sleep for 100 msec to give enough time for sector 7 to expire ( should have been 300 msec )
		Thread.sleep(100);
		
		// simulate updating sector 3 and adding sector 7
		// distance 500 stored little endian in bytes 0 and 1
		// amplitude 480.00 stored little endian in byte 2 (all) and byte 3 ( 4 LSB )
		// with the number being stored with 10 bits representing whole number and 2 bits
		// representing the fractional part
		// sector 3 stored in 4 MSB of byte 3
		// first binary: 11110100 00000001 10000000 00110111
		// distance 32
		// amplitude 983.75
		// sector 7
		// second binary: 00100000 00000000 01011111 01111111
		addToReceiveBuffer(testObject.getDistanceMessageId(), new byte[] {-12, 1, -128, 55, 32, 0, 95, 127});
		
		// sleep to allow time for update thread to process messages
		Thread.sleep(100);
		
		// since sector 15 didn't get updated within 250 msec, it should have been removed
		// and sector 3 should have been updated, sector 1 unchanged, and sector 7 added
		Assert.assertEquals(16, list.size());
		for ( int i = 0; i < 16; i++) {
			if ( i == 1 ) {
				LeddarDistanceSensorData measurement = list.get(1);
				Assert.assertEquals(1, measurement.getDistanceInCentimeters());
				Assert.assertEquals(1, measurement.getSegmentNumber());
				Assert.assertEquals(true, Math.abs(1.25 - measurement.getAmplitude()) < measurementError);
			} else if ( i == 3 ) {
				LeddarDistanceSensorData measurement = list.get(3);
				Assert.assertEquals(500, measurement.getDistanceInCentimeters());
				Assert.assertEquals(3, measurement.getSegmentNumber());
				Assert.assertEquals(true, Math.abs(480.00 - measurement.getAmplitude()) < measurementError);
			} else if ( i == 7 ) {
				LeddarDistanceSensorData measurement = list.get(7);
				Assert.assertEquals(32, measurement.getDistanceInCentimeters());
				Assert.assertEquals(7, measurement.getSegmentNumber());
				Assert.assertEquals(true, Math.abs(983.75 - measurement.getAmplitude()) < measurementError);
			} else {
				Assert.assertNull(list.get(i));
			}
		}
		
		testObject.shutDown();
		Thread.sleep(100);
	}
	
	private void addToReceiveBuffer(int messageId, byte[] data) {
		receiveBuffer.add(testObject.new CANMessage(messageId, data));
	}
}
