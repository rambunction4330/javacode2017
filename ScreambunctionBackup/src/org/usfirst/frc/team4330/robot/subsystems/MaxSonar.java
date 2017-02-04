package org.usfirst.frc.team4330.robot.subsystems;

import org.usfirst.frc.team4330.utils.RobotMap;

import edu.wpi.first.wpilibj.AnalogInput;

public class MaxSonar {
	private AnalogInput analog;
	private final static double conversionFactor = (5 / 512) * 1000;

	public MaxSonar() {
		analog = new AnalogInput(RobotMap.MAXSONAR_PORT);
	}
	
	public double getDistanceInInches() {
		return analog.getVoltage() / conversionFactor;
	}
	
	public double getDistanceInMeters() {
		return (analog.getVoltage() / conversionFactor) * 39.3700787;
	}
	
	public double getVoltage() {
		return analog.getVoltage();
	}
}
