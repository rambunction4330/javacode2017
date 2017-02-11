package frc.team4330.screambunction;

import java.util.List;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team4330.screambunction.server.ServerTest;
import frc.team4330.screambunction.subsystems.AutonomousManager;
import frc.team4330.screambunction.subsystems.MaxSonar;
import frc.team4330.screambunction.subsystems.RobotDrive;
import frc.team4330.screambunction.subsystems.RopeClimber;
import frc.team4330.screambunction.subsystems.Shooter;
import frc.team4330.screambunction.subsystems.VisionSystem;
import frc.team4330.screambunction.utils.RobotMap;
import frc.team4330.sensors.distance.LeddarDistanceSensor;
import frc.team4330.sensors.distance.LeddarDistanceSensorData;

/**
 * WIP 2017 Code.
 *
 * TODO Test encoders/drive command TODO Work on servers w/ Jeffrey TODO Vision
 * works?
 */
@SuppressWarnings("unused")
public class Robot extends IterativeRobot {

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
		SmartDashboardSetup.allDashboards();
		
		// Initializing Joysticks
		leftj = new Joystick(RobotMap.LEFT_JOYSTICK_PORT);
		rightj = new Joystick(RobotMap.RIGHT_JOYSTICK_PORT);
		buttonj = new Joystick(RobotMap.SHOOT_JOYSTICK_PORT);

		// Initializing Components
		gyro = new AHRS(SerialPort.Port.kMXP);
		leddar = new LeddarDistanceSensor();

	}

	@Override
	public void autonomousInit() {
		// ServerTest server = new ServerTest();
		// try {
		// server.start();
		// } catch (Exception e){
		//
		// }

		SmartDashboardSetup.autonomousDashboard();

		Scheduler.getInstance().removeAll();

		gyro.reset();
		gyro.resetDisplacement();

		vision.startUp();
		leddar.startUp();

		steveBannon.init();
		// steveBannon.testDriveCommand(1);
		// Scheduler.getInstance().enable();
	}

	@Override
	public void autonomousPeriodic() {
		// System.out.println("leddar: " + getDistance(8));

		// System.out.println("x val: " + gyro.getDisplacementX() + "; y val: "
		// + gyro.getDisplacementY());
		steveBannon.run();
		// Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		vision.startUp();
		leddar.startUp();

		SmartDashboardSetup.teleOpDashboard();
	}

	@Override
	public void teleopPeriodic() {

		// steveBannon.testDriveCommand(1);

		// myRobot.curveDrive(leftj, rightj);
		myRobot.tankDrive(leftj, rightj,
				leftj.getRawButton(RobotMap.REVERSE_BUTTON));

		// tarzan.setClimb(buttonj.getRawButton(RobotMap.CLIMB_SLOW_SPEED_BUTTON),
		// buttonj.getRawButton(RobotMap.CLIMB_FAST_SPEED_BUTTON));
		tarzan.testClimb(leftj.getRawButton(11), leftj.getRawButton(12),
				leftj.getRawButton(7));

		bambam.manualShoot(rightj.getRawButton(RobotMap.SHOOT_POWER_ON_BUTTON),
				rightj.getRawButton(RobotMap.SHOOT_POWER_OFF_BUTTON),
				rightj.getRawButton(RobotMap.FEED_POWER_BUTTON));
		

//		bambam.testWheel();
//		
//		if (leftj.getRawButton(4)) bambam.testFeeder();
//		else bambam.stopFeed();
	}

	@Override
	public void testInit() {
	}

	@Override
	public void testPeriodic() {
	}

	@Override
	public void disabledInit() {
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
		if (distances.isEmpty()) {
			return null;
		}
		for (LeddarDistanceSensorData distance : distances) {
			if (distance.getSegmentNumber() == segment) {
				return distance.getDistanceInCentimeters() / 100.0;
			}
		}
		return null;
	}
}
