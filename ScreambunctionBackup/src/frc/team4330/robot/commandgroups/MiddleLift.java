package frc.team4330.robot.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.team4330.robot.commands.DriveForwardEncoders;
import frc.team4330.robot.commands.PhaseCompleteCommand;
import frc.team4330.robot.utils.AutonomousPhase;
import frc.team4330.robot.utils.RobotMap;

public class MiddleLift extends CommandGroup {

	public MiddleLift() {
		addSequential(new DriveForwardEncoders(RobotMap.WALL_TO_BASELINE 
				- RobotMap.ROBOT_WIDTH));
		addSequential(new WaitCommand(0.5));
		addSequential(new PhaseCompleteCommand(AutonomousPhase.oneComplete));
	}
}
