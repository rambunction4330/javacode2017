package frc.team4330.robot;

import java.nio.ByteBuffer;
import java.util.List;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team4330.robot.subsystems.AutonomousManager;
import frc.team4330.robot.subsystems.MaxSonar;
import frc.team4330.robot.subsystems.RobotDrive;
import frc.team4330.robot.subsystems.RopeClimber;
import frc.team4330.robot.subsystems.Shooter;
import frc.team4330.robot.subsystems.VisionSystem;
import frc.team4330.robot.utils.RobotMap;
import frc.team4330.sensors.distance.LeddarDistanceSensor;
import frc.team4330.sensors.distance.LeddarDistanceSensorData;

/**
 * WIP 2017 Code.
 *
 * TODO Test encoders/drive command 
 * TODO Work on servers w/ Jeffrey 
 * TODO Vision works?
 */
@SuppressWarnings("unused")
public class Robot extends IterativeRobot {
	SPI test = new SPI(edu.wpi.first.wpilibj.SPI.Port.kMXP);
	// Subsystems
	public final static AutonomousManager steveBannon = new AutonomousManager();
	public final static MaxSonar sonar = new MaxSonar();
	public final static RobotDrive myRobot = new RobotDrive();
	public final static RopeClimber tarzan = new RopeClimber();
	public final static Shooter bambam = new Shooter();
	public final static VisionSystem vision = new VisionSystem();

	// Joysticks
	private Joystick leftj, rightj, buttonj;

	// Components
	public static LeddarDistanceSensor leddar;
	public static AHRS gyro;

	@Override
	public void robotInit() {
		RobotMap.updateVals();
		SmartDashboardSetup.allDashboards();

		// Initializing Joysticks
		leftj = new Joystick(RobotMap.LEFT_JOYSTICK_PORT);
		rightj = new Joystick(RobotMap.RIGHT_JOYSTICK_PORT);
		buttonj = new Joystick(RobotMap.SHOOT_JOYSTICK_PORT);

		// Initializing Components
		gyro = new AHRS(Port.kMXP);
		leddar = new LeddarDistanceSensor();
	}

	@Override
	public void autonomousInit() {
		RobotMap.updateVals();
		SmartDashboardSetup.autonomousDashboard();

		// ServerTest server = new ServerTest();
		// try {
		// server.start();
		// } catch (Exception e){
		//
		// }


		Scheduler.getInstance().removeAll();

		gyro.reset();
		gyro.resetDisplacement();
		
		vision.startUp();
		leddar.startUp();
		leddar.setRecording(RobotMap.RECORDING_LEDDAR_VALS);

		//		Scheduler.getInstance().enable();
		//		steveBannon.init();
		// steveBannon.testDriveCommand(1);
	}


	@Override
	public void autonomousPeriodic() {
		System.out.println("leddar: " + getDistance(8));
//		ByteBuffer test2=new ByteBuffer();
//		System.out.println("lidar" + test.read(false, , size))
		// System.out.println("x val: " + gyro.getDisplacementX() + "; y val: "
		// + gyro.getDisplacementY());
		//		steveBannon.run();
		//		 Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		RobotMap.updateVals();
		SmartDashboardSetup.teleOpDashboard();

		vision.startUp();
		leddar.startUp();
	}

	@Override
	public void teleopPeriodic() {
//		System.out.println("leddar: " + leddar.getDistances().toString());
		System.out.println("test: " + RobotMap.SLOW_SPEED);
		// steveBannon.testDriveCommand(1);

		// myRobot.curveDrive(leftj, rightj);
		myRobot.tankDrive(leftj, rightj,
				leftj.getRawButton(RobotMap.REVERSE_BUTTON));

		// tarzan.setClimb(buttonj.getRawButton(RobotMap.CLIMB_SLOW_SPEED_BUTTON),
		// buttonj.getRawButton(RobotMap.CLIMB_FAST_SPEED_BUTTON));
		tarzan.testClimb(leftj.getRawButton(11), leftj.getRawButton(12),
				leftj.getRawButton(7));

		bambam.manualShoot(rightj.getRawButton(RobotMap.SHOOT_POWER_OFF_BUTTON),
				rightj.getRawButton(RobotMap.SHOOT_POWER_ON_BUTTON),
				rightj.getRawButton(RobotMap.FEED_POWER_BUTTON),
				rightj.getRawButton(RobotMap.FEED_POWER_OFF_BUTTON));
	}

	@Override
	public void testInit() {
	}

	@Override
	public void testPeriodic() {
	}

	@Override
	public void disabledInit() {
		RobotMap.updateVals();
		Scheduler.getInstance().removeAll();
		Scheduler.getInstance().disable();

		vision.shutDown();
		leddar.shutDown();

		bambam.stop();
		tarzan.stop();
		myRobot.stop();
	}

	/**
	 * Returns the segment distance from the LEDDAR in segment 0-15. (or null).
	 * 
	 * @param segment
	 * @return distance for that segment or null (in meters.)
	 */
	public final static Double getDistance(int segment) {
		List<LeddarDistanceSensorData> distances = leddar.getDistances();
		
		if (distances.get(8) != null) return distances.get(8).getDistanceInCentimeters() / 100.0;
		else return null;
	}
}
