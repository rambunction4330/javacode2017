package frc.team4330.robot.subsystems;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.team4330.robot.Robot;
import frc.team4330.robot.SmartDashboardSetup;
import frc.team4330.robot.commandgroups.LeftLift;
import frc.team4330.robot.commandgroups.MiddleLift;
import frc.team4330.robot.commandgroups.RightLift;
import frc.team4330.robot.commands.DriveForward;
import frc.team4330.robot.commands.PhaseCompleteCommand;
import frc.team4330.robot.commands.Turn;
import frc.team4330.robot.commands.ZeroPhaseCommand;
import frc.team4330.robot.utils.AutonomousPhase;

public class AutonomousManager extends Subsystem {

	public AutonomousPhase phase = AutonomousPhase.one;
	private int position;

	private VisionSystem vision;

	public void init() {
		phase = AutonomousPhase.one;
		position = SmartDashboardSetup.getStart();

		vision = Robot.vision;
		
		// This on start up because we want it to go once.
		Scheduler.getInstance().add(new ZeroPhaseCommand());
		switch(position) {
		case 1:
			Scheduler.getInstance().add(new LeftLift());
			break;
		case 2:
			Scheduler.getInstance().add(new RightLift());
			break;
		case 3:
			Scheduler.getInstance().add(new MiddleLift());
			break;
		default:
			System.out.println("No autonomous was selected.");
			break;
		}

		Scheduler.getInstance().enable(); 
	}

	public void run() {
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
			System.out.println("Phase three loaded.");
			Double distance = Robot.getLeddarDistance(7);
			if ( distance == null ) {
				System.out.println("Leddar doesn't know distance");
				driveToLift(0.5);
			} else {
				System.out.println("Leddar says distance is " + distance);
				driveToLift(distance);
			}
		} else;
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
		return phase == AutonomousPhase.oneComplete;
	}

	private boolean isPhaseTwoFinished() {
		return phase == AutonomousPhase.twoComplete;
	}
	
	private boolean isPhaseThreeFinished() {
		return phase == AutonomousPhase.threeComplete;
	}

	@Override
	protected void initDefaultCommand() { }
}
