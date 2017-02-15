package frc.team4330.robot.server;

import frc.team4330.robot.Robot;

public class NavXProtocol {

	public String processDataRequests(String input) {
		String output;
		
		if (input.equalsIgnoreCase("navX"))
//			output = "thing";
			output = "[" + Robot.gyro.getVelocityX() + ", " + Robot.gyro.getVelocityY() + ", " + Robot.gyro.getAngle() + "]";
		else output = null;
		
		return output;
	}
}
