package frc.team4330.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.robot.Robot;
import frc.team4330.robot.parts.HeadingProvider;
import frc.team4330.robot.parts.TankDrive;
import frc.team4330.robot.utils.HeadingCalculator;
import frc.team4330.robot.utils.RobotMap;

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

		
		if (change > 0) { // means we need to turn right
			if (change < 10) {
				Robot.myRobot.tankAuto(RobotMap.SLOW_SPEED/2, -RobotMap.SLOW_SPEED/2);
			} else {
				if (test) tankDrive.setSpeed(RobotMap.TEST_SPEED, -RobotMap.TEST_SPEED);
				else Robot.myRobot.tankAuto(RobotMap.SLOW_SPEED, -RobotMap.SLOW_SPEED);
			}
		} else { // need to turn left
			if (change > -10) {
				Robot.myRobot.tankAuto(-RobotMap.SLOW_SPEED/2, RobotMap.SLOW_SPEED/2);
			} else {
				if (test) tankDrive.setSpeed(-RobotMap.TEST_SPEED, RobotMap.TEST_SPEED);
				else Robot.myRobot.tankAuto(-RobotMap.SLOW_SPEED, RobotMap.SLOW_SPEED);
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
		System.out.println(this.getName() + " was interrupted. Stopping " + this.getClass());
		end();
	}

}
