package frc.team4330.robot.parts;

public interface TankDrive {
	
	void setSpeed ( double left, double right );
	
	double getLeftSpeed ( );

	double getRightSpeed ( );
}
