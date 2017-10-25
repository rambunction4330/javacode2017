package frc.team4330.robot.server;

import com.kauailabs.navx.frc.AHRS;

import frc.team4330.robot.Robot;
import frc.team4330.robot.utils.HeadingCalculator;

public class NavXProtocol {
	private AHRS gyro;

	public String processDataRequests(String input) {
		gyro = Robot.gyro;

		String output;
		if (input.equalsIgnoreCase("spdX")) 
			output = "" + gyro.getVelocityX();
		else if (input.equalsIgnoreCase("spdY"))
			output = "" + gyro.getVelocityY();
		else if (input.equalsIgnoreCase("angle")) 
			output = "" + HeadingCalculator.normalize(gyro.getAngle());
		else 
			output = null;

		return output;
	}
}
