package frc.team4330.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team4330.robot.RobotMap;
import frc.team4330.robot.utils.Registrar;

public class RopeClimber extends Subsystem {
	private SpeedController motor;
	
	public RopeClimber(){
		motor = Registrar.victor(RobotMap.MOTOR_CLIMB_PORT);
		motor.setInverted(true);
	}
	
	public void manualClimb(Joystick stick) {
		set(Math.abs(stick.getY()));
	}
	
	public void setClimb(boolean slowSpd, boolean fastSpd) {
		if (slowSpd) set(RobotMap.REEL_SPEED);
		else if (fastSpd) set(RobotMap.CLIMB_SPEED);
		else stop();
	}
	
	public void testClimb(boolean slowSpd, boolean fastSpd, boolean backwards) {
		if (backwards) set(-RobotMap.REEL_SPEED);
		else if (slowSpd) set(RobotMap.REEL_SPEED);
		else if (fastSpd) set(RobotMap.CLIMB_SPEED);
		else stop();
	}
	
	public void stop() {
		set(0);
	}

	@Override
	protected void initDefaultCommand() {		
	}
	
	public void set(double spd) {
		motor.set(spd);
	}

}
