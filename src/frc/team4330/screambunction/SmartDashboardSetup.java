package frc.team4330.screambunction;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4330.screambunction.commands.DriveForward;
import frc.team4330.screambunction.commands.Turn;


public class SmartDashboardSetup {


	public static void testDashboard() {
		System.out.println("\n*****************************************");
		System.out.println("************ TEST INITIATED ************" + "\n");

		SmartDashboard.putData("Turn Command (default)", new Turn(90.0));
		SmartDashboard.putData("Drive Robot (default)", new DriveForward(1.0));
		allDashboards();
	}

	public static void teleOpDashboard() {
		System.out.println("\n****************************************");
		System.out.println("********** BUTTONS FOR DRIVERS *********");
		System.out.println("\nLEFT joystick controls :" + "\nPRESS "
				//				 + [button here]
				+ "to [action]." + "\nPRESS "
				//				 + [button here]
				+ "to [action]." + "\nPRESS "
				//				 + [button here]
				+ " to [action]." + "\n");

		System.out.println("RIGHT joystick controls : " + "\nPRESS "
				//				+ [button here]
				+ " to [action]." + "\nPRESS " 
				//				+ [button here]
				+ " to [action]." + "\nPRESS "
				//				+ [button here] 
				+ " to [action]." + "\n");
		System.out.println("****************************************");
		System.out.println("****************************************"
				+ "\n");


		allDashboards();
	}

	public static void autonomousDashboard() {

		allDashboards();
	}

	private static void allDashboards() {

	}
}
