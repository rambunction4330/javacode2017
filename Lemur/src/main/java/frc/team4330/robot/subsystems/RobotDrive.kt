package frc.team4330.robot.subsystems

import edu.wpi.first.wpilibj.Encoder
import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.SpeedController
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team4330.robot.RobotMap
import frc.team4330.robot.utils.Registrar
import com.ctre.CANTalon

/**
 * RobotDrive drives the robot!!!
 *
 * @author Amander
 */
class RobotDrive : Subsystem() {
    private val rightMotor1: CANTalon
    private val rightMotor2: SpeedController
    private val leftMotor1: CANTalon
    private val leftMotor2: SpeedController
    private val left: Encoder
    private val right: Encoder

    private var reverse = false
    private var lastPressed = false

    internal var leftval = 0.0
    internal var rightval = 0.0
    internal val inc = .02

    val rightDistance: Double
        get() = right.distance

    val leftDistance: Double
        get() = left.distance

    init {
        rightMotor1 = CANTalon(RobotMap.MOTOR_ONE_PORT)
        rightMotor2 = Registrar.victor(RobotMap.MOTOR_FOUR_PORT)
        leftMotor1 = CANTalon(RobotMap.MOTOR_THREE_PORT)
        leftMotor2 = Registrar.victor(RobotMap.MOTOR_TWO_PORT)

        rightMotor1.inverted = true
        rightMotor2.inverted = true
        leftMotor1.inverted = false
        leftMotor2.inverted = false

        left = Encoder(RobotMap.ENCODER_LEFT_ONE_PORT, RobotMap.ENCODER_LEFT_TWO_PORT)
        right = Encoder(RobotMap.ENCODER_RIGHT_ONE_PORT, RobotMap.ENCODER_RIGHT_TWO_PORT)
        left.setDistancePerPulse(RobotMap.DISTANCE_PER_PULSE)
        right.setDistancePerPulse(RobotMap.DISTANCE_PER_PULSE)

        left.reset()
        right.reset()
    }

    private fun reverseDrive(button: Boolean) {
        if (!lastPressed && button) {
            reverse = !reverse
            if (reverse) {
                println("*************** Robot has reverse ***************")
            }
        }

        lastPressed = button
    }

    /**
     * For using tank drive to drive the robot with Joysticks.
     *
     * @param left the left joystick.
     * @param right the right joystick.
     * @param button button for reversing the drive directrion.
     */
    fun tankDrive(left: Joystick, right: Joystick, button: Boolean) {
        reverseDrive(button)

        if (reverse) {
            automatedDrive(right.y * RobotMap.FAST_SPEED, left.y * RobotMap.FAST_SPEED)
        } else {
            automatedDrive(-left.y * RobotMap.FAST_SPEED, -right.y * RobotMap.FAST_SPEED)
            // negative because joysticks.
        }
    }

    fun curveDrive(left: Joystick, right: Joystick) {
        if (leftMotor2.get() < -left.y && left.y < 0)
            leftval += inc
        else if (leftMotor2.get() > -left.y && left.y > 0)
            leftval -= inc
        else
            leftval = left.y

        if (rightMotor2.get() < -right.y && right.y < 0)
            rightval += inc
        else if (rightMotor2.get() > -right.y && right.y > 0)
            rightval -= inc
        else
            rightval = 0.0

        automatedDrive(leftval, rightval)
    }

    // TODO Uncomment for testing only.
    //	SpeedController motor = new Talon(RobotMap.MOTOR_FOUR_PORT);

    /**
     * Method for testing the tank drive wheels. Uncomment one at a time. Set at slow speed
     * for visibility & safety.
     *
     * @param stick the left joystick, used for testing wheel direction and position.
     */
    fun tankTesting(stick: Joystick) {
        // TODO Uncomment for testing only.
        //		motor.set(stick.getY() * RobotMap.TEST_SPEED);
    }

    /**
     * For using tank drive the robot with set speeds.
     *
     * @param leftv the speed of the left wheels.
     * @param rightv the speed of the right wheels.
     */
    fun automatedDrive(leftv: Double, rightv: Double) {
        if (leftv > 1 || rightv > 1 || leftv < -1 || rightv < -1) {
            println("Check speed values.")
        } else {
            rightMotor1.set(rightv)
            rightMotor2.set(rightv)
            leftMotor1.set(leftv)
            leftMotor2.set(leftv)
        }
    }

    /**
     * Stops the wheels.
     */
    fun stop() {
        automatedDrive(0.0, 0.0)
    }

    fun resetEncoders() {
        left.reset()
        right.reset()
    }

    fun totalDistance(): Double {
        val val1 = right.distance
        val val2 = left.distance
        val vel1 = right.rate
        val vel2 = left.rate

        return (val1 + val2) / 2

        //		if (vel1 < .001 && vel2 > .001) {
        //			return val2;
        //		} else if (v < .0005 && val1 > .0005) {
        //			return val1;
        //		} else if (val1 > .0005 && val2 > .0005) {
        //			return (val1 + val2) / 2;
        //		} else {
        //			return 0;
        //		}
    }

    override fun initDefaultCommand() {}

}
