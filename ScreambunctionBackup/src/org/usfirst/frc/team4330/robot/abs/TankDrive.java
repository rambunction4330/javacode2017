package org.usfirst.frc.team4330.robot.abs;

public interface TankDrive {
	
	void setSpeed ( double left, double right );
	
	double getLeftSpeed ( );

	double getRightSpeed ( );
}
