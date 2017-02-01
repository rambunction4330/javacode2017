package frc.team4330.screambunction.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team4330.screambunction.utils.RobotMap;

public class Shooter extends Subsystem {

	private SpeedController motor;
	private Relay feeder;
	
	private Value feederOn;

	private double motorVal = 0;

	public Shooter() {
		motor = new Victor(RobotMap.MOTOR_SHOOT_PORT);

		feeder = new Relay(RobotMap.RELAY_FEED_PORT);
		feeder.setDirection(Direction.kForward);
		
		feederOn = Value.kOff;
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
		if (buttonOn) motorVal = RobotMap.SHOOTING_SPEED;
		else if (addPwr) motorVal += RobotMap.INCREMENT_VALUE;
		else if (subPwr) motorVal -= RobotMap.INCREMENT_VALUE;
		else if (buttonOff) stop();
		
		if (feederOn || buttonOn) this.feederOn = Value.kOn;

		motor.set(motorVal);
		feeder.set(this.feederOn);
	}

	public void autoShoot() {
		motor.set(RobotMap.SHOOTING_SPEED);
	}

	public void stop() {
		feederOn = Value.kOff;
		motorVal = 0;
	}

	@Override
	protected void initDefaultCommand() {		
	}

}
