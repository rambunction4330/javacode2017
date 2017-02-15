package frc.team4330.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.robot.Robot;

public class StartAgitator extends Command {

	@Override
	protected void initialize() {
		requires(Robot.bambam);
	}
	
	@Override
	protected void execute() {
		Robot.bambam.automatedFeeder();
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
	
	@Override
	protected void end() {
		Robot.bambam.stopFeed();
	}

}
