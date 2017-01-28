package frc.team4330.screambunction;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team4330.screambunction.vision.VisionComms;

/**
 * WIP 2017 Code.
 *
 */
public class Robot extends IterativeRobot {

	// Subsystems
	public final static RobotDrive myRobot = new RobotDrive();

	// Joysticks
	private Joystick leftj;
	private Joystick rightj ;

	// Components
	public static AnalogInput channel;
	public static VisionComms vis;
	public static AHRS gyro;

	@Override
	public void robotInit() {

		// Initializing components
		leftj = new Joystick(RobotMap.LEFT_JOYSTICK_PORT);
		rightj = new Joystick(RobotMap.RIGHT_JOYSTICK_PORT);
		
		
		channel = new AnalogInput(0);
		vis = new VisionComms();  
		gyro = new AHRS(SerialPort.Port.kMXP);


		System.out.println("\n*********************************");
		System.out.println("*********************************");
		System.out.println("LEFT JOYSTICK IN PORT " + RobotMap.LEFT_JOYSTICK_PORT);
		System.out.println("RIGHT JOYSTICK IN PORT " + RobotMap.RIGHT_JOYSTICK_PORT);
		System.out.println("*********************************");
		System.out.println("*********************************" + "\n");
	}

	@Override
	public void autonomousInit() {
		SmartDashboardSetup.autonomousDashboard();
		Scheduler.getInstance().enable();   
		gyro.resetDisplacement();

		try {
			vis.startUp();
		} catch (Exception e) {
			System.out.println("********* Error Message *********" + "\n" + e.getMessage());
		} 
	}

	@Override
	public void autonomousPeriodic() {
		// TODO Develop phase 1 of autonomous
	}

	@Override
	public void teleopInit() {
		SmartDashboardSetup.teleOpDashboard();

		gyro.reset();
		gyro.resetDisplacement();
	}

	@Override
	public void teleopPeriodic() {
		//		System.out.println("NavX angle output: " + gyro.getAngle());
//		System.out.println("NavX displacement output: " + gyro.getVelocityX());
		myRobot.tankDrive(leftj, rightj);
	}


	@Override
	public void testInit() {
		SmartDashboardSetup.testDashboard();
		
		try {
			vis.startUp();
		} catch (Exception e) {
			System.out.println("********* Error Message *********" + "\n" + e.getMessage());
		}
	}

	@Override
	public void testPeriodic() {
		//		System.out.println(vis.retrieveData());

		myRobot.tankTesting(leftj);
	}

	@Override
	public void disabledInit() {
		Scheduler.getInstance().disable();
		
		
		try {
			vis.shutDown();
		} catch (Exception e) {
			System.out.println("********* Error Message *********" + "\n" + e.getMessage());
		}
	}
}
