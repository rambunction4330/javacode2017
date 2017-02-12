package edu.wpi.first.wpilibj.can;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import frc.team4330.sensors.distance.LeddarDistanceSensor;

public class CANJNI {
	
	public static final int CAN_SEND_PERIOD_NO_REPEAT = 0;
	public static final int CAN_SEND_PERIOD_STOP_REPEATING = -1;

	  /* Flags in the upper bits of the messageID */
	public static final int CAN_IS_FRAME_REMOTE = 0x80000000;
	public static final int CAN_IS_FRAME_11BIT = 0x40000000;
	
	private static boolean active = false;
	private static Random random = new Random();

	public static void FRCNetCommCANSessionMuxSendMessage(
			int messageID, ByteBuffer data, int periodMs) {
		
		if ( messageID == LeddarDistanceSensor.LEDDAR_TX_BASE_ID_DEFAULT ) {
			active = true;
			System.out.println("Setting to active");
		}
	}

	public static ByteBuffer FRCNetCommCANSessionMuxReceiveMessage(
	      IntBuffer messageID, int messageIDMask, ByteBuffer timeStamp) {
		if ( !active ) {
			throw new CANMessageNotFoundException();
		}
		if ( random.nextInt(10) > 5 ) {
			throw new CANMessageNotFoundException();
		}
		ByteBuffer byteBufer = null;
		int msgId = messageID.get();
		if ( msgId == LeddarDistanceSensor.LEDDAR_RX_BASE_ID_DEFAULT + 1 ) {
			return ByteBuffer.wrap(new byte[] {0x01});
		} else if ( msgId == LeddarDistanceSensor.LEDDAR_RX_BASE_ID_DEFAULT) {
			// 1 cm                   aL          s7   aM
			// 0000 0001, 0000 0000,  0000 0100,  0111 0000
			byte[] bytes = new byte[] {0x01, 0x00, 0x04, 0x70, 0x00, 0x00, 0x00, 0x00};
			return ByteBuffer.wrap(bytes);
		} else {
			throw new CANMessageNotFoundException();
		}
	}

}
