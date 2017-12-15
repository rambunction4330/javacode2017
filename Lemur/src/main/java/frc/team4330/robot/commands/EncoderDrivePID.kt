package frc.team4330.robot.commands

import edu.wpi.first.wpilibj.PIDController
import edu.wpi.first.wpilibj.PIDOutput
import edu.wpi.first.wpilibj.PIDSource
import edu.wpi.first.wpilibj.PIDSourceType
import edu.wpi.first.wpilibj.command.Command
import frc.team4330.robot.Robot
import frc.team4330.robot.utils.HeadingCalculator

/**
 * LeddarDrive uses the Leddar to drive the robot forward until it is a certain distance
 * (see RobotMap) from the "wall" or a flat surface in front of it using sector 8.
 *
 * @author Amanda
 */
class EncoderDrivePID
/**
 * A command that can be used to drive forward for a designated distance.
 *
 * @param desDistance The distance for the robot to travel.
 */
(private val desDistance: Double)//		requires(Robot.myRobot);
    : Command() {
    private var curHeading: Double = 0.toDouble()
    private var pastHeading: Double = 0.toDouble()
    var `val` = 0.0
    var basespeed = 0.0

    internal var scr: PIDSource = object : PIDSource {

        override fun setPIDSourceType(pidSource: PIDSourceType) {}

        override fun pidGet(): Double {
            return Robot.myRobot.totalDistance() - startDis
        }

        override fun getPIDSourceType(): PIDSourceType {
            return PIDSourceType.kDisplacement
        }
    }
    internal var out: PIDOutput = PIDOutput { output ->
        basespeed = output

        curHeading = Robot.gyro.angle

        var rightval = basespeed
        var leftval = basespeed

        val chg = .1
        val thr = 2.0
        val courseChange = HeadingCalculator.calculateCourseChange(curHeading, pastHeading)

        if (courseChange > thr) {
            rightval -= chg
            leftval += chg
        } else if (courseChange < -thr) {
            leftval -= chg
            rightval += chg
        }


        Robot.myRobot.automatedDrive(leftval, rightval)
    }
    internal var pid = PIDController(.8, 0.0, 8.0, scr, out, .005)
    private var startDis: Double = 0.toDouble()

    override fun initialize() {
        println("Initializing " + this.name + " command.")
        startDis = Robot.myRobot.totalDistance()

        pastHeading = Robot.gyro.angle
        pid.setOutputRange(-.4, .4)
        pid.setpoint = desDistance
        pid.enable()
    }

    override fun isFinished(): Boolean {
        if (Robot.myRobot.totalDistance() - startDis >= desDistance) {
            println(this.name + " command is finished.")
            return true
        } else
            return false
    }

    override fun end() {
        pid.disable()
        Robot.myRobot.stop()
    }

}