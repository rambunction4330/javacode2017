package frc.team4330.screambunction.commands;

import jaci.openrio.toast.lib.registry.Registrar;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Command;


public class Turn extends Command {
	private AHRS gyro;
	private double curHeading, desHeading;
	private boolean finished = false;

	Talon left = Registrar.talon(3);
	Talon right = Registrar.talon(4);


	public Turn(double desHeading, Port port) {
		this.desHeading = desHeading;
		gyro = new AHRS(port);
	}

	/**
	 * 
	 * Used to initialize the command.
	 */
	@Override
	protected void initialize() {
		curHeading = gyro.getAngle();
	}

	@Override
	public void execute() {
		curHeading = gyro.getAngle();

		if (Math.abs(curHeading - desHeading) <= 5) {
			finished = true;
		} else if (desHeading - curHeading > 0) {
			left.set(.5);
			right.set(-.5);
		} else {
			left.set(-.5);
			right.set(.5);
		}
	}

	@Override
	protected boolean isFinished() {
		return finished;
	}

	@Override
	protected void end() {
	}

	public double getLeftMotorSpeed() {
		return left.get();
	}

	public double getRightMotorSpeed() {
		return right.get();
	}

	@Override
	protected void interrupted() {

	}


}
