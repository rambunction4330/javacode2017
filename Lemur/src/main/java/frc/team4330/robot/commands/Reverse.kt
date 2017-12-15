package frc.team4330.robot.commands

import edu.wpi.first.wpilibj.command.Command
import frc.team4330.robot.Robot
import frc.team4330.robot.RobotMap

class Reverse : Command() {
    init {
        requires(Robot.tarzan)
    }

    override fun initialize() {
        Robot.tarzan.set(-.3)
    }

    override fun isFinished(): Boolean {
        return false
    }

    override fun end() {
        Robot.tarzan.stop()
    }

}
