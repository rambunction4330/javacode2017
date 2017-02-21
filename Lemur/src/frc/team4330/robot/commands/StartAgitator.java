package frc.team4330.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.robot.Robot;

/**
 * A command that runs the agitator/feeder (independent of the shooter.)
 * 
 * @author Amanda
 */
public class StartAgitator extends Command {

	public StartAgitator() {
		requires(Robot.bamm);
	}
	
	@Override
	protected void initialize() {
		Robot.bamm.start();
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
	
	@Override
	protected void end() {
		Robot.bamm.stop();
	}

}
