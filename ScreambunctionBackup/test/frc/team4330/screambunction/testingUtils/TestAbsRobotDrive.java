package frc.team4330.screambunction.testingUtils;

import org.usfirst.frc.team4330.robot.abs.AbsRobotDrive;

import edu.wpi.first.wpilibj.Joystick;

public class TestAbsRobotDrive implements AbsRobotDrive {
	private static double leftS, rightS;
	
	@Override
	public void tankDrive(Joystick left, Joystick right) {
		leftS = left.getY();
		rightS = right.getY();
	}

	@Override
	public void tankAuto(double left, double right) {
		leftS = left;
		rightS = right;
	}

	@Override
	public void stop() {
		leftS = 0;
		rightS = 0;
	}

	@Override
	public double getLeftSpeed() {
		return leftS;
	}

	@Override
	public double getRightSpeed() {
		return rightS;
	}

	
	
}
