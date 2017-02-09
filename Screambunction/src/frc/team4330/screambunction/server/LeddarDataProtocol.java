package frc.team4330.screambunction.server;

import frc.team4330.screambunction.Robot;
import frc.team4330.sensors.distance.LeddarDistanceSensor;

public class LeddarDataProtocol {
	private LeddarDistanceSensor leddar = Robot.leddar;

	
	public String processDataRequests(String requestSeg) {
		String output;
		int ind = Integer.parseInt(requestSeg);
		
		if (ind <= 15 && ind >= 0) {
			output = leddar.getDistances().get(ind).toString();
//			output = "fite " + ind + ".0";
		} else {
			output = null;
		}
		
		return output;
	}
	
	public void start() {
//		Robot.leddar.startUp();
	}
	
	public void end() {
//		Robot.leddar.shutDown();
	}
	
}
