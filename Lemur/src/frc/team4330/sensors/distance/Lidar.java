package frc.team4330.sensors.distance;

import java.nio.ByteBuffer;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.hal.SPIJNI;


public class Lidar{
	private SPI serial = new SPI(SPI.Port.kOnboardCS0);
//	private ByteBuffer buffer = 
	
	public Lidar() {
		serial.free();
	}
	
	private void pls() {
//		serial.read(initiate, dataReceived, size)
	}
	
	public double[] read() {
		return new double[0];
	}

}
