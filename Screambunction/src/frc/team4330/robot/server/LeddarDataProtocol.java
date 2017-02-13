package frc.team4330.robot.server;

import frc.team4330.robot.Robot;
import frc.team4330.sensors.distance.LeddarDistanceSensor;

public class LeddarDataProtocol {
	private LeddarDistanceSensor leddar;

	
	public String processDataRequests(String requestSeg) {
		leddar = Robot.leddar;
		
		String output;
		int ind = Integer.parseInt(requestSeg);
		
		if (ind <= 15 && ind >= 0)
			output = leddar.getDistances().get(ind).toString();
		else
			output = null;
		
		return output;
	}
	
	public void start() {
		leddar.startUp();
	}
	
	public void end() {
		leddar.shutDown();
	}
	
}
