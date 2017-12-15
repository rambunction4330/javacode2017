package frc.team4330.robot.commands;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.robot.Robot;
import frc.team4330.robot.utils.HeadingCalculator;

/**
 * LeddarDrive uses the Leddar to drive the robot forward until it is a certain distance 
 * (see RobotMap) from the "wall" or a flat surface in front of it using sector 8.
 * 
 * @author Amanda
 */
public class EncoderDrivePID extends Command {
	private double curHeading, pastHeading;
	public double val = 0;
	public double basespeed = 0;
		
	PIDSource scr = new PIDSource() {
		
		@Override
		public void setPIDSourceType(PIDSourceType pidSource) {			
		}
		
		@Override
		public double pidGet() {
			return Robot.myRobot.totalDistance() - startDis;
		}
		
		@Override
		public PIDSourceType getPIDSourceType() {
			return PIDSourceType.kDisplacement;
		}
	};
	PIDOutput out = new PIDOutput() {
		@Override
		public void pidWrite(double output) {
			basespeed = output;

			curHeading = Robot.gyro.getAngle();
			
			double rightval = basespeed;
			double leftval = basespeed;

			final double chg = .1;
			final double thr = 2;
			double courseChange = HeadingCalculator.calculateCourseChange(curHeading, pastHeading);

			if (courseChange > thr) {
				rightval -= chg;
				leftval += chg;
			} else if (courseChange < -thr) {
				leftval -= chg;
				rightval += chg;
			}


			Robot.myRobot.automatedDrive(leftval, rightval);
		}
	};
	PIDController pid = new PIDController(.8, 0, 8, scr, out, .005);
	
	private double desDistance;
	private double startDis;
	
	/**
	 * A command that can be used to drive forward for a designated distance.
	 * 
	 * @param desDistance The distance for the robot to travel.
	 */
	public EncoderDrivePID(double desDistance) {
		this.desDistance = desDistance;
//		requires(Robot.myRobot);
	}
	
	@Override
	protected void initialize() {
		System.out.println("Initializing " + this.getName() + " command.");
		startDis = Robot.myRobot.totalDistance();

		pastHeading = Robot.gyro.getAngle();
		pid.setOutputRange(-.4, .4);
		pid.setSetpoint(desDistance);
		pid.enable();
	}
	
	@Override
	protected boolean isFinished() {
		if (Robot.myRobot.totalDistance() - startDis >= desDistance) {
				System.out.println(this.getName() + " command is finished.");
				return true;
		} else return false;
	}
	
	@Override
	protected void end() {
		pid.disable();
		Robot.myRobot.stop();
	}

}