package frc.team4330.sensors.distance;

import java.nio.ByteBuffer;

import edu.wpi.first.wpilibj.hal.JNIWrapper;

public class LeddarJNI extends JNIWrapper {
	
	public static native void FRCNetCommCANSessionMuxReceiveLeddarDistanceMessages(
		int messageID, int messageIDMask, ByteBuffer[] output);

}
