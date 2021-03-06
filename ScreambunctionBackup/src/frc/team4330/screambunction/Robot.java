package frc.team4330.screambunction;

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

/**
 * WIP 2017 Code.
 *
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
		// Initializing Joysticks
		leftj = new Joystick(RobotMap.LEFT_JOYSTICK_PORT);
		rightj = new Joystick(RobotMap.RIGHT_JOYSTICK_PORT);
		buttonj = new Joystick(RobotMap.SHOOT_JOYSTICK_PORT);

		// Initializing Components
		//			channel = new AnalogInput(0);
		gyro = new AHRS(SerialPort.Port.kMXP);
		leddar = new LeddarDistanceSensor();

	}

	@Override
	public void autonomousInit() {
//		ServerTest server = new ServerTest();
//		try {
//			server.start();
//		} catch (Exception e){
//			
//		}
		
		SmartDashboardSetup.autonomousDashboard();

		Scheduler.getInstance().removeAll();
		
		gyro.resetDisplacement();
		
		vision.startUp();
		leddar.startUp();

//		steveBannon.init();
		steveBannon.testDriveCommand(1);
		Scheduler.getInstance().enable();
	}

	@Override
	public void autonomousPeriodic() {
//		if (leddar.getDistances().size() != 0) 
//			System.out.println("leddar: " + leddar.getDistances().get(0).getDistanceInCentimeters());
//		else System.out.println("No leddar.");

//		System.out.println("x val: " + gyro.getDisplacementX() + "; y val: " + gyro.getDisplacementY());
//		steveBannon.run();
		Scheduler.getInstance().run();
	}



	@Override
	public void teleopInit() {
		vision.startUp();
		leddar.startUp();
		
		SmartDashboardSetup.teleOpDashboard();
	}

	@Override
	public void teleopPeriodic() {
		
//		steveBannon.testDriveCommand(1);
		
//		myRobot.curveDrive(leftj, rightj);
		myRobot.tankDrive(leftj, rightj, leftj.getRawButton(RobotMap.REVERSE_BUTTON));
		
//		tarzan.setClimb(buttonj.getRawButton(RobotMap.CLIMB_SLOW_SPEED_BUTTON),
//				buttonj.getRawButton(RobotMap.CLIMB_FAST_SPEED_BUTTON));
//		tarzan.testClimb(leftj.getRawButton(11), leftj.getRawButton(12), leftj.getRawButton(7));
		
//		bambam.manualShoot(rightj.getRawButton(RobotMap.SHOOT_POWER_ON_BUTTON),
//				rightj.getRawButton(RobotMap.SHOOT_POWER_OFF_BUTTON),
//				rightj.getRawButton(RobotMap.FEED_POWER_BUTTON));
//		
//		if (rightj.getRawButton(3)) bambam.testWheel();
//		else if (rightj.getRawButton(2)) bambam.testFeeder();
//		else if (rightj.getRawButton(5)) bambam.stop();
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
}
