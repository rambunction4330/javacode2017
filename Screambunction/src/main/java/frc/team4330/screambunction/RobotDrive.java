package frc.team4330.screambunction;

import jaci.openrio.toast.lib.registry.Registrar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;

public class RobotDrive {
	private Spark frontRight;
	private Spark backRight;  // YEAH TOAST
	private Spark frontLeft;
	private Spark backLeft;

	public RobotDrive() {
		frontRight = Registrar.spark(RobotMap.FRONT_RIGHT_PORT);
		backRight = Registrar.spark(RobotMap.BACK_RIGHT_PORT);
		frontLeft = Registrar.spark(RobotMap.FRONT_LEFT_PORT);
		backLeft = Registrar.spark(RobotMap.BACK_LEFT_PORT);

	}

	/**
	 * For using tank drive to drive the robot.
	 * 
	 * @param left the left joystick.
	 * @param right the right joystick.
	 */
	public void tankDrive(Joystick left, Joystick right) {
		frontRight.set(right.getY());
		backRight.set(right.getY());
		frontLeft.set(left.getY());
		backLeft.set(left.getY());
	}
}
