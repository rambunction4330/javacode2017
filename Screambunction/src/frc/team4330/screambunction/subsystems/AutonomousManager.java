package frc.team4330.screambunction.subsystems;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team4330.screambunction.commands.DriveForward;
import frc.team4330.screambunction.commands.Turn;
import frc.team4330.screambunction.utils.RobotMap;

public class AutonomousManager extends Subsystem {

	public void travelToLeftLift() {
		CommandGroup group = new CommandGroup();
		
		group.addSequential(new DriveForward(RobotMap.DISTANCE_TO_BASELINES
				+ RobotMap.ROBOT_WIDTH));
		group.addSequential(new Turn(-90));
		
		Scheduler.getInstance().add(group);
	}
	
	public void travelToRightLift() {
		CommandGroup group = new CommandGroup();

		group.addSequential(new DriveForward(RobotMap.DISTANCE_TO_BASELINES + RobotMap.ROBOT_WIDTH));
		group.addSequential(new Turn(90));
		
		Scheduler.getInstance().add(group);
	}
	
	public void travelToCenterLift() {
		CommandGroup group = new CommandGroup();

		group.addSequential(new DriveForward(RobotMap.DISTANCE_TO_BASELINES - RobotMap.ROBOT_WIDTH));
		
		Scheduler.getInstance().add(group);
	}

	@Override
	protected void initDefaultCommand() { }
}
