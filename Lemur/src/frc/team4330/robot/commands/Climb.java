package frc.team4330.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.robot.Robot;
import frc.team4330.robot.RobotMap;

public class Climb extends Command {

	
	
	@Override
	protected void initialize() {
		requires(Robot.tarzan);
	}
	
	@Override
	protected void execute() {
		Robot.tarzan.set(RobotMap.CLIMB_SPEED);
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
