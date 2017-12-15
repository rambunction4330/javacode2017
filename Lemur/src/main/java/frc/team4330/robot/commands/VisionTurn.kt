package frc.team4330.robot.commands

import edu.wpi.first.wpilibj.PIDController
import edu.wpi.first.wpilibj.PIDOutput
import edu.wpi.first.wpilibj.PIDSource
import edu.wpi.first.wpilibj.PIDSourceType
import edu.wpi.first.wpilibj.command.Command
import frc.team4330.robot.Robot

class VisionTurn : Command() {
    internal var scr: PIDSource = object : PIDSource {

        override fun setPIDSourceType(pidSource: PIDSourceType) {
            var pidSource = pidSource
            pidSource = PIDSourceType.kDisplacement
        }

        override fun pidGet(): Double {
            return if (Robot.vision.liftAngle == null)
                0.0
            else
                Robot.vision.liftAngle!!
        }

        override fun getPIDSourceType(): PIDSourceType {
            return PIDSourceType.kDisplacement
        }
    }
    internal var out: PIDOutput = PIDOutput { output ->
        println("output: " + output)
        Robot.myRobot.automatedDrive(output, -output)
    }
    internal var pid = PIDController(.8, 0.0, 8.0, scr, out, .005)

    init {
        println("Starting a new " + this.name + " command.")
        //		requires(Robot.myRobot);
    }

    override fun initialize() {
        pid.setOutputRange(-.4, .4)
        pid.setpoint = 0.0
        pid.enable()

        println("Initializing " + this.name + " command.")
    }

    override fun isFinished(): Boolean {
        if (Robot.vision.liftAngle != null) {
            if (Math.abs(Robot.vision.liftAngle!!) <= 1) {
                println(this.name + " command is finished.")
                return true
            } else
                return false
        } else
            return false
    }

    override fun end() {
        pid.disable()
        println("Ending " + this.name + " command.")
        Robot.myRobot.stop()
    }

}
