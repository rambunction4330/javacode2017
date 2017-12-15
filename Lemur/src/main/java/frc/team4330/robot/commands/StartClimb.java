package frc.team4330.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.robot.Robot;
import frc.team4330.robot.RobotMap;

public class StartClimb extends Command {

	public StartClimb() {
		requires(Robot.tarzan);
	}
	@Override
	protected void initialize() {
		Robot.tarzan.set(RobotMap.REEL_SPEED);
	}
	
	
	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		Robot.tarzan.stop();
	}
	
	@Override
	protected void interrupted() {
		System.out.println(this.getName() + "was interrupted!");
		end();
	}
}
