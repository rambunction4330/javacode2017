package frc.team4330.screambunction;

import org.usfirst.frc.team4330.robot.RobotDrive;
import org.usfirst.frc.team4330.robot.RobotMap;

import jaci.openrio.toast.lib.log.Logger;
import jaci.openrio.toast.lib.module.IterativeModule;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotModule extends IterativeModule {

	public static Logger logger;
	SmartDashboard dash;
	RobotDrive myRobot;

	private Joystick leftj, rightj;

	@Override
	public String getModuleName() {
		return "Screambunction";
	}

	@Override
	public String getModuleVersion() {
		return "0.0.1";
	}

	@Override
	public void robotInit() {
		logger = new Logger("Screambunction", Logger.ATTR_DEFAULT);
		//TODO: Module Init
		dash = new SmartDashboard();
		myRobot = new RobotDrive();
		leftj = new Joystick(RobotMap.LEFT_JOYSTICK_PORT);
		rightj = new Joystick(RobotMap.RIGHT_JOYSTICK_PORT);
	}

	@Override
	public void autonomousInit() {

	}

	@Override
	public void autonomousPeriodic() {

	}

	@Override
	public void teleopInit() {

	}

	@Override
	public void teleopPeriodic() {
		myRobot.tankDrive(leftj, rightj);
	}
}
