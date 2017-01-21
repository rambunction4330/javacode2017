package frc.team4330.screambunction;

import jaci.openrio.toast.lib.log.Logger;
import jaci.openrio.toast.lib.module.IterativeModule;
import jaci.openrio.toast.lib.registry.Registrar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotModule extends IterativeModule {

	public static Logger logger;
	public Relay spiky;
	SmartDashboard dash;

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
