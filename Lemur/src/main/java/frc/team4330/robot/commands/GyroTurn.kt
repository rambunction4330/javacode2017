package frc.team4330.robot.commands

import edu.wpi.first.wpilibj.command.Command
import frc.team4330.robot.Robot
import frc.team4330.robot.parts.HeadingProvider
import frc.team4330.robot.parts.TankDrive
import frc.team4330.robot.utils.HeadingCalculator

/**
 * Uses the gyro to turn. You can imput the angle change you want to make. Absolute
 * determines whether or not you want to change by x amount or go straight to inputed angle.
 *
 * @author Amanda
 */
class GyroTurn : Command {

    private var curHeading: Double = 0.toDouble()
    private var change: Double = 0.toDouble()
    private var desHeading: Double? = null
    private var headingProvider: HeadingProvider? = null
    private var tankDrive: TankDrive? = null
    private var test: Boolean = false

    private var vision: Boolean = false
    private var thing: Double = 0.0

    /**
     * Turn Command for if you don't know the current or desired heading.
     *
     * @param headingChange The change in heading. Negative means to the right.
     */
    constructor(heading: Double, useVision: Boolean) {
        curHeading = Robot.gyro.angle

        this.vision = useVision

        thing = heading

        //		change = HeadingCalculator.calculateCourseChange(curHeading, desHeading);

        test = false

        //		requires (Robot.myRobot);
    }

    /**
     * Used for testing the Turn Command (unit tests).
     *
     * @param desHeading Desired heading.
     * @param headingProvider The angle provider.
     * @param tankDrive Abstract driver.
     */
    constructor(desHeading: Double, headingProvider: HeadingProvider, tankDrive: TankDrive) {
        curHeading = headingProvider.angle
        this.desHeading = desHeading

        this.headingProvider = headingProvider
        this.tankDrive = tankDrive

        test = true
    }

    /**
     * Used to initialize the command.
     */
    override fun initialize() {
        if (!test)
            curHeading = Robot.gyro.angle
        else
            curHeading = HeadingCalculator.normalize(headingProvider!!.angle)


        if (vision && Robot.vision.liftAngle != null)
            this.desHeading = curHeading + Robot.vision.liftAngle!!
        else if (vision)
            desHeading = null
        else
            desHeading = thing

    }

    public override fun execute() {
        if (desHeading == null)
            return

        curHeading = Robot.gyro.angle
        change = HeadingCalculator.calculateCourseChange(curHeading, desHeading!!)

        if (change > 0) { // means we need to turn right
            if (change < 10) {
                Robot.myRobot.automatedDrive(.37, -.37)
            } else {
                Robot.myRobot.automatedDrive(.5, -.5)
            }
        } else { // need to turn left
            if (change > -10) {
                Robot.myRobot.automatedDrive(-.37, .37)
            } else {
                Robot.myRobot.automatedDrive(-.5, .5)
            }
        }

        curHeading = Robot.gyro.angle
        change = HeadingCalculator.calculateCourseChange(curHeading, desHeading!!)
    }

    override fun isFinished(): Boolean {
        return Math.abs(change) <= 2 || desHeading == null
    }

    override fun end() {
        if (!test)
            Robot.myRobot.stop()
        else
            tankDrive?.setSpeed(0.0, 0.0)
    }


    override fun interrupted() {
        println(this.name + " was interrupted. Stopping " + this.javaClass)
        end()
    }

}
