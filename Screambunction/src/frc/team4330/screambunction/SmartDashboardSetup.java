package frc.team4330.screambunction;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4330.screambunction.commands.DriveForward;
import frc.team4330.screambunction.commands.Turn;


public class SmartDashboardSetup {
	
	//Auto Stuff
		static SendableChooser positionChooser;
		public static final int one = 1;
		public static final int two = 2;
		public static final int three = 3;

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

	public static void allDashboards() {

		SmartDashboardSetup.allDashboards();
		positionChooser = new SendableChooser();
		positionChooser.addDefault("Left", one);
		positionChooser.addObject("Middle", two);
		positionChooser.addObject("Right", three);
		SmartDashboard.putData("Autonomous Position", positionChooser);		
	}
	
	public static int getStart(){
		return (int)positionChooser.getSelected();
	}
}
