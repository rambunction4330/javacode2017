package frc.team4330.screambunction;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4330.screambunction.commands.DriveForward;
import frc.team4330.screambunction.commands.Turn;


public class SmartDashboardSetup {
	
	
	public static void testDashboard() {
		SmartDashboard.putData("Turn Command (default)", new Turn(90.0));
		SmartDashboard.putData("Drive Robot (default)", new DriveForward(5.0));
		allDashboards();
	}
	
	public static void teleOpDashboard() {
		
		allDashboards();
	}
	
	public static void autonomousDashboard() {
		
		allDashboards();
	}
	
	private static void allDashboards() {
		
	}
}
