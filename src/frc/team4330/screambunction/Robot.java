package frc.team4330.screambunction;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SerialPort;
import frc.team4330.screambunction.subsystems.RobotDrive;
import frc.team4330.screambunction.vision.VisionComms;

public class Robot extends IterativeRobot {

	public final static RobotDrive myRobot = new RobotDrive();

	private Joystick leftj, rightj;

	AnalogInput channel;
	VisionComms vis;

	public static AHRS gyro = new AHRS(SerialPort.Port.kMXP);

	@Override
	public void robotInit() {
		leftj = new Joystick(RobotMap.LEFT_JOYSTICK_PORT);
		rightj = new Joystick(RobotMap.RIGHT_JOYSTICK_PORT);
		channel = new AnalogInput(0);
		vis = new VisionComms();
	}

	@Override
	public void autonomousInit() {
		SmartDashboardSetup.autonomousDashboard();
	}

	@Override
	public void autonomousPeriodic() {

	}

	@Override
	public void teleopInit() {
		gyro.reset();

	}

	@Override
	public void teleopPeriodic() {
		//				System.out.println("NavX angle output: " + gyro.getAngle());
		System.out.println("NavX displacement output: " + gyro.getVelocityX());
		//		kms
		myRobot.tankDrive(leftj, rightj);
	}


	@Override
	public void testInit() {
		try {
			vis.startUp();
		} catch (Exception e) {
			System.out.println("********* Error Message *********" + "\n" + e.getMessage());
		}
	}

	@Override
	public void testPeriodic() {
		System.out.println(vis.retrieveData());
	}

	@Override
	public void disabledInit() {
		try {
			vis.shutDown();
		} catch (Exception e) {
			System.out.println("********* Error Message *********" + "\n" + e.getMessage());
		}
	}
}
