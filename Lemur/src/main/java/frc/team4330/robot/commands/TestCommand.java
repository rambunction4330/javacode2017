package frc.team4330.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.robot.Robot;

/**
 * Simple command that drives robot forward. This was used to test the OI feature.
 * 
 * @author 4330
 */
public class TestCommand extends Command {

	public TestCommand() {
		System.out.println("Creating new " + this.getName());
		requires(Robot.myRobot);
	}
	
	@Override
	protected void execute() {
		System.out.println("Executing " + this.getName());
		Robot.myRobot.automatedDrive(.3, .3);
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
	
	@Override
	protected void end() {
		Robot.myRobot.stop();
	}

}
