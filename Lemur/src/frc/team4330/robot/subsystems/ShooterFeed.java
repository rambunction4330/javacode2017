package frc.team4330.robot.subsystems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team4330.robot.RobotMap;
import frc.team4330.robot.utils.Registrar;

public class ShooterFeed extends Subsystem {
	private SpeedController motor;
	
	public ShooterFeed() {
		motor = Registrar.victor(RobotMap.MOTOR_FEED_PORT);
	}
	
	public void teleShoot(boolean powerOn, boolean powerOff) {
		if (powerOff) stop();
		else if (powerOn) start();
	}
	
	public void start() {
		motor.set(RobotMap.FEEDING_SPEED);
	}
	
	public void stop() {
		motor.set(0);
	}
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

}
