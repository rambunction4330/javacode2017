package frc.team4330.robot.subsystems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team4330.robot.utils.Registrar;
import frc.team4330.robot.utils.RobotMap;

public class Shooter extends Subsystem {

	private SpeedController motor;
	private SpeedController motor2;

	private double motorVal = 0;
	private double motor2Val = 0;

	public Shooter() {
		motor = Registrar.victor(RobotMap.MOTOR_SHOOT_PORT);

		motor2 = Registrar.victor(RobotMap.MOTOR_FEED_PORT);
	}

	/**
	 * To operate the shooter manually (via Joysticks).
	 * 
	 * @param buttonOff
	 * @param buttonOn
	 * @param feederOn
	 * @param addPwr
	 * @param subPwr
	 */
	public void manualShoot(boolean buttonOff, boolean buttonOn, boolean feederOn) {
		if (buttonOff) stop();
		else if (buttonOn) motorVal = RobotMap.SHOOTING_SPEED;

		if (feederOn && motor.get() >= RobotMap.SHOOTING_SPEED - .05) {
			this.motor2Val = RobotMap.FEEDING_SPEED;
		} else if (feederOn && motor.get() <= RobotMap.SHOOTING_SPEED - .05) {
			System.out.println("Turning motor on. Let motor power up.");
			motorVal = RobotMap.SHOOTING_SPEED;
		}

		motor.set(motorVal);
		motor2.set(motor2Val);
	}

	public void autoShoot(boolean shooter, boolean feeder) {
		if (shooter) motor.set(RobotMap.SHOOTING_SPEED);

		if (feeder && motor.get() >= RobotMap.SHOOTING_SPEED - .05)
			motor2.set(RobotMap.FEEDING_SPEED);
	}

	public void stop() {
		stopFeed();
		stopMotor();
	}

	public void stopFeed() {
		motor2Val = 0;
		motor2.set(motor2Val);
	}

	private void stopMotor() {
		motorVal = 0;
		motor.set(motorVal);
	}
	
	public void testFeeder() {
		motor2Val = -.99;
		motor2.set(motor2Val);
	}
	
	public void testWheel() {
		motorVal = .7;
		motor.set(motorVal);
	}

	@Override
	protected void initDefaultCommand() {		
	}

}
