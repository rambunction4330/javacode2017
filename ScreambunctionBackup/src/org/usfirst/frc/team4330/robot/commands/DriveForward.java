package org.usfirst.frc.team4330.robot.commands;

import org.usfirst.frc.team4330.robot.Robot;
import org.usfirst.frc.team4330.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class DriveForward extends Command {
	double desDistance;

	/**
	 * A command that can be used to drive forward for a designated distance.
	 * 
	 * @param desDistance The distance for the robot to travel.
	 */
	public DriveForward(double desDistance) {
		this.desDistance = desDistance;
		requires(Robot.draven);
	}

	@Override
	protected void initialize() {

	}

	@Override
	protected void execute() {
		if (Math.abs(Robot.gyro.getDisplacementY() - desDistance) <= 5) {
			Robot.draven.tankAuto(RobotMap.SLOW_SPEED, RobotMap.SLOW_SPEED);
		} else {
			Robot.draven.tankAuto(RobotMap.FAST_SPEED, RobotMap.FAST_SPEED);
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
		Robot.draven.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}

}
