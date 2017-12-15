package frc.team4330.robot.commands

import edu.wpi.first.wpilibj.command.Command
import frc.team4330.robot.Robot
import frc.team4330.robot.utils.AutonomousPhase

/**
 * Call to complete a phase in autonomous.
 *
 * @author Randy
 */
class PhaseCompleteCommand(private val phase: AutonomousPhase) : Command() {

    override fun execute() {
        Robot.steveBannon.phase = this.phase
    }

    override fun isFinished(): Boolean {
        return true
    }


}
