package frc.team4330.screambunction.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.screambunction.parts.HeadingProvider;
import frc.team4330.screambunction.parts.TankDrive;

public class Turn extends Command {

	private double curHeading;
	private boolean finished = false;
	private double desHeading;
	private HeadingProvider headingProvider;
	private TankDrive tankDrive;

	public Turn(double desHeading, HeadingProvider headingProvider, TankDrive tankDrive ) {
		this.desHeading = desHeading;
		this.headingProvider = headingProvider;
		this.tankDrive = tankDrive;
	}

	/**
	 * 
	 * Used to initialize the command.
	 */
	@Override
	protected void initialize() {
		curHeading = headingProvider.getAngle();
	}

	@Override
	public void execute() {
		curHeading = headingProvider.getAngle();

		if (Math.abs(curHeading - desHeading) <= 5) {
			finished = true;
		} else if (desHeading - curHeading > 0) {
			tankDrive.setSpeed(0.5, -0.5);
		} else {
			tankDrive.setSpeed(-0.5, 0.5);
		}
	}

	@Override
	protected boolean isFinished() {
		return finished;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {

	}


}
