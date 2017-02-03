package frc.team4330.screambunction.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.screambunction.Robot;
import frc.team4330.screambunction.parts.HeadingProvider;
import frc.team4330.screambunction.parts.TankDrive;
import frc.team4330.screambunction.utils.HeadingCalculator;
import frc.team4330.screambunction.utils.RobotMap;

public class Turn extends Command {

	private double curHeading, desHeading, change;
	private HeadingProvider headingProvider;
	private TankDrive tankDrive;
	private boolean test;

	/**
	 * Turn Command for if you don't know the current or desired heading.
	 * 
	 * @param headingChange The change in heading. Negative means to the right.
	 */
	public Turn( double heading, boolean absolute ) {
		curHeading = Robot.gyro.getAngle();

		if (absolute) this.desHeading = heading;
		else this.desHeading = curHeading + heading;

		//		change = HeadingCalculator.calculateCourseChange(curHeading, desHeading);

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
		curHeading = headingProvider.getAngle();
		this.desHeading = desHeading;

		this.headingProvider = headingProvider;
		this.tankDrive = tankDrive;

		test = true;
	}

	/**
	 * Used to initialize the command.
	 */
	@Override
	protected void initialize() {
		if (!test) curHeading = Robot.gyro.getAngle();
		else curHeading = HeadingCalculator.normalize(headingProvider.getAngle());

	}

	@Override
	public void execute() {
		if (!test) {
			curHeading = Robot.gyro.getAngle();
			change = HeadingCalculator.calculateCourseChange(curHeading, desHeading);
		} else curHeading = headingProvider.getAngle();

		double tempSpd = .5; 
		
		if (change > 0) { // means we need to turn right
			if (change < 10) {
				Robot.myRobot.tankAuto(tempSpd/2, -tempSpd/2);
			} else {
				if (test) tankDrive.setSpeed(RobotMap.TEST_SPEED, -RobotMap.TEST_SPEED);
				else Robot.myRobot.tankAuto(tempSpd, -tempSpd);
			}
		} else { // need to turn left
			if (change > -10) {
				Robot.myRobot.tankAuto(-tempSpd/2, tempSpd/2);
			} else {
				if (test) tankDrive.setSpeed(-RobotMap.TEST_SPEED, RobotMap.TEST_SPEED);
				else Robot.myRobot.tankAuto(-tempSpd, tempSpd);
			}
		}
	}

	@Override
	protected boolean isFinished() {
		return (Math.abs(change) <= 1);
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
