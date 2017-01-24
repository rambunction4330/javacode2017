package frc.team4330.screambunction.testingUtils;

import frc.team4330.screambunction.parts.TankDrive;

public class TestTankDrive implements TankDrive {
	
	private double left;
	private double right;

	@Override
	public void setSpeed(double left, double right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public double getLeftSpeed() {
		return left;
	}

	@Override
	public double getRightSpeed() {
		return right;
	}
	

}
