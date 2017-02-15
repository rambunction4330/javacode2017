package frc.team4330.robot.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.team4330.robot.Robot;
import frc.team4330.robot.commands.DriveForwardEncoders;
import frc.team4330.robot.commands.Turn;
import frc.team4330.robot.subsystems.VisionSystem;
import frc.team4330.robot.utils.RobotMap;
import frc.team4330.sensors.distance.LeddarDistanceSensor;

public class AutomatedLiftProcedure extends CommandGroup {

	public AutomatedLiftProcedure(VisionSystem vision, LeddarDistanceSensor leddar) {
		if (vision.getLiftAngle() != null)
			addSequential(new Turn(vision.getLiftAngle(), false));
		else System.out.println("Vision angle error.");
		
		addSequential(new WaitCommand(0.5));
		
		if (Robot.getLeddarDistance(8) != null)
			addSequential(new DriveForwardEncoders(Robot.getLeddarDistance(8) - RobotMap.ONE_FOOT));
		else System.out.println("Leddar distance error.");
	}
}
