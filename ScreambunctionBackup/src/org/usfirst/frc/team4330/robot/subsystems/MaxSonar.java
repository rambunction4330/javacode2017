package org.usfirst.frc.team4330.robot.subsystems;

import org.usfirst.frc.team4330.utils.RobotMap;

import edu.wpi.first.wpilibj.AnalogInput;

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
