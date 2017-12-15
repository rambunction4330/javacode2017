package frc.team4330.robot.commands

import edu.wpi.first.wpilibj.command.Command
import frc.team4330.robot.Robot

/**
 * A command that runs the agitator/feeder (independent of the shooter.)
 *
 * @author Amanda
 */
class StartAgitator : Command() {
    init {
        requires(Robot.bamm)
    }

    override fun initialize() {
        Robot.bamm.start()
    }

    override fun isFinished(): Boolean {
        return false
    }

    override fun end() {
        Robot.bamm.stop()
    }

    override fun interrupted() {
        println(this.name + "was interrupted!")
        end()
    }
}
