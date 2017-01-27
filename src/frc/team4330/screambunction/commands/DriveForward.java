package frc.team4330.screambunction.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.screambunction.Robot;
import frc.team4330.screambunction.RobotMap;

public class DriveForward extends Command {
	double desDistance;

	/**
	 * A command that can be used to drive forward for a designated distance.
	 * 
	 * @param desDistance The distance for the robot to travel.
	 */
	public DriveForward(double desDistance) {
		this.desDistance = desDistance;
		requires(Robot.myRobot);
	}

	@Override
	protected void initialize() {

	}

	@Override
	protected void execute() {
		if (Math.abs(Robot.gyro.getDisplacementY() - desDistance) <= 5) {
			Robot.myRobot.tankAuto(RobotMap.SLOW_SPEED, RobotMap.SLOW_SPEED);
		} else {
			Robot.myRobot.tankAuto(RobotMap.FAST_SPEED, RobotMap.FAST_SPEED);
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return Math.abs(Robot.gyro.getDisplacementY() - desDistance) <= .2;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		Robot.myRobot.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}

}
