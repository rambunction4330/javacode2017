package frc.team4330.robot.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.team4330.robot.RobotMap;
import frc.team4330.robot.commands.EncoderDrive;
import frc.team4330.robot.commands.LeddarDrive;
import frc.team4330.robot.commands.GyroTurn;
import frc.team4330.robot.commands.VisionTurn;
import frc.team4330.robot.commands.ZeroPhaseCommand;

/**
 * This commandGroup loads all the autonomous commands in order.
 * 
 * @author Amanda
 */
public class AutonomousCommand extends CommandGroup {

	public AutonomousCommand(int position) {
		addSequential(new ZeroPhaseCommand());
		
		switch(position) {
		case (1):
			addSequential(new EncoderDrive(RobotMap.WALL_TO_BASELINE));
			addSequential(new WaitCommand(.5));
			addSequential(new GyroTurn(RobotMap.TURN_ANGLE, false));
			break;
		case 2:
			addSequential(new EncoderDrive( RobotMap.WALL_TO_BASELINE));
			addSequential(new WaitCommand(.5));
			addSequential(new GyroTurn(-RobotMap.TURN_ANGLE, false)); 
			break;
		case 3:
			addSequential(new EncoderDrive(RobotMap.WALL_TO_BASELINE 
					- RobotMap.ROBOT_WIDTH));
			addSequential(new WaitCommand(0.5));
		}
		
		addSequential(new WaitCommand(.5));
		addSequential(new VisionTurn());
		addSequential(new WaitCommand(.5));
		addSequential(new LeddarDrive());
	}
}
