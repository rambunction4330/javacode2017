package frc.team4330.screambunction;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;
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
	public static VisionComms visGears, visBoiler;
	public static AHRS gyro;
	public static Relay ledSwitch;


	// Var
	protected AutonomousPhase phase;

	@Override
	public void robotInit() {

		// Initializing components
		leftj = new Joystick(RobotMap.LEFT_JOYSTICK_PORT);
		rightj = new Joystick(RobotMap.RIGHT_JOYSTICK_PORT);


		channel = new AnalogInput(0);
		visGears = new VisionComms("tegra-ubuntu", 9001);
		visBoiler = new VisionComms("tegra-ubuntu", 9002);
		gyro = new AHRS(SerialPort.Port.kMXP);
		ledSwitch = new Relay(0);
		ledSwitch.setDirection(Direction.kForward);


		System.out.println("\n*********************************");
		System.out.println("*********************************");
		System.out.println("LEFT JOYSTICK (" + leftj.getName() + ") IN PORT " + RobotMap.LEFT_JOYSTICK_PORT);
		System.out.println("RIGHT JOYSTICK (" + rightj.getName() + ") IN PORT " + RobotMap.RIGHT_JOYSTICK_PORT);
		System.out.println("*********************************");
		System.out.println("*********************************" + "\n");

	}

	protected enum AutonomousPhase {
		one, two;
	}

	@Override
	public void autonomousInit() {
		SmartDashboardSetup.autonomousDashboard();
		Scheduler.getInstance().enable();   
		gyro.resetDisplacement();

		phase = AutonomousPhase.one;

		try {
			visGears.startUp();
			visBoiler.startUp();
			ledSwitch.set(Value.kOn);
		} catch (Exception e) {
			System.out.println("********* Error Message *********" + "\n" + e.getMessage());
		} 
	}

	@Override
	public void autonomousPeriodic() {
		System.out.println(visGears.retrieveData());
		
		// TODO Develop phase 1 of autonomous
		if (phase == AutonomousPhase.one) {
			myRobot.tankAuto(RobotMap.FAST_SPEED, RobotMap.FAST_SPEED);

			int position = SmartDashboardSetup.getStart();
			switch (position) {
			case SmartDashboardSetup.one: // left
				break;
			case SmartDashboardSetup.two: // middle
				break;
			case SmartDashboardSetup.three: // right
				break;
			default:
				break;
			}
			
			phase = AutonomousPhase.two;
		} else if (phase == AutonomousPhase.two){

		}
		// TODO Develop phase 2 of autonomous
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
			ledSwitch.set(Value.kOn);
			visGears.startUp();
			visBoiler.startUp();
		} catch (Exception e) {
			System.out.println("********* Error Message *********" + "\n" + e.getMessage());
		}
	}

	@Override
	public void testPeriodic() {
		//		System.out.println(vis.retrieveData());
		//		myRobot.tankTesting(leftj);

		if (leftj.getRawButton(5)) ledSwitch.set(Value.kOn);
		else ledSwitch.set(Value.kOff);
	}

	@Override
	public void disabledInit() {
		Scheduler.getInstance().disable();
		ledSwitch.set(Value.kOff);

		try {
			visGears.shutDown();
			visBoiler.shutDown();
		} catch (Exception e) {
			System.out.println("********* Error Message *********" + "\n" + e.getMessage());
		}
	}
}
