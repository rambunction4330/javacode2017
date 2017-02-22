package frc.team4330.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class SmartDashboardSetup {

	//Auto Stuff


	public static final Integer left = 1;
	public static final Integer middle = 2;
	public static final Integer right = 3;
	static SendableChooser<Integer> positionChooser = new SendableChooser<Integer>();
	static {
		positionChooser.addDefault("Middle", middle);
		positionChooser.addObject("Left", left);
		positionChooser.addObject("Right", right);
		SmartDashboard.putData("Autonomous Position", positionChooser);	
	}

	public static void testDashboard() {
		System.out.println("\n*****************************************");
		System.out.println("************ TEST INITIATED ************" + "\n");

	}

	public static void teleOpDashboard() {
		System.out.println("\n****************************************");
		System.out.println("********** BUTTONS FOR DRIVERS *********");
		System.out.println("\nLEFT joystick controls :" + "\nPRESS "
				+ RobotMap.REVERSE_BUTTON
				+ "to reverse the drive direction." + "\n");

		System.out.println("\nTHIRD joystick controls : " + "\nPRESS "
					+ RobotMap.FEED_POWER_BUTTON
				+ " to turn on the agitator." + "\nPRESS " 
				+ RobotMap.FEED_POWER_OFF_BUTTON
			+ " to turn off the agitator." + "\nPRESS " 
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


	}

	public static void autonomousDashboard() {

	}

	public static int getStart(){
		return positionChooser.getSelected();
	}
}
