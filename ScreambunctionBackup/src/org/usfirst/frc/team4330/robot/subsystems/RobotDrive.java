package org.usfirst.frc.team4330.robot.subsystems;

import org.usfirst.frc.team4330.utils.RobotMap;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * RobotDrive drives the robot!!!
 * 
 * @author Amander
 *
 */
public class RobotDrive extends Subsystem {
	private SpeedController rightMotor1, rightMotor2, leftMotor1, leftMotor2;
	private boolean reverse = false;
	boolean lastPressed = false;

	public RobotDrive() {
		rightMotor1 = new Victor(RobotMap.MOTOR_THREE_PORT);
		rightMotor2 = new Victor(RobotMap.MOTOR_FOUR_PORT);
		leftMotor1 = new Victor(RobotMap.MOTOR_ONE_PORT);
		leftMotor2 = new Victor(RobotMap.MOTOR_TWO_PORT);

		rightMotor1.setInverted(true);
		rightMotor2.setInverted(true);
		leftMotor1.setInverted(false);
		leftMotor2.setInverted(false);
	}

	private void reverseDrive(boolean button) {
		if (!lastPressed && button) {
			reverse = !reverse;
			if (reverse) {
				System.out.println("*************** Robot has reverse ***************");
			}
		}

		lastPressed = button;
	}

	/**
	 * For using tank drive to drive the robot with Joysticks.
	 * 
	 * @param left the left joystick.
	 * @param right the right joystick.
	 */
	public void tankDrive(Joystick left, Joystick right, boolean button) {
		reverseDrive(button);
		if (reverse) {
			rightMotor1.set(left.getY()* RobotMap.FAST_SPEED);
			rightMotor2.set(left.getY() * RobotMap.FAST_SPEED);
			leftMotor1.set(right.getY() * RobotMap.FAST_SPEED);
			leftMotor2.set(right.getY() * RobotMap.FAST_SPEED);
		} else {
			rightMotor1.set(-right.getY()* RobotMap.FAST_SPEED);
			rightMotor2.set(-right.getY() * RobotMap.FAST_SPEED);
			leftMotor1.set(-left.getY() * RobotMap.FAST_SPEED);
			leftMotor2.set(-left.getY() * RobotMap.FAST_SPEED);
			// negative because joysticks
		}
	}

	// TODO Uncomment for testing only.
	//	SpeedController motor = new Talon(RobotMap.MOTOR_FOUR_PORT);

	/**
	 * Method for testing the tank drive wheels. Uncomment one at a time. Set at slow speed
	 * for visibility & safety.
	 * 
	 * @param stick the left joystick, used for testing wheel direction and position.
	 */
	public void tankTesting(Joystick stick) {
		// TODO Uncomment for testing only.
		//		motor.set(stick.getY() * RobotMap.TEST_SPEED);
	}

	/**
	 * For using tank drive the robot with set speeds.
	 * 
	 * @param leftv the speed of the left wheels.
	 * @param rightv the speed of the right wheels.
	 */
	public void tankAuto(double leftv, double rightv) {
		if (leftv > 1 || rightv > 1 || leftv < -1 || rightv < -1) {
			System.out.println("Check speed values.");
		} else {
			rightMotor1.set(rightv);
			rightMotor2.set(rightv);
			leftMotor1.set(leftv);
			leftMotor2.set(leftv);
		}
	}

	/**
	 * Stops the wheels.
	 */
	public void stop() {
		rightMotor1.set(0);
		leftMotor2.set(0);
		rightMotor2.set(0);
		leftMotor1.set(0);
	}

	@Override
	protected void initDefaultCommand() { }

}
