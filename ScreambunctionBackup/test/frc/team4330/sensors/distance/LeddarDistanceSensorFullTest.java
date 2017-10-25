package frc.team4330.sensors.distance;

import java.util.List;

public class LeddarDistanceSensorFullTest {
	
	public static void main ( String[] args ) {
		try {
			LeddarDistanceSensor sensor = new LeddarDistanceSensor();
			sensor.setRecording(true);
			
			for ( int n = 0; n < 100; n++ ) {
				sensor.startUp();
				
				for ( int i = 0; i < 100; i++ ) {
					List<LeddarDistanceSensorData> messages = sensor.getDistances();
					System.out.println("Messages are " + messages);
					Thread.sleep(20);
				}
				System.out.println("Shutting Down");
				
				sensor.shutDown();
				
				Thread.sleep(500);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

}
