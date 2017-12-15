package frc.team4330.robot.subsystems

import edu.wpi.first.wpilibj.SpeedController
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team4330.robot.RobotMap
import frc.team4330.robot.utils.Registrar

class Shooter : Subsystem() {

    private val motor: SpeedController
    private val motor2: SpeedController

    private var motorVal = 0.0
    private var motor2Val = 0.0

    init {
        motor = Registrar.victor(RobotMap.MOTOR_SHOOT_PORT)
        motor2 = Registrar.victor(RobotMap.MOTOR_FEED_PORT)
    }

    /**
     * To operate the shooter manually (via Joysticks).
     *
     * @param buttonOff
     * @param buttonOn
     * @param feederOn
     * @param addPwr
     * @param subPwr
     */
    fun manualShoot(buttonOff: Boolean, buttonOn: Boolean, feederOn: Boolean, feederOff: Boolean) {
        if (buttonOff)
            stop()
        else if (buttonOn) motorVal = RobotMap.SHOOTING_SPEED

        if (feederOn && motor.get() >= RobotMap.SHOOTING_SPEED - .05) {
            this.motor2Val = RobotMap.FEEDING_SPEED
        } else if (feederOn && motor.get() <= RobotMap.SHOOTING_SPEED - .05) {
            println("Turning motor on. Let motor power up.")
            motorVal = RobotMap.SHOOTING_SPEED
        } else if (feederOff) stopFeed()

        motor.set(motorVal)
        motor2.set(motor2Val)
    }

    fun autoShoot(shooter: Boolean, feeder: Boolean) {
        if (shooter) motor.set(RobotMap.SHOOTING_SPEED)

        if (feeder && motor.get() >= RobotMap.SHOOTING_SPEED - .05)
            motor2.set(RobotMap.FEEDING_SPEED)
    }

    fun stop() {
        stopFeed()
        stopShooter()
    }

    fun stopFeed() {
        motor2Val = 0.0
        motor2.set(motor2Val)
    }

    fun stopShooter() {
        motorVal = 0.0
        motor.set(motorVal)
    }

    fun testFeeder() {
        motor2Val = -.99
        motor2.set(motor2Val)
    }

    fun testWheel() {
        motorVal = .7
        motor.set(motorVal)
    }

    override fun initDefaultCommand() {}

    fun automatedShooter() {
        motor.set(RobotMap.SHOOTING_SPEED)
    }

    fun automatedFeeder() {
        motor2.set(RobotMap.FEEDING_SPEED)
    }
}
