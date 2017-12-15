package frc.team4330.robot.commands

import edu.wpi.first.wpilibj.command.Command
import frc.team4330.robot.Robot
import frc.team4330.robot.RobotMap
import frc.team4330.robot.parts.HeadingProvider
import frc.team4330.robot.parts.TankDrive
import frc.team4330.robot.utils.HeadingCalculator

/**
 * This command utilizes the encoders to drive forward in a straight line.
 *
 * @author Amanda
 */
class EncoderDrive : Command {
    private var desDistance: Double = 0.toDouble()
    private var curHeading: Double = 0.toDouble()
    private var pastHeading: Double = 0.toDouble()
    private var startDis: Double = 0.toDouble()
    private var deltaDis: Double = 0.toDouble()

    internal var distanceLeftToDrive = 0.0

    /**
     * A command that can be used to drive forward for a designated distance.
     *
     * @param desDistance The distance for the robot to travel.
     */
    constructor(desDistance: Double) {
        this.desDistance = desDistance
        distanceLeftToDrive = desDistance

        //		requires(Robot.myRobot);
    }

    constructor(desDistance: Double, headingProvider: HeadingProvider, tankDrive: TankDrive) {
        this.desDistance = desDistance

        distanceLeftToDrive = desDistance
    }

    override fun initialize() {

        //	System.out.print("gyro start value: " + gyro.getDisplacementY());
        pastHeading = HeadingCalculator.normalize(Robot.gyro.angle)
        startDis = Robot.myRobot.totalDistance()
    }


    public override fun execute() {
        //		System.out.println("" + distanceLeftToDrive);
        curHeading = HeadingCalculator.normalize(Robot.gyro.angle)

        var rightval = 0.0
        var leftval = 0.0

        deltaDis = Robot.myRobot.totalDistance() - startDis
        distanceLeftToDrive = desDistance - deltaDis

        if (distanceLeftToDrive <= .3) {
            rightval = RobotMap.SLOW_SPEED - .2 + .02
            leftval = RobotMap.SLOW_SPEED - .2
        } else {
            rightval = RobotMap.SLOW_SPEED + .02
            leftval = RobotMap.SLOW_SPEED
        }

        val chg = .2
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

    override fun isFinished(): Boolean {
        return distanceLeftToDrive <= .005
    }

    public override fun end() {
        Robot.myRobot.stop()
    }

    // TODO change back to protected?
    public override fun interrupted() {
        println(this.name + " was interrupted. Stopping " + this.javaClass)
        end()
    }

}
