package frc.team4330.robot.commands

import edu.wpi.first.wpilibj.command.Command
import frc.team4330.robot.Robot

class ZeroPhaseCommand : Command() {

    override fun isFinished(): Boolean {
        return !Robot.gyro.isCalibrating
    }

}
