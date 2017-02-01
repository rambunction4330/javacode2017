package frc.team4330.screambunction.subsystems;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team4330.screambunction.canbus.LeddarDistanceSensor.LeddarDistanceSensorData;
import frc.team4330.screambunction.commands.DriveForward;
import frc.team4330.screambunction.commands.Turn;
import frc.team4330.screambunction.utils.RobotMap;

public class AutonomousManager extends Subsystem {

	Command turn = null;
	Command driveInitial = null;
	Command driveLift = null;
	
	public void travelToLeftLift() {
		if (driveInitial == null) {
			driveInitial = new DriveForward(RobotMap.DISTANCE_TO_BASELINES
					+ RobotMap.ROBOT_WIDTH);
		}
				
		CommandGroup group = new CommandGroup();
		group.addSequential(driveInitial);
		group.addSequential(new Turn(-90));
		
		Scheduler.getInstance().add(group);
	}

	public void travelToRightLift() {
		if (driveInitial == null) {
			driveInitial = new DriveForward(RobotMap.DISTANCE_TO_BASELINES
					+ RobotMap.ROBOT_WIDTH);
		}
				
		Scheduler.getInstance().add(driveInitial);
		Scheduler.getInstance().add(new Turn(90));

	}

	public void travelToCenterLift() {
		if (driveInitial == null) {
			driveInitial = new DriveForward(RobotMap.DISTANCE_TO_BASELINES 
					- RobotMap.ROBOT_WIDTH);
		}
				
		Scheduler.getInstance().add(driveInitial);
	}
	
	/**
	 * Handles the commands to turn towards the lift.
	 * 
	 * @param angle The angle of the vision processor.
	 * @return True if it is a success.
	 */
	public boolean turnToAngle(double angle) {
		if (angle != 0 && turn == null) {
			turn = new Turn(angle);
		}
		
		if (turn != null) {
			Scheduler.getInstance().add(turn);
			return true;
		} else return false;
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
