package frc.team4330.screambunction;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4330.screambunction.utils.RobotMap;


public class SmartDashboardSetup {

	//Auto Stuff
	static SendableChooser<Integer> positionChooser;
	public static final int one = 1;
	public static final int two = 2;
	public static final int three = 3;


	public static void testDashboard() {
		System.out.println("\n*****************************************");
		System.out.println("************ TEST INITIATED ************" + "\n");

		allDashboards();
	}

	public static void teleOpDashboard() {
		System.out.println("\n****************************************");
		System.out.println("********** BUTTONS FOR DRIVERS *********");
		System.out.println("\nLEFT joystick controls :" + "\nPRESS "
				+ RobotMap.REVERSE_BUTTON
				+ "to reverse the drive direction." + "\nPRESS "
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

		System.out.println("THIRD joystick controls : " + "\nPRESS "
					+ RobotMap.FEED_POWER_BUTTON
				+ " to turn on the feeder." + "\nPRESS " 
					+ RobotMap.SHOOT_POWER_ON_BUTTON
				+ " to turn on the shooter." + "\nPRESS "
					+ RobotMap.SHOOT_POWER_OFF_BUTTON
				+ " to turn off the shooter." + "\nHOLD "
					+ RobotMap.CLIMB_SLOW_SPEED_BUTTON
				+ " to start reeling in the rope." + "\nHOLD "
					+ RobotMap.CLIMB_FAST_SPEED_BUTTON
				+ " to start climbing the rope." + "\n");
		System.out.println("****************************************");
		System.out.println("****************************************"
				+ "\n");


		allDashboards();
	}

	public static void autonomousDashboard() {

		allDashboards();
	}

	public static void allDashboards() {
		positionChooser = new SendableChooser<Integer>();
		positionChooser.addDefault("Left", one);
		positionChooser.addObject("Middle", two);
		positionChooser.addObject("Right", three);
		SmartDashboard.putData("Autonomous Position", positionChooser);		
	}

	public static int getStart(){
		return (int) positionChooser.getSelected();
	}
}
