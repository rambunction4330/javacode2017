package frc.team4330.screambunction;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4330.screambunction.utils.Registrar;

public class Robot extends IterativeRobot {

	public Relay spiky;
	SmartDashboard dash;


	@Override
	public void robotInit() {
		//TODO: Module Init
		spiky = Registrar.relay(0);
		spiky.setDirection(Direction.kForward);
		dash = new SmartDashboard();
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
		spiky.set(Value.kOn);
	}
}
