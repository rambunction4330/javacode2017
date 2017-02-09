package frc.team4330.screambunction.subsystems;

import java.util.Map;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team4330.sensors.vision.VisionComms;

public class VisionSystem extends Subsystem {

	private VisionComms visLift, visBoiler;
	private Relay ledSwitch;

	public VisionSystem() {
		visLift = new VisionComms("tegra-ubuntu", 9001);
		visBoiler = new VisionComms("tegra-ubuntu", 9002);

		ledSwitch = new Relay(3, Direction.kForward);
		ledSwitch.set(Value.kForward);
	}

	public void startUp() {
		try {
			visBoiler.startUp();
			visLift.startUp();
			ledSwitch.set(Value.kForward);
		} catch(Exception e) {
			System.out.println("********* Error Message *********" + "\n" + e.getMessage());

		}
	}

	public void shutDown() {
		try {
			visLift.shutDown();
			visBoiler.shutDown();
			ledSwitch.set(Value.kOff);
		} catch (Exception e) {
			System.out.println("********* Error Message *********" + "\n" + e.getMessage());
		}
	}

	public void ledOn() {
		ledSwitch.set(Value.kForward);
	}

	public Double getBoilerAngle() {
		return Double.parseDouble(visBoiler.retrieveData().get("rb"));
	}

	public Double getLiftAngle() {
		return Double.parseDouble(visLift.retrieveData().get("rb"));
	}

	public Map<String, String> getBoilerData() {
		return visBoiler.retrieveData();
	}

	public Map<String, String> getLiftData() {
		return visLift.retrieveData();
	}

	@Override
	protected void initDefaultCommand() {		
	}

}
