package edu.wpi.first.wpilibj.can;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import frc.team4330.sensors.distance.LeddarDistanceSensor;

/**
 * Fake CANJNI implementation to test LeddarDistanceSensor as fully as possible
 * @author rltiel
 *
 */
public class CANJNI {
	
	public static final int CAN_SEND_PERIOD_NO_REPEAT = 0;
	public static final int CAN_SEND_PERIOD_STOP_REPEATING = -1;

	  /* Flags in the upper bits of the messageID */
	public static final int CAN_IS_FRAME_REMOTE = 0x80000000;
	public static final int CAN_IS_FRAME_11BIT = 0x40000000;
	
	private static final long updateDelayMSec = 40;
	private static Random random = new Random();
	private static long lastUpdateMSec;
	private static List<Integer> segmentsToReturn = new ArrayList<Integer>();
	private static boolean started = false;
	private static ByteBuffer outputBuffer = ByteBuffer.allocate(1000);

	public static synchronized void FRCNetCommCANSessionMuxSendMessage(
			int messageID, ByteBuffer data, int periodMs) {
		
		if ( messageID == LeddarDistanceSensor.LEDDAR_TX_BASE_ID_DEFAULT ) {
			byte command = data.get();
			if ( command == 2 ) {
				lastUpdateMSec = System.currentTimeMillis();
				started = true;
				System.out.println("Fake CANJNI: Sent start command to LEDDAR");
			} else if ( command == 3 ) {
				started = false;
				System.out.println("Fake CANJNI: Sent stop command to LEDDAR");
			}
		}
	}

	public static synchronized ByteBuffer FRCNetCommCANSessionMuxReceiveMessage(
	      IntBuffer messageID, int messageIDMask, ByteBuffer timeStamp) {
		
		long currentTime = System.currentTimeMillis();
		int msgId = messageID.get();
		System.out.println("Fake CANJNI asking for messageId " + msgId);
		if ( msgId == LeddarDistanceSensor.LEDDAR_RX_BASE_ID_DEFAULT + 1 ) {
			// asking for size message
			if ( (!started && segmentsToReturn.isEmpty()) || segmentsToReturn.size() > 0 || (currentTime - lastUpdateMSec) < updateDelayMSec ) {
				System.out.println("Fake CANJNI throwing CANMessageNotFoundException");
				throw new CANMessageNotFoundException();
			} else {
				// time to provide some sensor data
				for ( int i = 0; i < 16; i++ ) {
					// give 30% chance for any sector to have data
					if ( random.nextInt(100) < 30 ) {
						segmentsToReturn.add(i);
					}
				}
				if ( segmentsToReturn.isEmpty() ) {
					// try again next time
					throw new CANMessageNotFoundException();
				}
				lastUpdateMSec = currentTime;
				// respond that there is data available
				System.out.println("Fake CANJNI returning size message of " + segmentsToReturn.size());
				if ( outputBuffer.remaining() == 0 ) {
					outputBuffer.clear();
				}
				outputBuffer.mark();
				outputBuffer.put((byte) (segmentsToReturn.size() & 0xff));
				outputBuffer.limit(outputBuffer.position());
				outputBuffer.reset();
				return outputBuffer;
			}
			
		} else if ( msgId == LeddarDistanceSensor.LEDDAR_RX_BASE_ID_DEFAULT) {
			
			if ( segmentsToReturn.isEmpty() ) {
				System.out.println("Fake CANJNI throwing CANMessageNotFoundException");
				throw new CANMessageNotFoundException();
			} else {
			
				Integer firstSegmentId = segmentsToReturn.remove(0);
				Integer secondSegmentId = null;
				if ( !segmentsToReturn.isEmpty() ) {
					secondSegmentId = segmentsToReturn.remove(0);
				}
				
				byte[] bytes = new byte[8];
				for ( int i = 0; i < 2; i++ ) {
					int index = 0;
					int sector = firstSegmentId;
					if ( i == 1 ) {
						if ( secondSegmentId == null ) {
							System.out.println("Fake CANJNI: Skipping index 1");
							continue;
						}
						sector = secondSegmentId;
						index = 4;
					}
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
				
				System.out.println("Fake CANJNI returning distance message");
				if ( outputBuffer.remaining() < 8 ) {
					outputBuffer.clear();
				}
				outputBuffer.mark();
				outputBuffer.put(bytes);
				outputBuffer.limit(outputBuffer.position());
				outputBuffer.reset();
				System.out.println("outputBuffer pos=" + outputBuffer.position() + " limit=" + outputBuffer.limit() +
						" remaining");
				return outputBuffer;
			}
		} else {
			// unrecognized message id
			System.out.println("Fake CANJNI didn't recognize message id so throwing CANMessageNotFound");
			throw new CANMessageNotFoundException();
		}
	}

}
