package frc.team4330.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class DashboardManager {

	public static final Integer left = 1;
	public static final Integer middle = 2;
	public static final Integer right = 3;
	
	public static final String autoManager = "!";
	public static final String autoCommands = "?";
	
	private static SendableChooser<Integer> positionChooser = new SendableChooser<Integer>();
	static {
		positionChooser.addDefault("Middle", middle);
		positionChooser.addObject("Left", left);
		positionChooser.addObject("Right", right);
		SmartDashboard.putData("Autonomous Position", positionChooser);	
	}
	
	private static SendableChooser<String> autoChooser = new SendableChooser<>();
	static {
		autoChooser.addDefault("Use AutoManager", autoManager);
		autoChooser.addObject("Use commands", autoCommands);
		SmartDashboard.putData("Autonomous Method", autoChooser);
	}
	
	public static void start() {
		SmartDashboard.putData("Autonomous Position", positionChooser);	
		SmartDashboard.putData("Autonomous Method", autoChooser);
	}

	public static String getMethod() {
		return autoChooser.getSelected();
	}
	
	public static int getStart(){
		return positionChooser.getSelected();
	}
}
