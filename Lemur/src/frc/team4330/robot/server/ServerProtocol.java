package frc.team4330.robot.server;

import java.util.ArrayList;
import java.util.List;

import frc.team4330.robot.Robot;
import frc.team4330.sensors.distance.LeddarDistanceSensorData;

public class ServerProtocol {

	public String processDataRequests(String requestSeg) {
		List<LeddarDistanceSensorData> datas = Robot.leddar.getDistances();
		String output;

		List<Integer> distances = new ArrayList<Integer>();
		List<Double> amplitudes = new ArrayList<Double>();
		
		for (LeddarDistanceSensorData e : datas) {
			if (e != null) {
				distances.add(e.getDistanceInCentimeters());
				amplitudes.add(e.getAmplitude());
			} else {
				distances.add(1000000000);
				amplitudes.add(1000000000.);
			}
		}
		
		if (requestSeg.equalsIgnoreCase("LEDDAR")) {
			output = distances.toString();
			output += ", " + amplitudes.toString();
		} else if (requestSeg.equalsIgnoreCase("navX"))
			output = "[" + Robot.gyro.getVelocityX() + ", " 
					+ Robot.gyro.getVelocityY() + ", " 
					+ Robot.gyro.getAngle() + "]";
		else
			output = null;


		return output;
	}
}
