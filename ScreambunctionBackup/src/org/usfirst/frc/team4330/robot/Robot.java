package org.usfirst.frc.team4330.robot;

import org.usfirst.frc.team4330.robot.subsystems.AutonomousManager;
import org.usfirst.frc.team4330.robot.subsystems.RobotDrive;
import org.usfirst.frc.team4330.robot.subsystems.MaxSonar;
import org.usfirst.frc.team4330.robot.subsystems.RopeClimber;
import org.usfirst.frc.team4330.robot.subsystems.VisionSystem;
import org.usfirst.frc.team4330.sensors.distance.LeddarDistanceSensor;
import org.usfirst.frc.team4330.utils.RobotMap;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * WIP 2017 Code.
 *
 */
public class Robot extends IterativeRobot {

	// Subsystems
	public final static RobotDrive myRobot = new RobotDrive();
	public final static RopeClimber tarzan = new RopeClimber();
	public final static VisionSystem vision = new VisionSystem();
	public final static MaxSonar sonar = new MaxSonar();
	public final static AutonomousManager manager = new AutonomousManager();
	//	public final static Shooter skittyskittybangbang = new Shooter();

	// Joysticks
	private Joystick leftj, rightj, buttonj;

	// Components
	public static LeddarDistanceSensor leddar;
	public static AHRS gyro;

	@Override
	public void robotInit() {
		// Initializing components
		leftj = new Joystick(RobotMap.LEFT_JOYSTICK_PORT);
		rightj = new Joystick(RobotMap.RIGHT_JOYSTICK_PORT);
		buttonj = new Joystick(RobotMap.SHOOT_JOYSTICK_PORT);

		//		//		channel = new AnalogInput(0);
		gyro = new AHRS(SerialPort.Port.kMXP);
		leddar = new LeddarDistanceSensor();

	}

	@Override
	public void autonomousInit() {
		SmartDashboardSetup.autonomousDashboard();

		Scheduler.getInstance().removeAll();

		gyro.reset();
		gyro.resetDisplacement();
		vision.startUp();
		leddar.startUp();

		manager.init();

	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}


	@Override
	public void teleopInit() {
		SmartDashboardSetup.teleOpDashboard();
	}

	@Override
	public void teleopPeriodic() {
		myRobot.tankDrive(leftj, rightj, leftj.getRawButton(RobotMap.REVERSE_BUTTON), true);
		//		tarzan.setClimb(buttonj.getRawButton(RobotMap.CLIMB_SLOW_SPEED_BUTTON),
		//				buttonj.getRawButton(RobotMap.CLIMB_FAST_SPEED_BUTTON));
		tarzan.testClimb(leftj.getRawButton(11), leftj.getRawButton(12), leftj.getRawButton(7));

		System.out.println("sonar distances (m): " + sonar.getDistanceInMeters());

		//		skittyskittybangbang.manualShoot(, buttonOn, feederOn, addPwr, subPwr);
	}


	@Override
	public void testInit() {
		SmartDashboardSetup.testDashboard();

//		vision.startUp();
	}

	@Override
	public void testPeriodic() {
		tarzan.testClimb(leftj.getRawButton(11), leftj.getRawButton(12), leftj.getRawButton(7));
		myRobot.tankDrive(leftj, rightj, leftj.getRawButton(RobotMap.REVERSE_BUTTON), true);

		//		System.out.println(dis.getDistanceInMeters());
		//		manager.testDriveCommand(dis.getDistanceInMeters());
	}

	@Override
	public void disabledInit() {
		vision.shutDown();
		leddar.shutDown();

		myRobot.stop();

		Scheduler.getInstance().removeAll();
		Scheduler.getInstance().disable();
	}
}
