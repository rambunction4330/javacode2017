package frc.team4330.screambunction;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

	public Relay spiky;
	public SmartDashboard dash;
	public final static RobotDrive myRobot = new RobotDrive();

	private Joystick leftj, rightj;
	
	AnalogInput channel = new AnalogInput(0);
	
	public static AHRS gyro = new AHRS(SerialPort.Port.kMXP);

	@Override
	public void robotInit() {
		dash = new SmartDashboard();
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
		gyro.reset();
	}

	@Override
	public void teleopPeriodic() {
//		System.out.println("NavX angle output: " + gyro.getAngle());
		System.out.println("NavX displacement output: " + gyro.getVelocityX());
//kms
		myRobot.tankDrive(leftj, rightj);
	}
	
	@Override
	public void testPeriodic() {
	}
}
