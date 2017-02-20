package frc.team4330.robot.commands;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import frc.team4330.robot.Robot;

public class VisionTurn extends Command {	
	PIDSource scr = new PIDSource() {
		
		@Override
		public void setPIDSourceType(PIDSourceType pidSource) {
			pidSource = PIDSourceType.kDisplacement;
		}
		
		@Override
		public double pidGet() {
			if (Robot.vision.getLiftAngle() == null) return 0;
			else return Robot.vision.getLiftAngle();
		}
		
		@Override
		public PIDSourceType getPIDSourceType() {
			return PIDSourceType.kDisplacement;
		}
	};
	PIDOutput out = new PIDOutput() {
		
		@Override
		public void pidWrite(double output) {
			System.out.println("output: " + output);
			Robot.myRobot.automatedDrive(output, -output);
		}
	};
	PIDController pid = new PIDController(.8, 0, 8, scr, out, .005);
	
	public VisionTurn() {
		System.out.println("Starting a new " + this.getName() + " command.");
//		requires(Robot.myRobot);
	}
	
	@Override
	protected void initialize() {
		pid.setOutputRange(-.4, .4);
		pid.setSetpoint(0);
		pid.enable();
		
		System.out.println("Initializing " + this.getName() + " command.");
	}
	
	@Override
	protected boolean isFinished() {
		if (Robot.vision.getLiftAngle() != null) {
			if (Math.abs(Robot.vision.getLiftAngle()) <= 1) {
				System.out.println(this.getName() + " command is finished.");
				return true;
			} else return false;
		} else return false;
	}
	
	@Override
	protected void end() {
		pid.disable();
		System.out.println("Ending " + this.getName() + " command.");
		Robot.myRobot.stop();
	}

}
