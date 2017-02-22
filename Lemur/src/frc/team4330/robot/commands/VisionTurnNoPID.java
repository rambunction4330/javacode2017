package frc.team4330.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.robot.Robot;
import frc.team4330.robot.RobotMap;

public class VisionTurnNoPID extends Command {
	
	/**
	 * Turn Command for if you don't know the current or desired heading.
	 * 
	 * @param headingChange The change in heading. Negative means to the right.
	 */
	public VisionTurnNoPID() {		

//		requires (Robot.myRobot);
	}

	@Override
	public void execute() {
		if (Robot.vision.getLiftAngle() == null) // no angle
			Robot.myRobot.stop();
		if (Robot.vision.getLiftAngle() > 0) { // means we need to turn right
				Robot.myRobot.automatedDrive(RobotMap.SLOW_SPEED, -RobotMap.SLOW_SPEED);
		} else { // need to turn left
				Robot.myRobot.automatedDrive(-RobotMap.SLOW_SPEED, RobotMap.SLOW_SPEED);
		}	
	}

	@Override
	protected boolean isFinished() {
		if (Robot.vision.getLiftAngle() != null)
			return Math.abs(Robot.vision.getLiftAngle()) <= 2;
		else return false;
	}

	@Override
	protected void end() {
		Robot.myRobot.stop();
	}


	@Override
	protected void interrupted() {
		System.out.println(this.getName() + " was interrupted. Stopping " + this.getClass());
		end();
	}
}
