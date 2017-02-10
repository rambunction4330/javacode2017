package frc.team4330.screambunction.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team4330.screambunction.utils.Registrar;
import frc.team4330.screambunction.utils.RobotMap;

public class Shooter extends Subsystem {

	private SpeedController motor;
	private Relay relay;

	private Value feederVal;

	private double motorVal = 0;

	public Shooter() {
		motor = Registrar.victor(RobotMap.MOTOR_SHOOT_PORT);

		relay = Registrar.relay(RobotMap.RELAY_FEED_PORT);
//		feeder.setDirection(Direction.kForward);

		feederVal = Value.kOff;
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
			this.feederVal = Value.kOn;
		} else if (feederOn && motor.get() <= RobotMap.SHOOTING_SPEED - .05) {
			System.out.println("Turning motor on. Let motor power up.");
			motorVal = RobotMap.SHOOTING_SPEED;
		}

		motor.set(motorVal);
		relay.set(this.feederVal);
	}

	public void autoShoot(boolean shooter, boolean feeder) {
		if (shooter) motor.set(RobotMap.SHOOTING_SPEED);

		if (feeder && motor.get() >= RobotMap.SHOOTING_SPEED - .05) relay.set(Value.kOn);
	}

	public void stop() {
		stopFeed();
		stopMotor();
	}

	private void stopFeed() {
		feederVal = Value.kOff;
		relay.set(feederVal);
	}

	private void stopMotor() {
		motorVal = 0;
		motor.set(motorVal);
	}
	
	public void testFeeder() {
		feederVal = Value.kForward;
		relay.set(feederVal);
	}
	
	public void testWheel() {
		motorVal = .99;
		motor.set(motorVal);
	}

	@Override
	protected void initDefaultCommand() {		
	}

}
