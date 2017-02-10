package frc.team4330.screambunction.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team4330.screambunction.Robot;
import frc.team4330.screambunction.utils.HeadingCalculator;
import frc.team4330.screambunction.utils.Registrar;
import frc.team4330.screambunction.utils.RobotMap;

/**
 * RobotDrive drives the robot!!!
 * 
 * @author Amander
 *
 */
public class RobotDrive extends Subsystem {
	private SpeedController rightMotor1, rightMotor2, leftMotor1, leftMotor2;
	private double yDis, xDis;
	private Encoder left, right;
	private boolean reverse = false;
	private boolean lastPressed = false;

	public RobotDrive() {
		rightMotor1 = Registrar.victor(RobotMap.MOTOR_THREE_PORT);
		rightMotor2 = Registrar.victor(RobotMap.MOTOR_FOUR_PORT);
		leftMotor1 = Registrar.victor(RobotMap.MOTOR_ONE_PORT);
		leftMotor2 = Registrar.victor(RobotMap.MOTOR_TWO_PORT);

		rightMotor1.setInverted(true);
		rightMotor2.setInverted(true);
		leftMotor1.setInverted(false);
		leftMotor2.setInverted(false);
		
		left = new Encoder(RobotMap.ENCODER_LEFT_ONE_PORT, RobotMap.ENCODER_LEFT_TWO_PORT);
		right = new Encoder(RobotMap.ENCODER_RIGHT_ONE_PORT, RobotMap.ENCODER_RIGHT_TWO_PORT);
		left.setDistancePerPulse(RobotMap.DISTANCE_PER_PULSE);
		right.setDistancePerPulse(RobotMap.DISTANCE_PER_PULSE);

		left.reset();
		right.reset();
		
		yDis = 0;
		xDis = 0;
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
			// negative because joysticks.
		}
	}
	
	double leftval = 0;
	double rightval = 0;
	final double inc = .02;
	
	public void curveDrive(Joystick left, Joystick right) {
		if (leftMotor2.get() < -left.getY() && left.getY() < 0)
			leftval += inc;
		else if (leftMotor2.get() > -left.getY() && left.getY() > 0)
			leftval -= inc;
		else leftval = left.getY();
		
		if (rightMotor2.get() < -right.getY() && right.getY() < 0)
			rightval += inc;
		else if (rightMotor2.get() > -right.getY() && right.getY() > 0)
			rightval -= inc;
		else rightval = 0;
		
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
		double angle = HeadingCalculator.normalize(Robot.gyro.getAngle());
		if (leftv > 1 || rightv > 1 || leftv < -1 || rightv < -1) {
			System.out.println("Check speed values.");
		} else {
			rightMotor1.set(rightv);
			rightMotor2.set(rightv);
			leftMotor1.set(leftv);
			leftMotor2.set(leftv);
		}
		
		yDis = totalDistance()*Math.cos(angle);
		xDis = totalDistance()*Math.sin(angle);
	}

	/**
	 * Stops the wheels.
	 */
	public void stop() {
		tankAuto(0, 0);
	}
	
	public void resetEncoders() {
		left.reset();
		right.reset();
	}
	
	public double totalDistance() {
		return (right.getDistance() + left.getDistance()) / 2;
	}
	
	
	public double yDist() {
		return yDis;
	}
	
	public double xDist() {
		return xDis;
	}

	@Override
	protected void initDefaultCommand() {		
	}

}
