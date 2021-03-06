package frc.team4330.robot.commands;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.robot.Robot;
import frc.team4330.robot.parts.HeadingProvider;
import frc.team4330.robot.parts.TankDrive;
import frc.team4330.robot.subsystems.RobotDrive;
import frc.team4330.robot.utils.HeadingCalculator;

/**
 * Drive the robot forward command.
 * 
 * @author Amanda
 */
public class DriveForwardEncoders extends Command {
	private double desDistance, curHeading, pastHeading;
	private double startDis, deltaDis;
	
	private AHRS gyro;
	private RobotDrive robot;

	double distanceLeftToDrive = 0;

	/**
	 * A command that can be used to drive forward for a designated distance.
	 * 
	 * @param desDistance The distance for the robot to travel.
	 */
	public DriveForwardEncoders(double desDistance) {
		this.desDistance = desDistance;
		distanceLeftToDrive = desDistance;
		
		requires(Robot.myRobot);
	}

	public DriveForwardEncoders(double desDistance, HeadingProvider headingProvider, TankDrive tankDrive) {
		this.desDistance = desDistance;

		distanceLeftToDrive = desDistance;
	}

	@Override
	protected void initialize() {
		this.gyro = Robot.gyro;
		robot = Robot.myRobot;

	//	System.out.print("gyro start value: " + gyro.getDisplacementY()); 
		pastHeading = HeadingCalculator.normalize(Robot.gyro.getAngle());
		startDis = robot.totalDistance();
	}


	@Override
	public void execute() {
		//		System.out.println("" + distanceLeftToDrive);
		curHeading = HeadingCalculator.normalize(gyro.getAngle());

		double rightval = 0;
		double leftval = 0;

		deltaDis = robot.totalDistance() - startDis;
		distanceLeftToDrive = desDistance - deltaDis;

		if (distanceLeftToDrive <= .5) {
			rightval = .3;
			leftval = .3;
		} else {
			rightval = .3;
			leftval = .3;		
		}

		final double chg = .1;
		final double thr = 2;
		double courseChange = HeadingCalculator.calculateCourseChange(curHeading, pastHeading);

		if (courseChange > thr) {
			rightval -= chg;
			leftval += chg;
		} else if (courseChange < -thr) {
			leftval -= chg;
			rightval += chg;
		} else;


		robot.tankAuto(leftval, rightval);
	}

	@Override
	protected boolean isFinished() {
		return distanceLeftToDrive <= .005;
	}

	@Override
	public void end() {
		//System.out.print("gyro finished value: " + robot.totalDistance()); 
		robot.stop();
	}

	// TODO change back to protected?
	@Override
	public void interrupted() {
		System.out.println(this.getName() + " was interrupted. Stopping " + this.getClass());
		end();
	}

}
