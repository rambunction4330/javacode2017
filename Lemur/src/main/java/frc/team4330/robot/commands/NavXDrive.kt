package frc.team4330.robot.commands

import com.kauailabs.navx.frc.AHRS

import edu.wpi.first.wpilibj.command.Command
import frc.team4330.robot.Robot
import frc.team4330.robot.parts.HeadingProvider
import frc.team4330.robot.parts.TankDrive
import frc.team4330.robot.subsystems.RobotDrive
import frc.team4330.robot.utils.HeadingCalculator

/**
 * This command utilizes the navX function of getDisplacementX() and getDisplacementY() to
 * drive the robot forward. It does not work seeing as the two function of the navX are HIGHLY
 * experimental. Use at your own risk.
 *
 * @author Amanda
 */
class NavXDrive : Command {
    private var desDistance: Double = 0.toDouble()
    private var curHeading: Double = 0.toDouble()
    private var pastHeading: Double = 0.toDouble()
    private var startX: Double = 0.toDouble()
    private var startY: Double = 0.toDouble()
    private var deltaX: Double = 0.toDouble()
    private var deltaY: Double = 0.toDouble()
    private var deltaDis: Double = 0.toDouble()
    private var pastY: Double = 0.toDouble()
    private var pastX: Double = 0.toDouble()

    private var gyro: AHRS? = null
    private var robot: RobotDrive? = null

    internal var distanceLeftToDrive = 0.0

    /**
     * A command that can be used to drive forward for a designated distance.
     *
     * @param desDistance The distance for the robot to travel.
     */
    constructor(desDistance: Double) {
        this.desDistance = desDistance
        distanceLeftToDrive = desDistance

        pastX = 0.0
        pastY = 0.0

        requires(Robot.myRobot)
    }

    constructor(desDistance: Double, headingProvider: HeadingProvider, tankDrive: TankDrive) {
        this.desDistance = desDistance

        distanceLeftToDrive = desDistance
    }

    override fun initialize() {
        this.gyro = Robot.gyro
        robot = Robot.myRobot

        //	System.out.print("gyro start value: " + gyro.getDisplacementY());
        pastHeading = HeadingCalculator.normalize(Robot.gyro.angle)
        startX = gyro!!.displacementX.toDouble()
        startY = gyro!!.displacementY.toDouble()
    }


    public override fun execute() {
        //		System.out.println("" + distanceLeftToDrive);
        curHeading = HeadingCalculator.normalize(gyro!!.angle)

        var rightval = 0.0
        var leftval = 0.0

        deltaX = gyro!!.displacementX - startX
        deltaY = gyro!!.displacementY - startY
        deltaDis = Math.sqrt(deltaX * deltaX + deltaY * deltaY)
        distanceLeftToDrive = desDistance - deltaDis

        if (distanceLeftToDrive <= .5) {
            rightval = .3
            leftval = .3
        } else {
            rightval = .3
            leftval = .3
        }

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


        robot!!.automatedDrive(leftval, rightval)
        pastX = gyro!!.displacementX.toDouble()
        pastY = gyro!!.displacementY.toDouble()
    }

    override fun isFinished(): Boolean {
        return distanceLeftToDrive <= .005 || gyro!!.displacementX - pastX < 0 || gyro!!.displacementY - pastY < 0
    }

    public override fun end() {
        //System.out.print("gyro finished value: " + robot.totalDistance());
        robot!!.stop()
    }

    // TODO change back to protected?
    public override fun interrupted() {
        robot!!.stop()
    }

}
