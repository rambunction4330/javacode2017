package frc.team4330.sensors.distance;

import java.util.List;

public class LeddarDistanceSensorFullTest {
	
	public static void main ( String[] args ) {
		try {
			LeddarDistanceSensor sensor = new LeddarDistanceSensor();
			sensor.startUp();
			
			for ( int i = 0; i < 100; i++ ) {
				List<LeddarDistanceSensorData> messages = sensor.getDistances();
				System.out.println("Messages are " + messages);
				Thread.sleep(100);
			}
			System.out.println("Shutting Down");
			
			sensor.shutDown();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

}
