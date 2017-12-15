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

	public final static String VISION_KEY = "rb";

	public VisionSystem() {
		visLift = new VisionComms("tegra-ubuntu", 9001);
		//		visBoiler = new VisionComms("tegra-ubuntu", 9002);

		ledSwitch = new Relay(3, Direction.kForward);
		ledSwitch.set(Value.kForward);
	}

	public void startUp() {
		try {			
			//			visBoiler.startUp();
			visLift.startUp();
			ledSwitch.set(Value.kForward);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void shutDown() {
		try {

			visLift.shutDown();
			//			visBoiler.shutDown();
			ledSwitch.set(Value.kOff);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ledOn() {
		ledSwitch.set(Value.kForward);
	}

	public Double getBoilerAngle() {		
		return visBoiler.retrieveData().get(VISION_KEY);
	}

	public Double getLiftAngle() {
		Map<String,Double> values = visLift.retrieveData();
		if ( values.containsKey(VISION_KEY)) {
			return values.get(VISION_KEY) - 3;
		}
//		System.out.println("No " + VISION_KEY + " messages.");
		return null;
	}

	public Double getLiftAngleCentered() {

		return 4.;
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
