package frc.team4330.robot;

import java.io.IOException;
import java.util.List;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team4330.robot.commandgroups.AutonomousCommand;
import frc.team4330.robot.commands.VisionTurn;
import frc.team4330.robot.server.ServerTest;
import frc.team4330.robot.subsystems.AutonomousManager;
import frc.team4330.robot.subsystems.RobotDrive;
import frc.team4330.robot.subsystems.RopeClimber;
import frc.team4330.robot.subsystems.ShooterFeed;
import frc.team4330.robot.subsystems.ShooterWheel;
import frc.team4330.robot.subsystems.VisionSystem;
import frc.team4330.sensors.distance.LeddarDistanceSensor;
import frc.team4330.sensors.distance.LeddarDistanceSensorData;

/**
 * WIP 2017 Code.
 * 
 */
public class Robot extends IterativeRobot {
	
	//OI
	public static OI oi;

	// Subsystems
	public final static AutonomousManager steveBannon = new AutonomousManager();
	public final static RobotDrive myRobot = new RobotDrive();
	public final static RopeClimber tarzan = new RopeClimber();
	public final static ShooterWheel bam = new ShooterWheel();
	public final static ShooterFeed bamm = new ShooterFeed();
	public final static VisionSystem vision = new VisionSystem();
	
	// Joysticks
	public final static Joystick leftj = new Joystick(RobotMap.LEFT_JOYSTICK_PORT),
			rightj = new Joystick(RobotMap.RIGHT_JOYSTICK_PORT),
			buttonj = new Joystick(RobotMap.SHOOT_JOYSTICK_PORT);

	// Components
	public final static LeddarDistanceSensor leddar = new LeddarDistanceSensor();
	public final static AHRS gyro = new AHRS(Port.kMXP);

	// Server
	public static boolean serverOn;
	ServerTest server = new ServerTest();
	
	// Commands
	AutonomousCommand autonomous;
	

	@Override
	public void robotInit() {
		SmartDashboardSetup.allDashboards();
		
		oi = new OI();

		serverOn = false;
	}


	@Override
	public void autonomousInit() {		
		SmartDashboardSetup.autonomousDashboard();

		Scheduler.getInstance().removeAll();
		gyro.reset();
		myRobot.resetEncoders();
		vision.startUp();
//		leddar.startUp();
//		leddar.setRecording(RobotMap.RECORDING_LEDDAR_VALS);

		serverOn = true;
		
		try {
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// TODO Uncomment for single group auto.
//		autonomous = new AutonomousCommand(SmartDashboardSetup.getStart());
		
		if (autonomous != null)
			Scheduler.getInstance().add(autonomous);
//		Scheduler.getInstance().enable();
		
		// TODO Uncomment for managed auto.
		steveBannon.init();
	}
	

	@Override
	public void autonomousPeriodic() {
//		System.out.println("leddar: " + getLeddarDistance(8));
		
		// TODO Uncomment if using single command auto.
//		Scheduler.getInstance().run();
		
		// TODO Uncomment if using managed auto.
		vision.getLiftAngle();
		steveBannon.run();
	}

	@Override
	public void teleopInit() {
		SmartDashboardSetup.teleOpDashboard();
		
		vision.startUp();
//		leddar.startUp();
//		leddar.setRecording(RobotMap.RECORDING_LEDDAR_VALS);
		
		serverOn = true;
		
		Scheduler.getInstance().enable();
	}
	
	@Override
	public void teleopPeriodic() {
		System.out.println("vision: " + vision.getLiftAngle());
//		System.out.println("leddar: " + leddar.getDistances().toString());
		
		Scheduler.getInstance().run();
		
		myRobot.tankDrive(leftj, rightj,
				leftj.getRawButton(RobotMap.REVERSE_BUTTON));

		tarzan.testClimb(buttonj.getRawButton(RobotMap.CLIMB_SLOW_SPEED_BUTTON), 
				buttonj.getRawButton(RobotMap.CLIMB_FAST_SPEED_BUTTON),
				buttonj.getRawButton(7));

		bam.teleShoot(buttonj.getRawButton(RobotMap.SHOOT_POWER_OFF_BUTTON),
				buttonj.getRawButton(RobotMap.SHOOT_POWER_ON_BUTTON));
		
		bamm.teleShoot(buttonj.getRawButton(RobotMap.FEED_POWER_OFF_BUTTON),
				buttonj.getRawButton(RobotMap.FEED_POWER_BUTTON));
	}	
	
	@Override
	public void testInit() {
		vision.startUp();
		
		Scheduler.getInstance().add(new VisionTurn());
		Scheduler.getInstance().enable();
	}

	@Override
	public void testPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void disabledInit() {		
		try {
			server.stop();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (autonomous != null)
			autonomous.cancel();
		
		Scheduler.getInstance().removeAll();
		Scheduler.getInstance().disable();
		
		leddar.shutDown();
		vision.shutDown();
		bam.stop();
		bamm.stop();
		tarzan.stop();
		myRobot.stop();

		serverOn = false;
	}

	/**
	 * Returns the segment distance from the LEDDAR in segment 0-15. (or null).
	 * 
	 * @param segment
	 * @return distance for that segment or null (in meters.)
	 */
	public final static Double getLeddarDistance(int segment) {
		List<LeddarDistanceSensorData> distances = leddar.getDistances();

		if (distances.get(segment) != null) {
			return distances.get(segment).getDistanceInCentimeters() / 100. - .18;
		}
		else return null;
	}

	@Override
	public void robotPeriodic() {
		RobotMap.updateVals();
	}
}
