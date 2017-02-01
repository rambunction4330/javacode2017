package frc.team4330.screambunction;

import java.util.Map;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.team4330.screambunction.commands.Turn;
import frc.team4330.screambunction.subsystems.AutonomousManager;
import frc.team4330.screambunction.subsystems.RobotDrive;
import frc.team4330.screambunction.subsystems.RopeClimber;
import frc.team4330.screambunction.subsystems.VisionSystem;
import frc.team4330.screambunction.utils.RobotMap;

/**
 * WIP 2017 Code.
 *
 */
public class Robot extends IterativeRobot {

	// Subsystems
	public final static RobotDrive myRobot = new RobotDrive();
	public final static RopeClimber tarzan = new RopeClimber();
//	public final static VisionSystem vision = new VisionSystem();
	public final static AutonomousManager manager = new AutonomousManager();
//	public final static Shooter skittyskittybangbang = new Shooter();

	// Joysticks
	private Joystick leftj, rightj, buttonj;

	// Components
	public static AnalogInput channel;
	public static AHRS gyro;

	// Variables
	protected AutonomousPhase phase;

	@Override
	public void robotInit() {

		// Initializing components
		leftj = new Joystick(RobotMap.LEFT_JOYSTICK_PORT);
		rightj = new Joystick(RobotMap.RIGHT_JOYSTICK_PORT);
		buttonj = new Joystick(RobotMap.SHOOT_JOYSTICK_PORT);

		//		channel = new AnalogInput(0);
		gyro = new AHRS(SerialPort.Port.kMXP);


		System.out.println("\n*********************************");
		System.out.println("*********************************");
		System.out.println("LEFT JOYSTICK (" + leftj.getName() + ") IN PORT " + RobotMap.LEFT_JOYSTICK_PORT);
		System.out.println("RIGHT JOYSTICK (" + rightj.getName() + ") IN PORT " + RobotMap.RIGHT_JOYSTICK_PORT);
		System.out.println("SHOOTER JOYSTICK (" + buttonj.getName() + ") IN PORT " + RobotMap.SHOOT_JOYSTICK_PORT);
		System.out.println("*********************************");
		System.out.println("*********************************" + "\n");

	}

	protected enum AutonomousPhase {
		one, two, three, done;
	}

	@Override
	public void autonomousInit() {
		SmartDashboardSetup.autonomousDashboard();

		Scheduler.getInstance().enable();   

		gyro.resetDisplacement();
//		vision.startUp();

		phase = AutonomousPhase.one;

	}

	@Override
	public void autonomousPeriodic() {
		//		System.out.println(visGears.retrieveData());

		// TODO Develop phase 1 of autonomous
		if (phase == AutonomousPhase.one) {			
			if (SmartDashboardSetup.getStart() == SmartDashboardSetup.one) { //LEFT
				manager.travelToLeftLift();
			} else if (SmartDashboardSetup.getStart() == SmartDashboardSetup.two) { //CENTER
				manager.travelToCenterLift();
			} else { //RIGHT
				manager.travelToRightLift();
			}

			phase = AutonomousPhase.two;

			// TODO Develop phase 2 of autonomous		
		} else if (phase == AutonomousPhase.two){
//			Map<String, String> vals = vision.getLiftData();
//			double angle = Integer.parseInt(vals.get("rb"));
//			System.out.println(angle + "");
//
//			Scheduler.getInstance().add(new Turn(angle));

			phase = AutonomousPhase.three;
			// TODO Develop phase 3 of autonomous		
		} else if (phase == AutonomousPhase.three) {

			phase = AutonomousPhase.done;
		} else { }
	}

	@Override
	public void teleopInit() {
//		SmartDashboardSetup.teleOpDashboard();

//		gyro.reset();
//		gyro.resetDisplacement();
	}

	@Override
	public void teleopPeriodic() {
		myRobot.tankDrive(leftj, rightj);
		tarzan.setClimb(buttonj.getRawButton(RobotMap.CLIMB_SLOW_SPEED_BUTTON),
				buttonj.getRawButton(RobotMap.CLIMB_FAST_SPEED_BUTTON), false);
		
//		skittyskittybangbang.manualShoot(, buttonOn, feederOn, addPwr, subPwr);
	}


	@Override
	public void testInit() {
//		SmartDashboardSetup.testDashboard();

//		vision.startUp();
	}

	@Override
	public void testPeriodic() {
		tarzan.setClimb(leftj.getRawButton(11), leftj.getRawButton(12), leftj.getRawButton(7));
		myRobot.tankDrive(leftj, rightj);
	}

	@Override
	public void disabledInit() {
//		vision.shutDown();

		Scheduler.getInstance().disable();
	}
}
