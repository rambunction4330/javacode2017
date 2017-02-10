package frc.team4330.screambunction.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.screambunction.Robot;
import frc.team4330.screambunction.utils.AutonomousPhase;

public class PhaseCompleteCommand extends Command {
	
	private AutonomousPhase phase;
	
	public PhaseCompleteCommand ( AutonomousPhase phase ) {
		this.phase = phase;
	}

	@Override
	protected void execute() {
		Robot.steveBannon.phase = this.phase;
	}

	@Override
	protected boolean isFinished() {
		return true;
	}
	
	

}
