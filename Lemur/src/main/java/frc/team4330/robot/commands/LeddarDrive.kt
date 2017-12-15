package frc.team4330.robot.commands

import edu.wpi.first.wpilibj.PIDController
import edu.wpi.first.wpilibj.PIDOutput
import edu.wpi.first.wpilibj.PIDSource
import edu.wpi.first.wpilibj.PIDSourceType
import edu.wpi.first.wpilibj.command.Command
import frc.team4330.robot.Robot
import frc.team4330.robot.RobotMap
import frc.team4330.robot.utils.HeadingCalculator

/**
 * LeddarDrive uses the Leddar to drive the robot forward until it is a certain distance
 * (see RobotMap) from the "wall" or a flat surface in front of it using sector 8.
 *
 * @author Amanda
 */
class LeddarDrive : Command() {
    private var curHeading: Double = 0.toDouble()
    private var pastHeading: Double = 0.toDouble()
    var `val` = 0.0
    var basespeed = 0.0

    internal var scr: PIDSource = object : PIDSource {

        override fun setPIDSourceType(pidSource: PIDSourceType) {}

        override fun pidGet(): Double {
            if (Robot.getLeddarDistance(RobotMap.LEDDAR_SEGMENT) == null) return 0.0

            `val` = RobotMap.DESIRED_DISTANCE_FROM_WALL - Robot.getLeddarDistance(RobotMap.LEDDAR_SEGMENT)!!
            return `val`
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

    init {
        println("Starting a new " + this.name + " command.")

        //		requires(Robot.myRobot);
    }

    override fun initialize() {
        println("Initializing " + this.name + " command.")

        pastHeading = Robot.gyro.angle
        pid.setOutputRange(-.4, .4)
        pid.setpoint = 0.0
        pid.enable()
    }

    override fun isFinished(): Boolean {
        if (Robot.getLeddarDistance(RobotMap.LEDDAR_SEGMENT) != null) {
            if (Robot.getLeddarDistance(RobotMap.LEDDAR_SEGMENT)!! <= RobotMap.DESIRED_DISTANCE_FROM_WALL + .02) {
                println(this.name + " command is finished.")
                return true
            } else
                return false
        } else
            return false
    }

    override fun end() {
        pid.disable()
        Robot.myRobot.stop()
    }

}
