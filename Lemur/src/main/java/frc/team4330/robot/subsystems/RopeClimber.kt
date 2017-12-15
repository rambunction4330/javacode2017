package frc.team4330.robot.subsystems

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.SpeedController
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team4330.robot.RobotMap
import frc.team4330.robot.utils.Registrar

class RopeClimber : Subsystem() {
    private val motor: SpeedController

    init {
        motor = Registrar.victor(RobotMap.MOTOR_CLIMB_PORT)
        motor.inverted = true
    }

    fun manualClimb(stick: Joystick) {
        set(Math.abs(stick.y))
    }

    fun setClimb(slowSpd: Boolean, fastSpd: Boolean) {
        if (slowSpd)
            set(RobotMap.REEL_SPEED)
        else if (fastSpd)
            set(RobotMap.CLIMB_SPEED)
        else
            stop()
    }

    fun testClimb(slowSpd: Boolean, fastSpd: Boolean, backwards: Boolean) {
        if (backwards)
            set(-RobotMap.REEL_SPEED)
        else if (slowSpd)
            set(RobotMap.REEL_SPEED)
        else if (fastSpd)
            set(RobotMap.CLIMB_SPEED)
        else
            stop()
    }

    fun stop() {
        set(0.0)
    }

    override fun initDefaultCommand() {}

    fun set(spd: Double) {
        motor.set(spd)
    }

}
