package frc.team4330.screambunction;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

	public Relay spiky;
	SmartDashboard dash;
	RobotDrive myRobot;

	private Joystick leftj, rightj;


	@Override
	public void robotInit() {
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
