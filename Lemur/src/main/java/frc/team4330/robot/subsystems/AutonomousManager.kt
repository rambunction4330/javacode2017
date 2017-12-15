package frc.team4330.robot.subsystems

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.Scheduler
import edu.wpi.first.wpilibj.command.Subsystem
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team4330.robot.Robot
import frc.team4330.robot.RobotMap
import frc.team4330.robot.DashboardManager
import frc.team4330.robot.commands.EncoderDrive
import frc.team4330.robot.commands.GyroTurn
import frc.team4330.robot.commands.PhaseCompleteCommand
import frc.team4330.robot.commands.ZeroPhaseCommand
import frc.team4330.robot.utils.AutonomousPhase

class AutonomousManager : Subsystem() {

    var phase = AutonomousPhase.one
    private var position: Int? = null


    private val isPhaseOneFinished: Boolean
        get() = phase == AutonomousPhase.oneComplete

    private val isPhaseTwoFinished: Boolean
        get() = phase == AutonomousPhase.twoComplete

    private val isPhaseThreeFinished: Boolean
        get() = phase == AutonomousPhase.threeComplete

    fun init() {
        phase = AutonomousPhase.one
        position = DashboardManager.start
        val leftright_distance = 2.0

        // This on start up because we want it to go once.
        val group = CommandGroup()
        Scheduler.getInstance().add(ZeroPhaseCommand())
        if (position == DashboardManager.left) {
            println("Going to the LEFT.")
            group.addSequential(EncoderDrive(leftright_distance))
            group.addSequential(WaitCommand(RobotMap.WAIT_TIMES))
            group.addSequential(GyroTurn(RobotMap.TURN_ANGLE, false))
        } else if (position == DashboardManager.right) {
            println("Going to the RIGHT.")
            group.addSequential(EncoderDrive(leftright_distance))
            group.addSequential(WaitCommand(RobotMap.WAIT_TIMES))
            group.addSequential(GyroTurn(-RobotMap.TURN_ANGLE, false))
        } else {
            println("Going to the MIDDLE.")
            group.addSequential(EncoderDrive(.6))
        }
        group.addSequential(WaitCommand(RobotMap.WAIT_TIMES))
        group.addSequential(PhaseCompleteCommand(AutonomousPhase.oneComplete))
        Scheduler.getInstance().add(group)

        Scheduler.getInstance().enable()
    }

    fun run() {
        if (isPhaseOneFinished) {
            println("Phase one FINISHED.")
            phase = AutonomousPhase.two
            loadPhases()
        } else if (isPhaseTwoFinished) {
            println("Phase two FINISHED.")
            phase = AutonomousPhase.three
            loadPhases()
        } else if (isPhaseThreeFinished) {
            println("Phase three FINISHED.")
            phase = AutonomousPhase.done
            println("Autonomous is done")
        }

        Scheduler.getInstance().run()
    }

    private fun loadPhases() {
        if (phase == AutonomousPhase.two) {
            val visionTargetAngle = Robot.vision.liftAngle
            if (visionTargetAngle != null) {
                println("Vision reporting angle " + visionTargetAngle)
                turnToAngle()
                println("Phase two LOADED.")
            } else {
                println("Phase two SKIPPED.")
                phase = AutonomousPhase.three
                loadPhases()
            }
        } else if (phase == AutonomousPhase.three) {
            println("Phase three LOADED.")
            val distance = Robot.getLeddarDistance(7)
            if (distance == null) {
                println("Phase three SKIPPED")
                driveToLift(1.36)
            } else {
                println("Leddar says distance is " + distance)
                driveToLift(distance - RobotMap.DESIRED_DISTANCE_FROM_WALL)
            }
        }
    }

    private fun turnToAngle() {
        if (Robot.vision.liftAngle == 0.0) {
            Scheduler.getInstance().add(PhaseCompleteCommand(AutonomousPhase.twoComplete))
        } else {
            val group = CommandGroup()
            group.addSequential(GyroTurn(0.0, true))
            group.addSequential(WaitCommand(RobotMap.WAIT_TIMES))
            group.addSequential(GyroTurn(0.0, true))
            group.addSequential(WaitCommand(RobotMap.WAIT_TIMES))
            group.addSequential(PhaseCompleteCommand(AutonomousPhase.twoComplete))
            Scheduler.getInstance().add(group)
        }
    }

    private fun driveToLift(distance: Double?) {
        val group = CommandGroup()
        group.addSequential(EncoderDrive(distance!!))
        group.addSequential(WaitCommand(RobotMap.WAIT_TIMES))
        group.addSequential(PhaseCompleteCommand(AutonomousPhase.threeComplete))
        Scheduler.getInstance().add(group)
    }

    fun testDriveCommand(distance: Double) {
        Scheduler.getInstance().add(EncoderDrive(distance))
    }

    fun testTurnAbsCommand(angle: Double) {
        Scheduler.getInstance().add(ZeroPhaseCommand())
        Scheduler.getInstance().add(GyroTurn(angle, true))
    }

    fun testTurnNonCommand(angle: Double) {
        Scheduler.getInstance().add(ZeroPhaseCommand())
        Scheduler.getInstance().add(GyroTurn(angle, false))
    }

    override fun initDefaultCommand() {}
}
