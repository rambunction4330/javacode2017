package frc.team4330.robot.subsystems

import edu.wpi.first.wpilibj.SpeedController
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team4330.robot.RobotMap
import frc.team4330.robot.utils.Registrar

class ShooterWheel : Subsystem() {
    private val motor: SpeedController

    init {
        motor = Registrar.victor(RobotMap.MOTOR_SHOOT_PORT)
    }

    fun teleShoot(powerOn: Boolean, powerOff: Boolean) {
        if (powerOff)
            stop()
        else if (powerOn) start()
    }

    fun start() {
        motor.set(RobotMap.SHOOTING_SPEED)
    }

    fun stop() {
        motor.set(0.0)
    }

    override fun initDefaultCommand() {

    }

}
