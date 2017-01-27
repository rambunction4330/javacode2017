package frc.team4330.screambunction.parts;

import edu.wpi.first.wpilibj.Joystick;


public interface AbsRobotDrive {

	void tankDrive ( Joystick left, Joystick right );

	void tankAuto ( double left, double right );
	
	void stop ( );
	
	double getLeftSpeed ( );

	double getRightSpeed ( );
}
