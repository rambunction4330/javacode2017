package frc.team4330.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.robot.Robot;

/**
 * A command that runs the shooter (independent of the feeder.)
 * 
 * @author Amanda
 */
public class SpinShooter extends Command {

	public SpinShooter() {
		requires(Robot.bam);
	}
	
	@Override
	protected void initialize() {
		Robot.bam.start();
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		Robot.bam.stop();
	}
	
	@Override
	protected void interrupted() {
		System.out.println(this.getName() + "was interrupted!");
		end();
	}
}
