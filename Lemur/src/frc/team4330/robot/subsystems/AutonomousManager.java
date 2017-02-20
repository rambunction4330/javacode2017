package frc.team4330.robot.subsystems;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.team4330.robot.Robot;
import frc.team4330.robot.RobotMap;
import frc.team4330.robot.SmartDashboardSetup;
import frc.team4330.robot.commands.EncoderDrive;
import frc.team4330.robot.commands.GyroTurn;
import frc.team4330.robot.commands.PhaseCompleteCommand;
import frc.team4330.robot.commands.ZeroPhaseCommand;
import frc.team4330.robot.utils.AutonomousPhase;

public class AutonomousManager extends Subsystem {

	public AutonomousPhase phase = AutonomousPhase.one;
	private int position;

	public void init() {
		phase = AutonomousPhase.one;
		position = SmartDashboardSetup.getStart();
		
		// This on start up because we want it to go once.
		CommandGroup group = new CommandGroup();
		Scheduler.getInstance().add(new ZeroPhaseCommand());
		switch(position) {
		case SmartDashboardSetup.left:
			group.addSequential(new EncoderDrive(RobotMap.WALL_TO_BASELINE));
			group.addSequential(new WaitCommand(.5));
			group.addSequential(new GyroTurn(RobotMap.TURN_ANGLE, false));
			break;
		case SmartDashboardSetup.right:
			group.addSequential(new EncoderDrive( RobotMap.WALL_TO_BASELINE));
			group.addSequential(new WaitCommand(.5));
			group.addSequential(new GyroTurn(-RobotMap.TURN_ANGLE, false)); 
			break;
		case SmartDashboardSetup.middle:
			group.addSequential(new EncoderDrive(RobotMap.WALL_TO_BASELINE 
					- RobotMap.ROBOT_WIDTH - 1.1));
			break;
		}
		group.addSequential(new WaitCommand(0.5));
		group.addSequential(new PhaseCompleteCommand(AutonomousPhase.oneComplete));
		Scheduler.getInstance().add(group);

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
			Double visionTargetAngle = Robot.vision.getLiftAngle();
			if (visionTargetAngle != null) {
				System.out.println("Vision reporting angle " + visionTargetAngle);
				turnToAngle();
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

	private void turnToAngle() {
		if (Robot.vision.getLiftAngle() == 0) {
			Scheduler.getInstance().add(new PhaseCompleteCommand(AutonomousPhase.twoComplete));
		} else {
			CommandGroup group = new CommandGroup();
			group.addSequential(new GyroTurn(0, true));
			group.addSequential(new WaitCommand(0.5));
			group.addSequential(new GyroTurn(0, true));
			group.addSequential(new WaitCommand(0.5));
			group.addSequential(new PhaseCompleteCommand(AutonomousPhase.twoComplete));
			Scheduler.getInstance().add(group);
		}
	}

	private void driveToLift(Double distance) {
		CommandGroup group = new CommandGroup();
		group.addSequential(new EncoderDrive(distance));
		group.addSequential(new WaitCommand(0.5));
		group.addSequential(new PhaseCompleteCommand(AutonomousPhase.threeComplete));
		Scheduler.getInstance().add(group);
	}

	public void testDriveCommand(double distance) {
		Scheduler.getInstance().add(new EncoderDrive(distance));
	}
	
	public void testTurnAbsCommand(double angle) {
		Scheduler.getInstance().add(new ZeroPhaseCommand());
		Scheduler.getInstance().add(new GyroTurn(angle, true));
	}
	
	public void testTurnNonCommand(double angle) {
		Scheduler.getInstance().add(new ZeroPhaseCommand());
		Scheduler.getInstance().add(new GyroTurn(angle, false));
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
