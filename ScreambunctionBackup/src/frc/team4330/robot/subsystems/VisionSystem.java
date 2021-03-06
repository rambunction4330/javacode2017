package frc.team4330.robot.subsystems;

import java.util.Map;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team4330.sensors.vision.VisionComms;

public class VisionSystem extends Subsystem {

	private VisionComms visLift, visBoiler;
	private Relay ledSwitch;

	private boolean active = false;
	
	public VisionSystem() {
		visLift = new VisionComms("tegra-ubuntu.local", 9001);
//		visBoiler = new VisionComms("tegra-ubuntu", 9002);

		ledSwitch = new Relay(3, Direction.kForward);
		ledSwitch.set(Value.kForward);
	}

	public void startUp() {
		try {
			if (active) return;
			
			//			visBoiler.startUp();
			visLift.startUp();
			ledSwitch.set(Value.kForward);
			active = true;
		} catch(Exception e) {
			System.out.println("********* Error Message *********" + "\n" + e.getMessage());
		}
	}

	public void shutDown() {
		try {
			if (!active) return;
			
			visLift.shutDown();
//			visBoiler.shutDown();
			ledSwitch.set(Value.kOff);
			active = false;
		} catch (Exception e) {
			System.out.println("********* Error Message *********" + "\n" + e.getMessage());
		}
	}

	public void ledOn() {
		ledSwitch.set(Value.kForward);
	}

	public Double getBoilerAngle() {		
		return visBoiler.retrieveData().get("rb");
	}

	public Double getLiftAngle() {
		Map<String,Double> values = visLift.retrieveData();
		if ( values.containsKey("rb")) {
			return values.get("rb");
		}
		return null;
	}

	public Map<String, Double> getBoilerData() {
		return visBoiler.retrieveData();
	}

	public Map<String, Double> getLiftData() {
		return visLift.retrieveData();
	}

	@Override
	protected void initDefaultCommand() {		
	}

}
