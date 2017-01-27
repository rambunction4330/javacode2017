package frc.team4330.screambunction.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.screambunction.HeadingCalculator;
import frc.team4330.screambunction.Robot;
import frc.team4330.screambunction.parts.TankDrive;

public class Turn extends Command {

	private double curHeading, desHeading;
//	private HeadingProvider headingProvider;
	private TankDrive tankDrive;

	
	/**
	 * Turn Command for if you don't know the current or desired heading.
	 * 
	 * @param headingChange The change in heading. Negative means to the right.
	 */
	public Turn( double headingChange ) {
		curHeading = HeadingCalculator.normalize(Robot.gyro.getAngle());
		this.desHeading = HeadingCalculator.normalize(curHeading + headingChange);
		
//		this.headingProvider = headingProvider;
//		this.tankDrive = tankDrive;
		
		requires (Robot.myRobot);
//		requires (headingProvider);
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
		
		requires (Robot.myRobot);
	}

	/**
	 * Used to initialize the command.
	 */
	@Override
	protected void initialize() {
		curHeading = HeadingCalculator.normalize(Robot.gyro.getAngle());
	}

	@Override
	public void execute() {
		curHeading = HeadingCalculator.normalize(Robot.gyro.getAngle());

		// TODO Make the smaller adjustment type thing.
		if (desHeading - curHeading > 0) {
			tankDrive.setSpeed(-0.5, .5);
//			Robot.myRobot.tankAuto(-.5, .5);
		} else {
			tankDrive.setSpeed(0.5, -0.5);
//			Robot.myRobot.tankAuto(.5, -.5);
		}
	}

	@Override
	protected boolean isFinished() {
		return (Math.abs(curHeading - desHeading) <= 1);
	}

	@Override
	protected void end() {
		Robot.myRobot.stop();
	}


	@Override
	protected void interrupted() {

	}


}
