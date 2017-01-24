package frc.team4330.screambunction;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;

public class RobotDrive {
	private Spark frontRight;
	private Spark backRight;  // YEAH TOAST
	private Spark frontLeft;
	private Spark backLeft;

	public RobotDrive() {
		frontRight = new Spark(RobotMap.FRONT_RIGHT_PORT);
		backRight = new Spark(RobotMap.BACK_RIGHT_PORT);
		frontLeft = new Spark(RobotMap.FRONT_LEFT_PORT);
		backLeft = new Spark(RobotMap.BACK_LEFT_PORT);

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
