package frc.team4330.robot.server;

import java.util.List;

import frc.team4330.robot.Robot;
import frc.team4330.sensors.distance.LeddarDistanceSensorData;


public class LeddarDataProtocol {
	
	public String processDataRequests(String requestSeg) {
		List<LeddarDistanceSensorData> distances = Robot.leddar.getDistances();
		String output;
		
		if (requestSeg.equalsIgnoreCase("LEDDAR")) {
			output = "[";
			for (LeddarDistanceSensorData e : distances) {
				if (e != null)
					output += e.getDistanceInCentimeters() + ", ";
				else 
					output += "null, ";
			}
			output += "], [";
			for (LeddarDistanceSensorData e : distances) {
				if (e != null)
					output += e.getAmplitude() + ", ";
				else 
					output += "null, ";
			}
			output += "]";
		} else
			output = null;
		
		
		return output;
	}
	
}
