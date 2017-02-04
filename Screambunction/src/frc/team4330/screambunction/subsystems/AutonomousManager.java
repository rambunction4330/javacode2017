package frc.team4330.screambunction.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.team4330.screambunction.Robot;
import frc.team4330.screambunction.SmartDashboardSetup;
import frc.team4330.screambunction.commands.DriveForward;
import frc.team4330.screambunction.commands.Turn;
import frc.team4330.screambunction.utils.AutonomousPhase;
import frc.team4330.screambunction.utils.RobotMap;
import frc.team4330.sensors.distance.LeddarDistanceSensorData;

public class AutonomousManager extends Subsystem {
	private double xCord, yCord;
	public static double driveDistance;
	
	public AutonomousPhase phase = AutonomousPhase.one;
	private int position;
	
	protected AHRS gyro;
	protected VisionSystem vision;
	
	public AutonomousManager() {
		driveDistance = 0;
	}
	
	public void init() {
		phase = AutonomousPhase.one;
		position = SmartDashboardSetup.getStart();
		
		gyro = Robot.gyro;
		vision = Robot.vision;
		
		xCord = gyro.getDisplacementX();
		yCord = gyro.getDisplacementY();

		// This on start up because we want it to go once.
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
		
		if (isPhaseOneFinished()) {
			phase = AutonomousPhase.two;
			loadPhases();
		} else if (isPhaseTwoFinished()) {
			phase = AutonomousPhase.three;
			loadPhases();
		} else;
		
		
		Scheduler.getInstance().run();
	}
	
	private void loadPhases() {
		if (phase == AutonomousPhase.two) {
			turnToAngle(vision.getLiftAngle());
		} else if (phase == AutonomousPhase.three) {
			driveToLift(Robot.leddar.getDistances().get(8));
		} else;
	}
	
	private void updateCoordinates() {
		xCord = gyro.getDisplacementX();
		yCord = gyro.getDisplacementY();
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
		group.addSequential(new Turn(RobotMap.TURN_ANGLE, true));
		
		Scheduler.getInstance().add(group);
	}

	private void travelToRightLift() {
		driveDistance = RobotMap.DISTANCE_TO_BASELINES + RobotMap.ROBOT_WIDTH;

		CommandGroup group = new CommandGroup();
		group.addSequential(new DriveForward(driveDistance));
		group.addSequential(new WaitCommand(.5));
		group.addSequential(new Turn(-RobotMap.TURN_ANGLE, false)); 
		
		Scheduler.getInstance().add(group);
	}

	private void travelToCenterLift() {
		driveDistance = RobotMap.DISTANCE_TO_BASELINES - RobotMap.ROBOT_WIDTH;

		Scheduler.getInstance().add(new DriveForward(driveDistance));
	}
	
	private void turnToAngle(double angle) {
		if (angle != 0) 
			Scheduler.getInstance().add(new Turn(angle, false));
	}
	
	private void driveToLift(LeddarDistanceSensorData data) {
		if (data.getDistanceInCentimeters() != 0 || data != null) {
			Scheduler.getInstance().add(new DriveForward(data.getDistanceInCentimeters() / 100));
		}
	}
	
	public void testDriveCommand(double distance) {
		Scheduler.getInstance().add(new DriveForward(distance));
	}
	
	private boolean isPhaseOneFinished() {
		updateCoordinates();
		double distance = Math.sqrt(xCord*xCord + yCord*yCord);
		
		if (phase == AutonomousPhase.one) {
			switch(position) {
			case 1:
				if (gyro.getAngle() == RobotMap.TURN_ANGLE && distance == RobotMap.DISTANCE_TO_BASELINES + RobotMap.ROBOT_WIDTH)
					return true;
				else return false;
			case 2:
				if (gyro.getAngle() == 0 && distance == RobotMap.DISTANCE_TO_BASELINES - RobotMap.ROBOT_WIDTH) 
					return true;
				else return false;
			case 3:
				if (gyro.getAngle() == -RobotMap.TURN_ANGLE && distance == RobotMap.DISTANCE_TO_BASELINES + RobotMap.ROBOT_WIDTH) 
					return true;
				else return false;
			default:
				System.out.println("No autonomous was selected.");
				return false;
			}
		} else return false;
	}
	
	private boolean isPhaseTwoFinished() {
		updateCoordinates();
		
		if (phase == AutonomousPhase.two) {
			if (vision.getLiftAngle() == 0) return true;
			else return false;
		} else return false;
	}
	
	@Override
	protected void initDefaultCommand() { }
}
