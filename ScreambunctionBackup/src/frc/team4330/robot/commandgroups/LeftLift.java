package frc.team4330.robot.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.team4330.robot.commands.DriveForwardEncoders;
import frc.team4330.robot.commands.PhaseCompleteCommand;
import frc.team4330.robot.commands.Turn;
import frc.team4330.robot.utils.AutonomousPhase;
import frc.team4330.robot.utils.RobotMap;

public class LeftLift extends CommandGroup {

	public LeftLift() {
		addSequential(new DriveForwardEncoders(RobotMap.WALL_TO_BASELINE));
		addSequential(new WaitCommand(.5));
		addSequential(new Turn(RobotMap.TURN_ANGLE, true));
		addSequential(new PhaseCompleteCommand(AutonomousPhase.oneComplete));
	}
}
