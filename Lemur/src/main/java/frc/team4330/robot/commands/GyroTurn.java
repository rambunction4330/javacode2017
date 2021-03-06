package frc.team4330.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.robot.Robot;
import frc.team4330.robot.parts.HeadingProvider;
import frc.team4330.robot.parts.TankDrive;
import frc.team4330.robot.utils.HeadingCalculator;

/**
 * Uses the gyro to turn. You can imput the angle change you want to make. Absolute
 * determines whether or not you want to change by x amount or go straight to inputed angle.
 * 
 * @author Amanda
 */
public class GyroTurn extends Command {

	private double curHeading, change;
	private Double desHeading;
	private HeadingProvider headingProvider;
	private TankDrive tankDrive;
	private boolean test;

	private boolean vision;
	private double thing;
	
	/**
	 * Turn Command for if you don't know the current or desired heading.
	 * 
	 * @param headingChange The change in heading. Negative means to the right.
	 */
	public GyroTurn( double heading, boolean useVision ) {
		curHeading = Robot.gyro.getAngle();
		
		vision = useVision;
		
		thing = heading;

		//		change = HeadingCalculator.calculateCourseChange(curHeading, desHeading);

		test = false;

//		requires (Robot.myRobot);
	}

	/**
	 * Used for testing the Turn Command (unit tests).
	 * 
	 * @param desHeading Desired heading.
	 * @param headingProvider The angle provider.
	 * @param tankDrive Abstract driver.
	 */
	public GyroTurn( double desHeading, HeadingProvider headingProvider, TankDrive tankDrive ) {
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
		

		if (vision && Robot.vision.getLiftAngle() != null) 
			this.desHeading = curHeading + Robot.vision.getLiftAngle();
		else if (vision)
			desHeading = null;
		else desHeading = thing;

	}

	@Override
	public void execute() {
		if (desHeading == null)
			return;
		
		curHeading = Robot.gyro.getAngle();
		change = HeadingCalculator.calculateCourseChange(curHeading, desHeading);

		if (change > 0) { // means we need to turn right
			if (change < 10) {
				Robot.myRobot.automatedDrive(.37, -.37);
			} else {
				Robot.myRobot.automatedDrive(.5, -.5);
			}
		} else { // need to turn left
			if (change > -10) {
				Robot.myRobot.automatedDrive(-.37, .37);
			} else {
				Robot.myRobot.automatedDrive(-.5, .5);
			}
		}
		
		curHeading = Robot.gyro.getAngle();
		change = HeadingCalculator.calculateCourseChange(curHeading, desHeading);
	}

	@Override
	protected boolean isFinished() {
		return (Math.abs(change) <= 2) || desHeading == null;
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
