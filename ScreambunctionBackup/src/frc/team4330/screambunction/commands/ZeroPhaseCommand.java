package frc.team4330.screambunction.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.screambunction.Robot;

public class ZeroPhaseCommand extends Command {

	@Override
	protected boolean isFinished() {
		return !Robot.gyro.isCalibrating();
	}

}
