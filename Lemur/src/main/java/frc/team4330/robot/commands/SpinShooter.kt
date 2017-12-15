package frc.team4330.robot.commands

import edu.wpi.first.wpilibj.command.Command
import frc.team4330.robot.Robot

/**
 * A command that runs the shooter (independent of the feeder.)
 *
 * @author Amanda
 */
class SpinShooter : Command() {
    init {
        requires(Robot.bam)
    }

    override fun initialize() {
        Robot.bam.start()
    }

    override fun isFinished(): Boolean {
        return false
    }

    override fun end() {
        Robot.bam.stop()
    }

    override fun interrupted() {
        println(this.name + "was interrupted!")
        end()
    }
}
