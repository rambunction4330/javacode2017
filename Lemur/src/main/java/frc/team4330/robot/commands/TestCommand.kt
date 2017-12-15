package frc.team4330.robot.commands

import edu.wpi.first.wpilibj.command.Command
import frc.team4330.robot.Robot

/**
 * Simple command that drives robot forward. This was used to test the OI feature.
 *
 * @author 4330
 */
class TestCommand : Command() {
    init {
        println("Creating new " + this.name)
        requires(Robot.myRobot)
    }

    override fun execute() {
        println("Executing " + this.name)
        Robot.myRobot.automatedDrive(.3, .3)
    }

    override fun isFinished(): Boolean {
        return false
    }

    override fun end() {
        Robot.myRobot.stop()
    }

}
