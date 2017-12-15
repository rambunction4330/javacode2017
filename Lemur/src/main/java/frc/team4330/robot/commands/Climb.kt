package frc.team4330.robot.commands

import edu.wpi.first.wpilibj.command.Command
import frc.team4330.robot.Robot
import frc.team4330.robot.RobotMap

class Climb : Command() {
    init {
        requires(Robot.tarzan)
    }

    override fun initialize() {
        Robot.tarzan.set(RobotMap.CLIMB_SPEED)
    }

    override fun isFinished(): Boolean {
        return false
    }

    override fun end() {
        Robot.tarzan.stop()
    }

}
