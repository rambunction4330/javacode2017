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
			byte command = data.get();
			if ( command == 2 ) {
				active = true;
				System.out.println("Fake CANJNI: Setting to active");
			} else if ( command == 3 ) {
				active = false;
				System.out.println("Fake CANJNI: Setting to inactive");
			}
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
			
			// throw message not found 15% of the time
			if ( random.nextInt(100) < 15 ) {
				throw new CANMessageNotFoundException();
			}
			
			byte[] bytes = new byte[8];
			for ( int i = 0; i < 2; i++ ) {
				int index = 0;
				if ( i == 1 ) {
					
					// about 10% of the time, don't include a second reading
					if ( random.nextInt(100) < 10 ) {
						System.out.println("Fake CANJNI: Skipped index 1");
						continue;
					}
					
					index = 4;
				}
				int sector = random.nextInt(16);
				int amplitudeWhole = random.nextInt(256 * 4);
				int amplitudeFraction = random.nextInt(4);
				int distance = random.nextInt(256 * 256);
				
				// first two bytes are distance little endian
				bytes[index] = (byte) (distance & 0xff);
				bytes[index + 1] = (byte) (distance >> 8 & 0xff);
				
				// third byte is 6bits least significant portion of amplitude whole plus 2 bits amplitude fractional
				bytes[index + 2] = (byte) (((amplitudeWhole & 0x3f) << 2) | (amplitudeFraction & 0x03));
				
				// fourth byte 4 MSB is sector and 4 LSB is most significant portion of amplitude whole
				bytes[index + 3] = (byte) (((sector & 0x0f) << 4) | ((amplitudeWhole >> 6) & 0x0f ));
				
				System.out.println("Fake CANJNI: Added index " + i + " sector: " + sector + " distance: " + distance + " amplitude: " +
						(amplitudeWhole + amplitudeFraction / 4.0));
				
			}
			
			return ByteBuffer.wrap(bytes);
		} else {
			throw new CANMessageNotFoundException();
		}
	}

}
