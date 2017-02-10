package frc.team4330.screambunction.commands;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.screambunction.Robot;
import frc.team4330.screambunction.parts.HeadingProvider;
import frc.team4330.screambunction.parts.TankDrive;
import frc.team4330.screambunction.subsystems.RobotDrive;
import frc.team4330.screambunction.utils.HeadingCalculator;

/**
 * Drive the robot forward command.
 * 
 * @author Amanda
 */
public class DriveForward extends Command {
	private double desDistance, curHeading, pastHeading, startX, startY, 
	deltaX, deltaY, deltaDis;
	
	private AHRS gyro;
	private RobotDrive robot;

	double distanceLeftToDrive = 0;

	/**
	 * A command that can be used to drive forward for a designated distance.
	 * 
	 * @param desDistance The distance for the robot to travel.
	 */
	public DriveForward(double desDistance) {
		this.desDistance = desDistance;
		distanceLeftToDrive = desDistance;

		requires(Robot.myRobot);
	}

	public DriveForward(double desDistance, HeadingProvider headingProvider, TankDrive tankDrive) {
		this.desDistance = desDistance;

		distanceLeftToDrive = desDistance;
	}

	@Override
	protected void initialize() {
		this.gyro = Robot.gyro;
		robot = Robot.myRobot;

		System.out.print("gyro start value: " + gyro.getDisplacementY()); 
		pastHeading = HeadingCalculator.normalize(Robot.gyro.getAngle());
		startX = gyro.getDisplacementX();
		startY = gyro.getDisplacementY();
	}


	@Override
	public void execute() {
		//		System.out.println("" + distanceLeftToDrive);
		curHeading = HeadingCalculator.normalize(gyro.getAngle());

		double rightval = 0;
		double leftval = 0;


		deltaX = gyro.getDisplacementX() - startX;
		deltaY = gyro.getDisplacementY() - startY;
		deltaDis = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
		System.out.println("dis: " + deltaDis + "; x: " + gyro.getDisplacementX() + "; y: " + gyro.getDisplacementY() + "; z: " + gyro.getDisplacementZ());
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
		System.out.print("gyro finished value: " + gyro.getDisplacementY()); 
		robot.stop();
	}

	// TODO change back to protected?
	@Override
	public void interrupted() {
		robot.stop();
	}

}
