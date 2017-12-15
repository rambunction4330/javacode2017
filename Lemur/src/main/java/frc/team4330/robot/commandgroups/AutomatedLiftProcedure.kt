package frc.team4330.robot.commandgroups

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team4330.robot.commands.GyroTurn
import frc.team4330.robot.commands.LeddarDrive

/**
 * A command for driving to the lift automatically.
 *
 * @author Amanda
 */
class AutomatedLiftProcedure : CommandGroup() {
    init {
        addSequential(GyroTurn(0.0, true))
        addSequential(WaitCommand(0.5))
        addSequential(GyroTurn(0.0, true))
        addSequential(WaitCommand(0.5))
        addSequential(LeddarDrive())
    }
}
