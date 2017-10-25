package frc.team4330.screambunction.subsystems;

import java.util.List;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.team4330.screambunction.Robot;
import frc.team4330.screambunction.SmartDashboardSetup;
import frc.team4330.screambunction.commands.DriveForward;
import frc.team4330.screambunction.commands.PhaseCompleteCommand;
import frc.team4330.screambunction.commands.Turn;
import frc.team4330.screambunction.utils.AutonomousPhase;
import frc.team4330.screambunction.utils.RobotMap;
import frc.team4330.sensors.distance.LeddarDistanceSensor;
import frc.team4330.sensors.distance.LeddarDistanceSensorData;

@SuppressWarnings("unused")
public class AutonomousManager extends Subsystem {
	private double xCord, yCord;

	public AutonomousPhase phase = AutonomousPhase.one;
	private int position;

	private AHRS gyro;
	private VisionSystem vision;
	private LeddarDistanceSensor leddar;

	public void init() {
		phase = AutonomousPhase.one;
		position = SmartDashboardSetup.getStart();

		gyro = Robot.gyro;
		vision = Robot.vision;
		leddar = Robot.leddar;

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
		if ( phase != AutonomousPhase.done) {
			updateCoordinates();
		}

		if (isPhaseOneFinished()) {
			System.out.println("Phase one finished.");
			phase = AutonomousPhase.two;
			loadPhases();
		} else if (isPhaseTwoFinished()) {
			System.out.println("Phase two finished.");
			phase = AutonomousPhase.three;
			loadPhases();
		} else if (isPhaseThreeFinished()) {
			System.out.println("Phase three finished.");
			phase = AutonomousPhase.done;
			System.out.println("Autonomous is done");
		} else;

		Scheduler.getInstance().run();
	}

	private void loadPhases() {
		if (phase == AutonomousPhase.two) {
			if (vision.getLiftAngle() != null) {
				double visionTargetAngle = vision.getLiftAngle();
				System.out.println("Vision reporting angle " + visionTargetAngle);
				turnToAngle(visionTargetAngle);
				System.out.println("Phase two loaded.");
			} else {
				System.out.println("Phase two skipped.");
				phase = AutonomousPhase.three;
				loadPhases();
			}
		} else if (phase == AutonomousPhase.three) {
			System.out.println("Phase three loaded");
			Double distance = getDistanceToWall();
			if ( distance == null ) {
				System.out.println("Leddar doesn't know distance");
				driveToLift(0.5);
			} else {
				System.out.println("Leddar says distance is " + distance);
				driveToLift(distance);
			}
		} else;
	}
	
	private Double getDistanceToWall ( ) {
		List<LeddarDistanceSensorData> distances = Robot.leddar.getDistances();
		if ( distances.isEmpty() ) {
			return null;
		}
		for ( LeddarDistanceSensorData distance: distances ) {
			if ( distance.getSegmentNumber() == 8 ) {
				return distance.getDistanceInCentimeters() / 100.0;
			}
		}
		return null;
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
		CommandGroup group = new CommandGroup();
		group.addSequential(new DriveForward(RobotMap.WALL_TO_BASELINE));
		group.addSequential(new WaitCommand(.5));
		group.addSequential(new Turn(RobotMap.TURN_ANGLE, true));
		group.addSequential(new PhaseCompleteCommand(AutonomousPhase.oneComplete));

		Scheduler.getInstance().add(group);
	}

	private void travelToRightLift() {
		CommandGroup group = new CommandGroup();
		group.addSequential(new DriveForward( RobotMap.WALL_TO_BASELINE));
		group.addSequential(new WaitCommand(.5));
		group.addSequential(new Turn(-RobotMap.TURN_ANGLE, false)); 
		group.addSequential(new PhaseCompleteCommand(AutonomousPhase.oneComplete));

		Scheduler.getInstance().add(group);
	}

	private void travelToCenterLift() {
		CommandGroup group = new CommandGroup();
		group.addSequential(new 
				DriveForward(RobotMap.WALL_TO_BASELINE - RobotMap.ROBOT_WIDTH));
		group.addSequential(new WaitCommand(0.5));
		group.addSequential(new PhaseCompleteCommand(AutonomousPhase.oneComplete));
		Scheduler.getInstance().add(group);
	}

	private void turnToAngle(double angle) {
		if (angle == 0) {
			Scheduler.getInstance().add(new PhaseCompleteCommand(AutonomousPhase.twoComplete));
		} else {
			CommandGroup group = new CommandGroup();
			group.addSequential(new Turn(angle, false));
			group.addSequential(new WaitCommand(0.5));
			group.addSequential(new PhaseCompleteCommand(AutonomousPhase.twoComplete));
			Scheduler.getInstance().add(group);
		}
	}

	private void driveToLift(Double distance) {
		CommandGroup group = new CommandGroup();
		group.addSequential(new DriveForward(distance));
		group.addSequential(new WaitCommand(0.5));
		group.addSequential(new PhaseCompleteCommand(AutonomousPhase.threeComplete));
		Scheduler.getInstance().add(group);
	}

	public void testDriveCommand(double distance) {
		Scheduler.getInstance().add(new DriveForward(distance));
	}

	private boolean isPhaseOneFinished() {
		updateCoordinates();
//		double distance = Math.sqrt(xCord*xCord + yCord*yCord);

		return Robot.steveBannon.phase == AutonomousPhase.oneComplete;
	}

	private boolean isPhaseTwoFinished() {
		updateCoordinates();

		return Robot.steveBannon.phase == AutonomousPhase.twoComplete;
	}
	
	private boolean isPhaseThreeFinished() {
		return Robot.steveBannon.phase == AutonomousPhase.threeComplete;
	}

	@Override
	protected void initDefaultCommand() { }
}
