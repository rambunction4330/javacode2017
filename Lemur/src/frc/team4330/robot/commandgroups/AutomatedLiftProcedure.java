package frc.team4330.robot.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.team4330.robot.commands.LeddarDrive;
import frc.team4330.robot.commands.VisionTurn;

/**
 * A command for driving to the lift automatically.
 * 
 * @author Amanda
 */
public class AutomatedLiftProcedure extends CommandGroup {

	public AutomatedLiftProcedure() {
		addSequential(new VisionTurn());
		addSequential(new WaitCommand(0.5));
		addSequential(new LeddarDrive());
	}
}
