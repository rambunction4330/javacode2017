package org.usfirst.frc.team4330.robot.subsystems;

import org.usfirst.frc.team4330.robot.Robot;
import org.usfirst.frc.team4330.robot.SmartDashboardSetup;
import org.usfirst.frc.team4330.robot.commands.DriveForward;
import org.usfirst.frc.team4330.robot.commands.Turn;
import org.usfirst.frc.team4330.utils.AutonomousPhase;
import org.usfirst.frc.team4330.utils.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class AutonomousManager extends Subsystem {
	private double xCord, yCord;
	public static double driveDistance;
	
	public AutonomousPhase phase = AutonomousPhase.one;
	
	public AutonomousManager() {
		driveDistance = 0;
//		xCord = Robot.gyro.getDisplacementX();
//		yCord = Robot.gyro.getDisplacementY();
	}
	
	public void init() {
		phase = AutonomousPhase.one;

		// This on start up because we want it to go once.
		int position = SmartDashboardSetup.getStart();

		switch(position) {
		case 1:
			travelToLeftLift();
			break;
		case 2:
			travelToCenterLift();
			break;
		case 3:
			travelToRightLift();
			break;
		default:
			System.out.println("No autonomous was selected.");
			break;
		}
		
		Scheduler.getInstance().enable(); 
	}
	
	public void run() {
		updateCoordinates();
		
		Scheduler.getInstance().run();
	}
	
	private void updateCoordinates() {
//		xCord = Robot.gyro.getDisplacementX();
//		yCord = Robot.gyro.getDisplacementY();
	}
	
	public double getXCord() {
		updateCoordinates();
		return xCord;
	}
	
	public double getYCord() {
		updateCoordinates();
		return yCord;
	}

	private void travelToLeftLift() {
		driveDistance = RobotMap.DISTANCE_TO_BASELINES + RobotMap.ROBOT_WIDTH;

		CommandGroup group = new CommandGroup();
		group.addSequential(new DriveForward(driveDistance));
		group.addSequential(new WaitCommand(.5));
		group.addSequential(new Turn(90, true));
		
		Scheduler.getInstance().add(group);
	}

	private void travelToRightLift() {
		driveDistance = RobotMap.DISTANCE_TO_BASELINES + RobotMap.ROBOT_WIDTH;

		CommandGroup group = new CommandGroup();
		group.addSequential(new DriveForward(driveDistance));
		group.addSequential(new WaitCommand(.5));
		group.addSequential(new Turn(-90, false)); 
		
		Scheduler.getInstance().add(group);
	}

	private void travelToCenterLift() {
		driveDistance = RobotMap.DISTANCE_TO_BASELINES - RobotMap.ROBOT_WIDTH;

		Scheduler.getInstance().add(new DriveForward(driveDistance));
	}
	
	/*private void turnToAngle(double angle) {
		if (angle != 0) 
			Scheduler.getInstance().add(new Turn(angle, false));
	}
	
	private void driveToLift(LeddarDistanceSensorData data) {
		if (data.getDistanceInCentimeters() != 0 || data != null) {
			driveDistance = data.getDistanceInCentimeters() / 100;
			Scheduler.getInstance().add(new DriveForward(driveDistance));
		}
	}*/
	
	public void testDriveCommand(double distance) {
		Scheduler.getInstance().add(new DriveForward(distance));
	}
	
	@Override
	protected void initDefaultCommand() { }
}
