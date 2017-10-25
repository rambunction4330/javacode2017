package frc.team4330.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.robot.Robot;

public class ZeroPhaseCommand extends Command {

	@Override
	protected boolean isFinished() {
		return !Robot.gyro.isCalibrating();
	}

}
