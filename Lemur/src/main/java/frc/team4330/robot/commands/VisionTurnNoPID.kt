package frc.team4330.robot.commands

import edu.wpi.first.wpilibj.command.Command
import frc.team4330.robot.Robot
import frc.team4330.robot.RobotMap


/**
 * Turn Command for if you don't know the current or desired heading.
 *
 * @param headingChange The change in heading. Negative means to the right.
 */
class VisionTurnNoPID : Command() {

    public override fun execute() {
        if (Robot.vision.liftAngle == null)
        // no angle
            Robot.myRobot.stop()
        if (Robot.vision.liftAngle!! > 0.0) { // means we need to turn right
            Robot.myRobot.automatedDrive(RobotMap.SLOW_SPEED, -RobotMap.SLOW_SPEED)
        } else { // need to turn left
            Robot.myRobot.automatedDrive(-RobotMap.SLOW_SPEED, RobotMap.SLOW_SPEED)
        }
    }

    override fun isFinished(): Boolean {
        return if (Robot.vision.liftAngle != null)
            Math.abs(Robot.vision.liftAngle!!) <= 2
        else
            false
    }

    override fun end() {
        Robot.myRobot.stop()
    }


    override fun interrupted() {
        println(this.name + " was interrupted. Stopping " + this.javaClass)
        end()
    }
}//		requires (Robot.myRobot);
