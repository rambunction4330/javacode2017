package frc.team4330.screambunction;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team4330.screambunction.parts.AbsRobotDrive;

/**
 * RobotDrive drives the robot!!!
 * 
 * @author Amander
 *
 */
public class RobotDrive extends Subsystem implements AbsRobotDrive  {
	private SpeedController frontRight, backRight, frontLeft, backLeft;

	public RobotDrive() {
		frontRight = new Victor(RobotMap.FRONT_RIGHT_PORT);
		backRight = new Victor(RobotMap.BACK_RIGHT_PORT);
		frontLeft = new Victor(RobotMap.FRONT_LEFT_PORT);
		backLeft = new Victor(RobotMap.BACK_LEFT_PORT);
		
		frontRight.setInverted(false);
		backRight.setInverted(false);
		frontLeft.setInverted(false);
		backLeft.setInverted(false);
	}

	/**
	 * For using tank drive to drive the robot with Joysticks.
	 * 
	 * @param left the left joystick.
	 * @param right the right joystick.
	 */
	public void tankDrive(Joystick left, Joystick right) {
		frontRight.set(right.getY()* RobotMap.FAST_SPEED);
		backRight.set(right.getY() * RobotMap.FAST_SPEED);
		frontLeft.set(left.getY() * RobotMap.FAST_SPEED);
		backLeft.set(left.getY() * RobotMap.FAST_SPEED);
	}

	/**
	 * For using tank drive the robot with set speeds.
	 * 
	 * @param leftv the speed of the left wheels.
	 * @param rightv the speed of the right wheels.
	 */
	public void tankAuto(double leftv, double rightv) {
		if (leftv > 1 || rightv > 1) {
			System.out.println("Check speed values.");
		} else {
			frontRight.set(rightv);
			backRight.set(rightv);
			frontLeft.set(leftv);
			backLeft.set(leftv);
		}
	}

	/**
	 * Stops the wheels.
	 */
	public void stop() {
		frontRight.set(0);
		backLeft.set(0);
		backRight.set(0);
		frontLeft.set(0);
	}

	@Override
	protected void initDefaultCommand() {
	}

	@Override
	public double getLeftSpeed() {
		return frontLeft.get();
	}

	@Override
	public double getRightSpeed() {
		return frontRight.get();
	}

}
