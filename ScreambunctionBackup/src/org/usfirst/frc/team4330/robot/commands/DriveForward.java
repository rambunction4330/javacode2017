package org.usfirst.frc.team4330.robot.commands;

import org.usfirst.frc.team4330.robot.Robot;
import org.usfirst.frc.team4330.robot.parts.HeadingProvider;
import org.usfirst.frc.team4330.robot.parts.TankDrive;
import org.usfirst.frc.team4330.utils.HeadingCalculator;
import org.usfirst.frc.team4330.utils.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

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

	double distanceLeftToDrive;
	
	/**
	 * A command that can be used to drive forward for a designated distance.
	 * 
	 * @param desDistance The distance for the robot to travel.
	 */
	public DriveForward(double desDistance) {
		this.desDistance = desDistance;
		test = false;
		distanceLeftToDrive = desDistance;
		
		requires(Robot.myRobot);
	}

	public DriveForward(double desDistance, HeadingProvider headingProvider, TankDrive tankDrive) {
		this.desDistance = desDistance;
		this.headingProvider = headingProvider;
		this.tankDrive = tankDrive;

		distanceLeftToDrive = desDistance;
		
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
		if (!test) curHeading = HeadingCalculator.normalize(Robot.gyro.getAngle());
		else curHeading = HeadingCalculator.normalize(headingProvider.getAngle());

		double rightval = 0;
		double leftval = 0;

		if (!test) {
			deltaX = Robot.gyro.getDisplacementX() - startX;
			deltaY = Robot.gyro.getDisplacementY() - startY;
			deltaDis = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
			distanceLeftToDrive = desDistance - deltaDis;
		} else distanceLeftToDrive = Math.abs(headingProvider.getAngle() - desDistance);
		
		if (distanceLeftToDrive <= .5) {
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
			rightval -= chg;
			leftval += chg;
		} else if (courseChange < -thr) {
			leftval -= chg;
			rightval += chg;
		} else;

		
		if (!test) Robot.myRobot.tankAuto(leftval, rightval);
		else tankDrive.setSpeed(leftval, rightval);
	}

	@Override
	protected boolean isFinished() {
		return distanceLeftToDrive <= .05;
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
