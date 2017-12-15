package frc.team4330.robot.commandgroups

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team4330.robot.RobotMap
import frc.team4330.robot.commands.EncoderDrive
import frc.team4330.robot.commands.GyroTurn
import frc.team4330.robot.commands.ZeroPhaseCommand

/**
 * This commandGroup loads all the autonomous commands in order.
 *
 * @author Amanda
 */
class AutonomousCommand(position: Int) : CommandGroup() {

    init {
        addSequential(ZeroPhaseCommand())

        when (position) {
            1 -> {
                addSequential(EncoderDrive(RobotMap.WALL_TO_BASELINE))
                addSequential(WaitCommand(.5))
                addSequential(GyroTurn(RobotMap.TURN_ANGLE, false))
            }
            2 -> {
                addSequential(EncoderDrive(RobotMap.WALL_TO_BASELINE))
                addSequential(WaitCommand(.5))
                addSequential(GyroTurn(-RobotMap.TURN_ANGLE, false))
            }
            3 -> {
                addSequential(EncoderDrive(RobotMap.WALL_TO_BASELINE - RobotMap.ROBOT_WIDTH))
                addSequential(WaitCommand(0.5))
            }
        }

        addSequential(WaitCommand(.5))
        addSequential(AutomatedLiftProcedure())
    }
}
