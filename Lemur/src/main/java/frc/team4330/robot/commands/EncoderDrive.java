package frc.team4330.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.robot.Robot;
import frc.team4330.robot.RobotMap;
import frc.team4330.robot.parts.HeadingProvider;
import frc.team4330.robot.parts.TankDrive;
import frc.team4330.robot.utils.HeadingCalculator;

/**
 * This command utilizes the encoders to drive forward in a straight line.
 * 
 * @author Amanda
 */
public class EncoderDrive extends Command {
	private double desDistance, curHeading, pastHeading;
	private double startDis, deltaDis;

	double distanceLeftToDrive = 0;

	/**
	 * A command that can be used to drive forward for a designated distance.
	 * 
	 * @param desDistance The distance for the robot to travel.
	 */
	public EncoderDrive(double desDistance) {
		this.desDistance = desDistance;
		distanceLeftToDrive = desDistance;
		
//		requires(Robot.myRobot);
	}

	public EncoderDrive(double desDistance, HeadingProvider headingProvider, TankDrive tankDrive) {
		this.desDistance = desDistance;

		distanceLeftToDrive = desDistance;
	}

	@Override
	protected void initialize() {

	//	System.out.print("gyro start value: " + gyro.getDisplacementY()); 
		pastHeading = HeadingCalculator.normalize(Robot.gyro.getAngle());
		startDis = Robot.myRobot.totalDistance();
	}


	@Override
	public void execute() {
		//		System.out.println("" + distanceLeftToDrive);
		curHeading = HeadingCalculator.normalize(Robot.gyro.getAngle());

		double rightval = 0;
		double leftval = 0;

		deltaDis = Robot.myRobot.totalDistance() - startDis;
		distanceLeftToDrive = desDistance - deltaDis;

		if (distanceLeftToDrive <= .3) {
			rightval = RobotMap.SLOW_SPEED - .2 + .02;
			leftval = RobotMap.SLOW_SPEED - .2;
		} else {
			rightval = RobotMap.SLOW_SPEED + .02;
			leftval = RobotMap.SLOW_SPEED;		
		}

		final double chg = .2;
		final double thr = 2;
		double courseChange = HeadingCalculator.calculateCourseChange(curHeading, pastHeading);

		if (courseChange > thr) {
			rightval -= chg;
			leftval += chg;
		} else if (courseChange < -thr) {
			leftval -= chg;
			rightval += chg;
		} else;


		Robot.myRobot.automatedDrive(leftval, rightval);
	}

	@Override
	protected boolean isFinished() {
		return distanceLeftToDrive <= .005;
	}

	@Override
	public void end() {
		Robot.myRobot.stop();
	}

	// TODO change back to protected?
	@Override
	public void interrupted() {
		System.out.println(this.getName() + " was interrupted. Stopping " + this.getClass());
		end();
	}

}
