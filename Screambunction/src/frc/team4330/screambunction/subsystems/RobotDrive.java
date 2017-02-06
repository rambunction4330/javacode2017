package frc.team4330.screambunction.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team4330.screambunction.utils.RobotMap;

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

		rightMotor1.setInverted(false);
		rightMotor2.setInverted(false);
		leftMotor1.setInverted(true);
		leftMotor2.setInverted(true);
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
	 * @param button button for reversing the drive directrion.
	 */
	public void tankDrive(Joystick left, Joystick right, boolean button) {
		reverseDrive(button);
		
		if (reverse) {
			tankAuto(right.getY() * RobotMap.FAST_SPEED, left.getY() * RobotMap.FAST_SPEED);
		} else {
			tankAuto(-left.getY() * RobotMap.FAST_SPEED, -right.getY() * RobotMap.FAST_SPEED);
			// negative because joysticks
		}
	}
	
	double leftval = 0;
	double rightval = 0;
	final double inc = .05;
	
	public void curveDrive(Joystick left, Joystick right) {
		if (leftMotor1.get() < -left.getY() && leftMotor1.get() > 0)
			leftval = leftMotor1.get() + inc;
		else if (leftMotor1.get() > -left.getY() && leftMotor1.get() < 0)
			leftval = leftMotor1.get() - inc;
		else leftval = left.getY();
		
		if (rightMotor1.get() < -right.getY() && rightMotor1.get() > 0)
			rightval = rightMotor1.get() + inc;
		else if (rightMotor1.get() > -right.getY() && rightMotor1.get() < 0)
			rightval = rightMotor1.get() - inc;
		else rightval = right.getY();
		
		tankAuto(leftval, rightval);
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
	protected void initDefaultCommand() {		
	}

}
