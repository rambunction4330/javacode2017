package frc.team4330.screambunction.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.screambunction.HeadingCalculator;
import frc.team4330.screambunction.Robot;
import frc.team4330.screambunction.RobotMap;

public class DriveForward extends Command {
	double desDistance;
	double curHeading;
	double pastHeading;

	/**
	 * A command that can be used to drive forward for a designated distance.
	 * 
	 * @param desDistance The distance for the robot to travel.
	 */
	public DriveForward(double desDistance) {
		this.desDistance = desDistance;
		requires(Robot.myRobot);
	}

	@Override
	protected void initialize() {
		pastHeading = HeadingCalculator.normalize(Robot.gyro.getAngle());
		Robot.gyro.resetDisplacement();
	}


	@Override
	protected void execute() {
		curHeading = HeadingCalculator.normalize(Robot.gyro.getAngle());

		double rightval = 0;
		double leftval = 0;

		if (Math.abs(Robot.gyro.getDisplacementY() - desDistance) <= 5) {
			rightval = RobotMap.SLOW_SPEED;
			leftval = RobotMap.SLOW_SPEED;
		} else {
			rightval = RobotMap.FAST_SPEED;
			leftval = RobotMap.FAST_SPEED;		
		}

		if (Math.abs(pastHeading - curHeading) > 5) {
			if (pastHeading - curHeading > 0) rightval += .1;
			else leftval += .1;
		}

		Robot.myRobot.tankAuto(leftval, rightval);
	}

	@Override
	protected boolean isFinished() {
		return Math.abs(Robot.gyro.getDisplacementY() - desDistance) <= .2;
	}

	@Override
	protected void end() {
		Robot.myRobot.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}

}
