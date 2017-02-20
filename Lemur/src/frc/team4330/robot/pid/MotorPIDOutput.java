package frc.team4330.robot.pid;

import edu.wpi.first.wpilibj.PIDOutput;
import frc.team4330.robot.Robot;

public class MotorPIDOutput implements PIDOutput {

	@Override
	public void pidWrite(double output) {
		System.out.println("output: " + output);
		Robot.myRobot.automatedDrive(output, output);
	}

}
