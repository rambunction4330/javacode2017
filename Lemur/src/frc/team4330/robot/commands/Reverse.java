package frc.team4330.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.robot.Robot;
import frc.team4330.robot.RobotMap;

public class Reverse extends Command {

	public Reverse() {
		requires(Robot.tarzan);
	}
	
	@Override
	protected void initialize() {
		Robot.tarzan.set(-.3);
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
	
	@Override
	protected void end() {
		Robot.tarzan.stop();
	}

}
