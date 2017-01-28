package frc.team4330.screambunction.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.screambunction.Robot;
import frc.team4330.screambunction.RobotMap;
import frc.team4330.screambunction.parts.HeadingProvider;
import frc.team4330.screambunction.parts.TankDrive;
import frc.team4330.screambunction.utils.HeadingCalculator;

public class Turn extends Command {

	private double curHeading, desHeading;
	private HeadingProvider headingProvider;
	private TankDrive tankDrive;
	private boolean test;
	
	/**
	 * Turn Command for if you don't know the current or desired heading.
	 * 
	 * @param headingChange The change in heading. Negative means to the right.
	 */
	public Turn( double headingChange ) {
		curHeading = HeadingCalculator.normalize(Robot.gyro.getAngle());
		this.desHeading = HeadingCalculator.normalize(curHeading + headingChange);
		
		test = false;
		
		requires (Robot.myRobot);
	}
	
	/**
	 * Used for testing the Turn Command (unit tests).
	 * 
	 * @param desHeading Desired heading.
	 * @param headingProvider The angle provider.
	 * @param tankDrive Abstract driver.
	 */
	public Turn( double desHeading, HeadingProvider headingProvider, TankDrive tankDrive ) {
		curHeading = HeadingCalculator.normalize(headingProvider.getAngle());
		this.desHeading = HeadingCalculator.normalize(desHeading);
		
		this.headingProvider = headingProvider;
		this.tankDrive = tankDrive;
		
		test = true;
	}
	
	/**
	 * Turn Command for a specific heading.
	 * 
	 * @param desHeading The desired heading.
	 * @param tru Doesn't matter.
	 */
	public Turn( double desHeading, boolean tru ) {
		curHeading = HeadingCalculator.normalize(Robot.gyro.getAngle());
		this.desHeading = HeadingCalculator.normalize(desHeading);
		
		test = false;
		
		requires (Robot.myRobot);
	}

	/**
	 * Used to initialize the command.
	 */
	@Override
	protected void initialize() {
		if (!test) curHeading = HeadingCalculator.normalize(Robot.gyro.getAngle());
		else curHeading = HeadingCalculator.normalize(headingProvider.getAngle());

	}

	@Override
	public void execute() {
		if (!test) curHeading = HeadingCalculator.normalize(Robot.gyro.getAngle());
		else curHeading = HeadingCalculator.normalize(headingProvider.getAngle());
		
		// TODO Make the smaller adjustment type thing.
		if (desHeading - curHeading > 0) {
			if (test) tankDrive.setSpeed(-RobotMap.TEST_SPEED, RobotMap.TEST_SPEED);
			else Robot.myRobot.tankAuto(-RobotMap.FAST_SPEED, RobotMap.FAST_SPEED);
		} else {
			if (test) tankDrive.setSpeed(RobotMap.TEST_SPEED, -RobotMap.TEST_SPEED);
			else Robot.myRobot.tankAuto(RobotMap.FAST_SPEED, -RobotMap.FAST_SPEED);
		}
	}

	@Override
	protected boolean isFinished() {
		return (Math.abs(curHeading - desHeading) <= 1);
	}

	@Override
	protected void end() {
		if (!test) Robot.myRobot.stop();
		else tankDrive.setSpeed(0, 0);
	}


	@Override
	protected void interrupted() {
		if (!test) Robot.myRobot.stop();
		else tankDrive.setSpeed(0, 0);
	}

}
