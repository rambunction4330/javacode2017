package frc.team4330.screambunction.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team4330.screambunction.utils.Registrar;
import frc.team4330.screambunction.utils.RobotMap;

public class Shooter extends Subsystem {

	private SpeedController motor;
	private Relay feeder;

	private Value feederVal;

	private double motorVal = 0;

	public Shooter() {
		motor = Registrar.victor(RobotMap.MOTOR_SHOOT_PORT);

		feeder = Registrar.relay(RobotMap.RELAY_FEED_PORT);
		feeder.setDirection(Direction.kForward);

		feederVal = Value.kOff;
	}

	/**
	 * To operate the 
	 * 
	 * @param buttonOff
	 * @param buttonOn
	 * @param feederOn
	 * @param addPwr
	 * @param subPwr
	 */
	public void manualShoot(boolean buttonOff, boolean buttonOn, boolean feederOn, boolean addPwr, boolean subPwr) {
		if (buttonOff) stop();
		else if (buttonOn) motorVal = RobotMap.SHOOTING_SPEED;

		if (addPwr) motorVal += RobotMap.INCREMENT_VALUE;
		else if (subPwr) motorVal -= RobotMap.INCREMENT_VALUE;

		if (feederOn && motor.get() >= .35) {
			this.feederVal = Value.kOn;
		} else if (feederOn && motor.get() <= .35) {
			System.out.println("Turning motor on. Let motor power up.");
			motorVal = RobotMap.SHOOTING_SPEED;
		}

		motor.set(motorVal);
		feeder.set(this.feederVal);
	}

	public void autoShoot() {
		motor.set(RobotMap.SHOOTING_SPEED);

		if (motor.get() >= .35) feeder.set(Value.kOn);
	}

	public void stop() {
		stopFeed();
		stopMotor();
	}

	private void stopFeed() {
		feederVal = Value.kOff;
		feeder.set(feederVal);
	}

	private void stopMotor() {
		motorVal = 0;
		motor.set(motorVal);
	}

	@Override
	protected void initDefaultCommand() {		
	}

}
