package frc.team4330.screambunction.subsystems;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.team4330.screambunction.commands.DriveForward;
import frc.team4330.screambunction.commands.Turn;
import frc.team4330.screambunction.utils.RobotMap;
import frc.team4330.sensors.distance.LeddarDistanceSensorData;

public class AutonomousManager extends Subsystem {

	Command turn = null;
	Command driveInitial = null;
	Command driveLift = null;
	
	public void travelToLeftLift() {
		CommandGroup group = new CommandGroup();
		group.addSequential(new DriveForward(RobotMap.DISTANCE_TO_BASELINES
				+ RobotMap.ROBOT_WIDTH));
		group.addSequential(new WaitCommand(.5));
		group.addSequential(new Turn(90, true));
		
		Scheduler.getInstance().add(group);
	}

	public void travelToRightLift() {
		CommandGroup group = new CommandGroup();
		group.addSequential(new DriveForward(RobotMap.DISTANCE_TO_BASELINES
				+ RobotMap.ROBOT_WIDTH));
		group.addSequential(new Turn(-90, false)); 
		Scheduler.getInstance().add(group);

	}

	public void travelToCenterLift() {
		Scheduler.getInstance().add(new DriveForward(RobotMap.DISTANCE_TO_BASELINES 
				- RobotMap.ROBOT_WIDTH));
	}
	
	/**
	 * Handles the commands to turn towards the lift.
	 * 
	 * @param angle The angle of the vision processor.
	 * @return True if it is a success.
	 */
	public void turnToAngle(double angle) {
		if (angle != 0) Scheduler.getInstance().add(new Turn(angle, false));
	}
	
	public void driveToLift(LeddarDistanceSensorData data) {
		if (data != null && data.getDistanceInCentimeters() != 0 && driveLift == null) {
			driveLift = new DriveForward(data.getDistanceInCentimeters()/100);
		}
		
		if (driveLift != null) Scheduler.getInstance().add(driveLift);
	}

	@Override
	protected void initDefaultCommand() { }
}
