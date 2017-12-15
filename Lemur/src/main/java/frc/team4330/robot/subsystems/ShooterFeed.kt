package frc.team4330.robot.subsystems

import edu.wpi.first.wpilibj.SpeedController
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team4330.robot.RobotMap
import frc.team4330.robot.utils.Registrar

class ShooterFeed : Subsystem() {
    private val motor: SpeedController

    init {
        motor = Registrar.victor(RobotMap.MOTOR_FEED_PORT)
    }

    fun teleShoot(powerOn: Boolean, powerOff: Boolean) {
        if (powerOff)
            stop()
        else if (powerOn) start()
    }

    fun start() {
        motor.set(RobotMap.FEEDING_SPEED)
    }

    fun stop() {
        motor.set(0.0)
    }

    override fun initDefaultCommand() {
        // TODO Auto-generated method stub

    }

}
