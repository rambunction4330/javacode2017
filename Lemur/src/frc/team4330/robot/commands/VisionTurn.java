package frc.team4330.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.robot.Robot;
import frc.team4330.robot.RobotMap;

public class VisionTurn extends Command {
	private Double change;

	public VisionTurn() {
		System.out.println("Starting a new " + this.getName() + " command.");
//		requires(Robot.myRobot);
	}
	
	@Override
	protected void initialize() {
		change = Robot.vision.getLiftAngle();
		System.out.println("Initializing " + this.getName() + " command.");
	}
	
	@Override
	protected void execute() {
		System.out.println("Executing " + this.getName() + " command.");
		change = Robot.vision.getLiftAngle();
		
		if (change == null)
			Robot.myRobot.stop();
		else if (change > 0)
			if (change <= 10)
				Robot.myRobot.automatedDrive(RobotMap.SLOW_SPEED, -RobotMap.SLOW_SPEED);
			else
				Robot.myRobot.automatedDrive(RobotMap.FAST_SPEED, -RobotMap.FAST_SPEED);
		else
			if (change >= -10)
				Robot.myRobot.automatedDrive(-RobotMap.SLOW_SPEED, RobotMap.SLOW_SPEED);
			else
				Robot.myRobot.automatedDrive(-RobotMap.FAST_SPEED, RobotMap.FAST_SPEED);
	}
	
	@Override
	protected boolean isFinished() {
		if (change != null) {
			if (Math.abs(change) <= .2) {
				System.out.println(this.getName() + " command is finished.");
				return Math.abs(change) <= .2;
			} else return false;
		} else return false;
	}
	
	@Override
	protected void end() {
		System.out.println("Ending " + this.getName() + " command.");
		Robot.myRobot.stop();
	}

}
