package frc.team4330.robot;

import java.io.IOException;
import java.util.List;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4330.robot.commandgroups.AutonomousCommand;
import frc.team4330.robot.commands.LeddarDrive;
import frc.team4330.robot.commands.VisionTurn;
import frc.team4330.robot.server.ServerTest;
import frc.team4330.robot.subsystems.AutonomousManager;
import frc.team4330.robot.subsystems.RobotDrive;
import frc.team4330.robot.subsystems.RopeClimber;
import frc.team4330.robot.subsystems.Shooter;
import frc.team4330.robot.subsystems.VisionSystem;
import frc.team4330.sensors.distance.LeddarDistanceSensor;
import frc.team4330.sensors.distance.LeddarDistanceSensorData;

/**
 * WIP 2017 Code.
 *
 * TODO Work on servers w/ Jeffrey 
 * TODO look at example code for navx (on starting)
 * TODO test new visionturn command
 */
public class Robot extends IterativeRobot {
	//OI
	public static OI oi;

	// Subsystems
	public final static AutonomousManager steveBannon = new AutonomousManager();
	public final static RobotDrive myRobot = new RobotDrive();
	public final static RopeClimber tarzan = new RopeClimber();
	public final static Shooter bambam = new Shooter();
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
		
//		oi = new OI();

		serverOn = false;
	}


	@Override
	public void autonomousInit() {		
		SmartDashboardSetup.autonomousDashboard();

		Scheduler.getInstance().removeAll();

		gyro.reset();
		myRobot.resetEncoders();
		vision.startUp();
		leddar.startUp();
		leddar.setRecording(RobotMap.RECORDING_LEDDAR_VALS);

		serverOn = true;
		
//		try {
//			server.start();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		autonomous = new AutonomousCommand(SmartDashboardSetup.getStart());
//		
//		if (autonomous != null)
//			Scheduler.getInstance().add(autonomous);
		
//		Scheduler.getInstance().add(new VisionTurn());
//		Scheduler.getInstance().enable();
		
		steveBannon.init();
	}
	

	@Override
	public void autonomousPeriodic() {
		vision.getLiftAngle();
//		System.out.println("leddar: " + getLeddarDistance(8));
//		Scheduler.getInstance().run();
		steveBannon.run();
	}

	@Override
	public void teleopInit() {
		SmartDashboardSetup.teleOpDashboard();
		
		vision.startUp();
		leddar.startUp();
		leddar.setRecording(RobotMap.RECORDING_LEDDAR_VALS);
		
		serverOn = true;
		
		try {
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void teleopPeriodic() {
		System.out.println("vision: " + vision.getLiftAngle());
//		System.out.println("leddar: " + leddar.getDistances().toString());
//		Scheduler.getInstance().run();
		
		myRobot.tankDrive(leftj, rightj,
				leftj.getRawButton(RobotMap.REVERSE_BUTTON));

		tarzan.testClimb(leftj.getRawButton(RobotMap.CLIMB_SLOW_SPEED_BUTTON), 
				leftj.getRawButton(RobotMap.CLIMB_FAST_SPEED_BUTTON),
				leftj.getRawButton(RobotMap.CLIMB_REVERSE_BUTTON));

		bambam.manualShoot(rightj.getRawButton(RobotMap.SHOOT_POWER_OFF_BUTTON),
				rightj.getRawButton(RobotMap.SHOOT_POWER_ON_BUTTON),
				rightj.getRawButton(RobotMap.FEED_POWER_BUTTON),
				rightj.getRawButton(RobotMap.FEED_POWER_OFF_BUTTON));
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (autonomous != null)
			autonomous.cancel();
		
		Scheduler.getInstance().removeAll();
		Scheduler.getInstance().disable();
		
		leddar.shutDown();
		vision.shutDown();
		bambam.stop();
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
