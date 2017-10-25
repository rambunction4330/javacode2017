package frc.team4330.screambunction.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import frc.team4330.screambunction.utils.RobotMap;

public class MaxSonar {
	private AnalogInput analog;
	private final static double conversionFactor = 9.765625;

	public MaxSonar() {
		analog = new AnalogInput(RobotMap.MAXSONAR_PORT);
	}
	
	public double getDistanceInInches() {
		return analog.getVoltage() * 1000 / conversionFactor;
	}
	
	public double getDistanceInMeters() {
		return (analog.getVoltage() * 1000 / conversionFactor) / 39.3700787;
	}
	
	public double getVoltage() {
		return analog.getVoltage();
	}
}
