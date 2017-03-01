package frc.team4330.robot;

import java.io.IOException;
import java.util.Map;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4330.robot.commandgroups.AutonomousCommand;
import frc.team4330.robot.commands.VisionTurn;
import frc.team4330.robot.server.ServerTest;
import frc.team4330.robot.subsystems.AutonomousManager;
import frc.team4330.robot.subsystems.RobotDrive;
import frc.team4330.robot.subsystems.RopeClimber;
import frc.team4330.robot.subsystems.ShooterFeed;
import frc.team4330.robot.subsystems.ShooterWheel;
import frc.team4330.robot.subsystems.VisionSystem;
import frc.team4330.sensors.distance.LeddarComms;

/**
 * 
 * 2017 WORKING CODE!!!
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
	public final static LeddarComms leddar = new LeddarComms();
	public final static AHRS gyro = new AHRS(Port.kMXP);

	// Server
	public static boolean serverOn;
	ServerTest server = new ServerTest();

	// Commands
	AutonomousCommand autonomous;


	@Override
	public void robotInit() {		
		oi = new OI();

		serverOn = false;
	}


	@Override
	public void autonomousInit() {		
		Scheduler.getInstance().removeAll();

		gyro.reset();
		myRobot.resetEncoders();
		vision.startUp();
		try {
			leddar.startUp();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		serverOn = true;

		try {
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (DashboardManager.getMethod() == DashboardManager.autoCommands) 
			autonomous = new AutonomousCommand(DashboardManager.getStart());
		else steveBannon.init();

		if (autonomous != null) {
			autonomous.start();
			Scheduler.getInstance().enable();
		}
	}


	@Override
	public void autonomousPeriodic() {
		leddar.retrieveData();
		vision.getLiftAngle();
		
		if (DashboardManager.getMethod() == DashboardManager.autoCommands) 
			Scheduler.getInstance().run();
		else steveBannon.run();
	}

	@Override
	public void teleopInit() {
		vision.startUp();
		try {
			leddar.startUp();
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		serverOn = true;

		Scheduler.getInstance().enable();
	}

	@Override
	public void teleopPeriodic() {
		//		System.out.println("encoders: " + myRobot.getLeftDistance() + ", " + myRobot.getRightDistance());
		//		System.out.println("vision: " + vision.getLiftAngle());
		//		System.out.println("leddar: " + getLeddarDistance(8));

		if (getLeddarDistance(8) != null) SmartDashboard.putNumber("Leddar Distance", getLeddarDistance(8));
		if (vision.getLiftAngle() != null) SmartDashboard.putNumber("Gyro Angle", vision.getLiftAngle());

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

		try {
			leddar.shutDown();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		Map<Integer, Integer> distances = leddar.retrieveData();

		if ( distances.get(segment) != null)
			return distances.get(segment)/ 100. - .18;
		else return null;
	}

	@Override
	public void robotPeriodic() {
		RobotMap.updateVals();
	}
}
