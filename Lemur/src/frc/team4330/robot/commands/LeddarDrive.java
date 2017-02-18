package frc.team4330.robot.commands;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.robot.Robot;
import frc.team4330.robot.RobotMap;
import frc.team4330.robot.utils.HeadingCalculator;

/**
 * LeddarDrive uses the Leddar to drive the robot forward until it is a certain distance 
 * (see RobotMap) from the "wall" or a flat surface in front of it using sector 8.
 * 
 * @author Amanda
 */
public class LeddarDrive extends Command {
	private double curHeading, pastHeading;
	private AHRS gyro;
	private Double distanceLeftToDrive;
	
	public LeddarDrive() {
		gyro = Robot.gyro;
		
//		requires(Robot.myRobot);
	}
	
	@Override
	protected void initialize() {
		distanceLeftToDrive = Robot.getLeddarDistance(RobotMap.LEDDAR_SEGMENT);
	}
	
	@Override
	public void execute() {
		curHeading = HeadingCalculator.normalize(gyro.getAngle());

		double rightval = 0;
		double leftval = 0;

		distanceLeftToDrive = Robot.getLeddarDistance(RobotMap.LEDDAR_SEGMENT);

		if (distanceLeftToDrive != null)
			if (distanceLeftToDrive <= 1) {
				rightval = RobotMap.SLOW_SPEED;
				leftval = RobotMap.SLOW_SPEED;
			} else {
				rightval = RobotMap.FAST_SPEED;
				leftval = RobotMap.FAST_SPEED;		
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


		Robot.myRobot.automatedDrive(leftval, rightval);
	}
	
	@Override
	protected boolean isFinished() {
		if (distanceLeftToDrive != null)
			return distanceLeftToDrive <= RobotMap.DESIRED_DISTANCE_FROM_WALL;
		else return false;
	}
	
	@Override
	protected void end() {
		Robot.myRobot.stop();
	}

}
