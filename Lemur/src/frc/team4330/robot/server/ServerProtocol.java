package frc.team4330.robot.server;

import frc.team4330.robot.Robot;

public class ServerProtocol {

	public String processDataRequests(String requestSeg) {
		String output;

		if (requestSeg.equalsIgnoreCase("DATA"))
			output = "[" + Robot.gyro.getVelocityX() + ", " 
					+ Robot.gyro.getVelocityY() + ", " 
					+ Robot.gyro.getAngle() + "]";
		else
			output = null;


		return output;
	}
}
