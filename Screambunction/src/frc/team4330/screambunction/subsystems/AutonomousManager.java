package frc.team4330.screambunction.subsystems;

import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team4330.screambunction.commands.DriveForward;
import frc.team4330.screambunction.commands.Turn;
import frc.team4330.screambunction.utils.RobotMap;

public class AutonomousManager extends Subsystem {

	public void travelToLeftLift() {
		Scheduler.getInstance().add(new DriveForward(RobotMap.DISTANCE_TO_BASELINES
				+ RobotMap.ROBOT_WIDTH));
		Scheduler.getInstance().add(new Turn(-90));
	}

	public void travelToRightLift() {
		Scheduler.getInstance().add(new DriveForward(RobotMap.DISTANCE_TO_BASELINES
				+ RobotMap.ROBOT_WIDTH));
		Scheduler.getInstance().add(new Turn(90));

	}

	public void travelToCenterLift() {
		Scheduler.getInstance().add(new DriveForward(RobotMap.DISTANCE_TO_BASELINES 
				- RobotMap.ROBOT_WIDTH));
	}

	@Override
	protected void initDefaultCommand() { }
}
