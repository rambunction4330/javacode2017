package frc.team4330.screambunction.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.screambunction.Robot;
import frc.team4330.screambunction.parts.HeadingProvider;
import frc.team4330.screambunction.parts.TankDrive;
import frc.team4330.screambunction.utils.HeadingCalculator;
import frc.team4330.screambunction.utils.RobotMap;

/**
 * Drive the robot forward command.
 * 
 * @author Amanda
 */
public class DriveForward extends Command {
	private double desDistance, curHeading, pastHeading, startX, startY, 
		deltaX, deltaY, deltaDis;
	private boolean test;

	private HeadingProvider headingProvider;
	private TankDrive tankDrive;

	/**
	 * A command that can be used to drive forward for a designated distance.
	 * 
	 * @param desDistance The distance for the robot to travel.
	 */
	public DriveForward(double desDistance) {
		this.desDistance = desDistance;
		test = false;

		requires(Robot.myRobot);
	}

	public DriveForward(double desDistance, HeadingProvider headingProvider, TankDrive tankDrive) {
		this.desDistance = desDistance;
		this.headingProvider = headingProvider;
		this.tankDrive = tankDrive;

		test = true;
	}

	@Override
	protected void initialize() {
		pastHeading = HeadingCalculator.normalize(Robot.gyro.getAngle());
		startX = Robot.gyro.getDisplacementX();
		startY = Robot.gyro.getDisplacementY();
	}


	@Override
	public void execute() {
		double difference = 0;
		if (!test) curHeading = HeadingCalculator.normalize(Robot.gyro.getAngle());
		else curHeading = HeadingCalculator.normalize(headingProvider.getAngle());

		double rightval = 0;
		double leftval = 0;

		if (!test) {
			deltaX = Robot.gyro.getDisplacementX() - startX;
			
			deltaY = Robot.gyro.getDisplacementY() - startY;
			deltaDis = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
			difference = Math.abs(desDistance - deltaDis);
		} else difference = Math.abs(headingProvider.getAngle() - desDistance);
		
		if (difference <= .5) {
			rightval = RobotMap.SLOW_SPEED;
			leftval = RobotMap.SLOW_SPEED;
		} else {
			rightval = RobotMap.FAST_SPEED;
			leftval = RobotMap.FAST_SPEED;		
		}
		
		final double chg = .1;
		final double thr = 2;
		double courseChange = HeadingCalculator.calculateCourseChange(curHeading, pastHeading);
		
		if (courseChange > thr) {
			rightval += chg;
			leftval -= chg;
		} else if (courseChange < -thr) {
			leftval += chg;
			rightval -= chg;
		} else;

		
		if (!test) Robot.myRobot.tankAuto(leftval, rightval);
		else tankDrive.setSpeed(leftval, rightval);
	}

	@Override
	protected boolean isFinished() {
		return Math.abs(Robot.gyro.getDisplacementY() - desDistance) <= .2;
	}

	@Override
	public void end() {
		if (!test) Robot.myRobot.stop();
		else tankDrive.setSpeed(0, 0);
	}

	// TODO change back to protected?
	@Override
	public void interrupted() {
		if (!test) Robot.myRobot.stop();
		else tankDrive.setSpeed(0, 0);
	}

}
