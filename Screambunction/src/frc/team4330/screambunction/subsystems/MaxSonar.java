package frc.team4330.screambunction.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import frc.team4330.screambunction.utils.RobotMap;

public class MaxSonar {
	private AnalogInput analog;
	private final static int conversionFactor = 512;

	public MaxSonar() {
		analog = new AnalogInput(RobotMap.MAXSONAR_PORT);
	}
	
	public double getDistanceInInches() {
		return analog.getVoltage() / conversionFactor;
	}
	
	public double getDistanceInMeters() {
		return (analog.getVoltage() / conversionFactor) / 0.0254;
	}
}
