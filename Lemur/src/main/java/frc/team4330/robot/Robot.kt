package frc.team4330.robot

import java.io.IOException

import com.kauailabs.navx.frc.AHRS

import edu.wpi.first.wpilibj.IterativeRobot
import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.SerialPort.Port
import edu.wpi.first.wpilibj.command.Scheduler
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team4330.robot.commandgroups.AutonomousCommand
import frc.team4330.robot.commands.VisionTurn
import frc.team4330.robot.subsystems.AutonomousManager
import frc.team4330.robot.subsystems.RobotDrive
import frc.team4330.robot.subsystems.RopeClimber
import frc.team4330.robot.subsystems.ShooterFeed
import frc.team4330.robot.subsystems.ShooterWheel
import frc.team4330.robot.subsystems.VisionSystem
import frc.team4330.sensors.distance.LeddarComms
import edu.wpi.first.wpilibj.CameraServer
import com.ctre.CANTalon

/**
 *
 * 2017 WORKING CODE!!!
 *
 */
class Robot : IterativeRobot() {

    // Commands
    internal var autonomous: AutonomousCommand? = null


    override fun robotInit() {
        oi = OI()
        CameraServer.getInstance().startAutomaticCapture()
        DashboardManager.start()
    }


    override fun autonomousInit() {
        Scheduler.getInstance().removeAll()

        gyro.reset()
        myRobot.resetEncoders()
        vision.startUp()
        try {
            leddar.startUp()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        if (DashboardManager.method === DashboardManager.autoCommands)
            autonomous = AutonomousCommand(DashboardManager.start)
        else
            steveBannon.init()

        if (autonomous != null) {
            autonomous!!.start()
            Scheduler.getInstance().enable()
        }

    }


    override fun autonomousPeriodic() {
        leddar.retrieveData()
        vision.liftAngle

        if (DashboardManager.method === DashboardManager.autoCommands)
            Scheduler.getInstance().run()
        else
            steveBannon.run()
    }

    override fun teleopInit() {
        vision.startUp()
        try {
            leddar.startUp()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        Scheduler.getInstance().enable()
    }

    override fun teleopPeriodic() {
        //		System.out.println("encoders: " + myRobot.getLeftDistance() + ", " + myRobot.getRightDistance());
        //		System.out.println("vision: " + vision.getLiftAngle());
        //		System.out.println("leddar: " + getLeddarDistance(8));

        if (getLeddarDistance(7) != null) SmartDashboard.putNumber("Leddar Distance Seg 7", getLeddarDistance(7)!!)
        if (getLeddarDistance(8) != null) SmartDashboard.putNumber("Leddar Distance Seg 8", getLeddarDistance(8)!!)
        if (getLeddarDistance(9) != null) SmartDashboard.putNumber("Leddar Distance Seg 9", getLeddarDistance(9)!!)
        //		if (vision.getLiftAngle() != null) SmartDashboard.putNumber("Gyro Angle", vision.getLiftAngle());

        Scheduler.getInstance().run()

        myRobot.tankDrive(leftj, rightj,
                leftj.getRawButton(RobotMap.REVERSE_BUTTON))
    }

    override fun testInit() {
        vision.startUp()

        Scheduler.getInstance().add(VisionTurn())
        Scheduler.getInstance().enable()
    }

    override fun testPeriodic() {
        Scheduler.getInstance().run()
    }

    override fun disabledInit() {

        if (autonomous != null)
            autonomous!!.cancel()

        Scheduler.getInstance().removeAll()
        Scheduler.getInstance().disable()

        try {
            leddar.shutDown()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        vision.shutDown()
        bam.stop()
        bamm.stop()
        tarzan.stop()
        myRobot.stop()

    }

    override fun robotPeriodic() {
        RobotMap.updateVals()
    }

    companion object {

        //OI
        var oi: OI? = null

        // Subsystems
        val steveBannon = AutonomousManager()
        val myRobot = RobotDrive()
        val tarzan = RopeClimber()
        val bam = ShooterWheel()
        val bamm = ShooterFeed()
        val vision = VisionSystem()

        // Joysticks
        val leftj = Joystick(RobotMap.LEFT_JOYSTICK_PORT)
        val rightj = Joystick(RobotMap.RIGHT_JOYSTICK_PORT)
        val buttonj = Joystick(RobotMap.SHOOT_JOYSTICK_PORT)

        // Components
        val leddar = LeddarComms()
        val gyro = AHRS(Port.kMXP)

        /**
         * Returns the segment distance from the LEDDAR in segment 0-15. (or null).
         *
         * @param segment
         * @return distance for that segment or null (in meters.)
         */
        fun getLeddarDistance(segment: Int): Double? {
            val distances = leddar.retrieveData()

            return if (distances[segment] != null)
                distances[segment]?.div(100.0)?.minus(.18)
            else
                null
        }
    }
}
