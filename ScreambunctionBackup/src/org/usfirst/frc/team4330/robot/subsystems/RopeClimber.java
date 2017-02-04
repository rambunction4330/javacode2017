package org.usfirst.frc.team4330.robot.subsystems;

import org.usfirst.frc.team4330.utils.RobotMap;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

public class RopeClimber extends Subsystem {
	
	private SpeedController motor;
	
	public RopeClimber(){
		motor = new Victor(RobotMap.MOTOR_CLIMB_PORT);
		motor.setInverted(true);
	}
	
	public void manualClimb(Joystick stick) {
		motor.set(Math.abs(stick.getY()));
	}
	
	public void setClimb(boolean slowSpd, boolean fastSpd) {
		if (slowSpd) motor.set(RobotMap.REEL_SPEED);
		else if (fastSpd) motor.set(RobotMap.CLIMB_SPEED);
		else stop();
	}
	
	public void testClimb(boolean slowSpd, boolean fastSpd, boolean backwards) {
		if (slowSpd) motor.set(RobotMap.REEL_SPEED);
		else if (fastSpd) motor.set(RobotMap.CLIMB_SPEED);
		else if (backwards) motor.set(-RobotMap.REEL_SPEED);
		else stop();
	}
	
	public void stop() {
		motor.set(0);
	}

	@Override
	protected void initDefaultCommand() {		
	}

}
