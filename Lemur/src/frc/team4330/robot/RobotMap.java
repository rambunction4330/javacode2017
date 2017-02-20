package frc.team4330.robot;

import edu.wpi.first.wpilibj.Preferences;
import frc.team4330.robot.utils.HeadingCalculator;

public class RobotMap {

	// Motor Ports
	public final static int MOTOR_ONE_PORT = 0;
	public final static int MOTOR_TWO_PORT = 1;
	public final static int MOTOR_THREE_PORT = 2;
	public final static int MOTOR_FOUR_PORT = 3;
	public final static int MOTOR_CLIMB_PORT = 4;
	public final static int MOTOR_SHOOT_PORT = 5;
	public final static int MOTOR_FEED_PORT = 6;
	
	// DIO Ports
	public final static int ENCODER_LEFT_ONE_PORT = 0;
	public final static int ENCODER_LEFT_TWO_PORT = 1;
	public final static int ENCODER_RIGHT_ONE_PORT = 2;
	public final static int ENCODER_RIGHT_TWO_PORT = 3;
	public final static int MAXSONAR_PORT = 0;
	
	// Relay Ports
	public final static int RELAY_LED_SWITCH_PORT = 3;
		
	// Joystick Ports
	public final static int LEFT_JOYSTICK_PORT = 0;
	public final static int RIGHT_JOYSTICK_PORT = 1;
	public final static int SHOOT_JOYSTICK_PORT = 2;
	
	// Button Joystick Buttons
	public static int CLIMB_SLOW_SPEED_BUTTON = Preferences.getInstance().getInt("Reel-In-Button", 11);
	public static int CLIMB_FAST_SPEED_BUTTON = Preferences.getInstance().getInt("Climb-Up-Button", 12);
	public static int CLIMB_REVERSE_BUTTON = Preferences.getInstance().getInt("Reverse-Climb-Button", 7);
	
	// Left Joystick Buttons
	public static int REVERSE_BUTTON = Preferences.getInstance().getInt("Reverse-Button", 3);
	
	// Right Joystick Buttons
	public static int FEED_POWER_BUTTON = Preferences.getInstance().getInt("Feeder-On-Button", 4);
	public static int FEED_POWER_OFF_BUTTON = Preferences.getInstance().getInt("Feeder-Off-Button", 6);
	public static int SHOOT_POWER_OFF_BUTTON = Preferences.getInstance().getInt("Shooter-Off-Button", 5);
	public static int SHOOT_POWER_ON_BUTTON = Preferences.getInstance().getInt("Shooter-On-Button", 3);

	// Motor Speeds
	public static double SLOW_SPEED = Preferences.getInstance().getDouble("Slow Speed", .5);
	public static double FAST_SPEED = Preferences.getInstance().getDouble("Fast Speed", .8);
	public static double TEST_SPEED = Preferences.getInstance().getDouble("Test Speed", .5);
	public static double REEL_SPEED = Preferences.getInstance().getDouble("Reel Speed", .3);
	public static double CLIMB_SPEED = Preferences.getInstance().getDouble("Climb Speed", .9);
	public static double SHOOTING_SPEED = Preferences.getInstance().getDouble("Shooting Speed", .99);
	public static double FEEDING_SPEED = Preferences.getInstance().getDouble("Feeding Speed", .8);
	
	// Robot Measurements (m)
	public final static double ROBOT_WIDTH = 0.9017; // w bumpers
	public final static double DISTANCE_PER_PULSE = 0.00132994*1.55;

	// Field Distances (m)
	public final static double WALL_TO_BASELINE = 2.8448;
	public final static double ESTIMATE_DIST_TO_LIFT = .5;
	public final static double ONE_FOOT = 0.3048;
	public static double TEST_DRIVE_DISTANCE = Preferences.getInstance().getDouble("Test Drive Distance", 5);
	public final static double DESIRED_DISTANCE_FROM_WALL = .3; // TODO change back
	public final static double BOILER_TO_CENTER_OF_ROBOT = 58.5;
	
	// Angles
	public static double TURN_ANGLE = Preferences.getInstance().getInt("Turn Angle", 60);
	public static double TEST_TURN_ANGLE = Preferences.getInstance().getInt("Test-Turn-Angle", 90);
	
	// stuff
	public final static int LEDDAR_SEGMENT = 7;
	
	// Debugging
	public static boolean RECORDING_LEDDAR_VALS = Preferences.getInstance().getBoolean("Leddar Recording", false);

	public static void updateVals() {
		// Button Joystick Buttons
		CLIMB_SLOW_SPEED_BUTTON = Preferences.getInstance().getInt("Reel-In-Button", 11);
		CLIMB_FAST_SPEED_BUTTON = Preferences.getInstance().getInt("Climb-Up-Button", 12);
		CLIMB_REVERSE_BUTTON = Preferences.getInstance().getInt("Reverse-Climb-Button", 7);
		
		// Left Joystick Buttons
		REVERSE_BUTTON = Preferences.getInstance().getInt("Reverse-Button", 3);
		
		// Right Joystick Buttons
		FEED_POWER_BUTTON = Preferences.getInstance().getInt("Feeder-On-Button", 4);
		FEED_POWER_OFF_BUTTON = Preferences.getInstance().getInt("Feeder-Off-Button", 6);
		SHOOT_POWER_OFF_BUTTON = Preferences.getInstance().getInt("Shooter-Off-Button", 5);
		SHOOT_POWER_ON_BUTTON = Preferences.getInstance().getInt("Shooter-On-Button", 3);
		
		// Motor Speeds
		SLOW_SPEED = Preferences.getInstance().getDouble("Slow Speed", .5);
		FAST_SPEED = Preferences.getInstance().getDouble("Fast Speed", .8);
		TEST_SPEED = Preferences.getInstance().getDouble("Test Speed", .5);
		REEL_SPEED = Preferences.getInstance().getDouble("Reel Speed", .3);
		CLIMB_SPEED = Preferences.getInstance().getDouble("Climb Speed", .9);
		SHOOTING_SPEED = Preferences.getInstance().getDouble("Shooting Speed", .99);
		FEEDING_SPEED = Preferences.getInstance().getDouble("Feeding Speed", .8);
		
		TEST_DRIVE_DISTANCE = Preferences.getInstance().getDouble("Test Drive Distance", 5);
	
		// Angles
		TURN_ANGLE = HeadingCalculator.normalize(Preferences.getInstance().getInt("Turn Angle", 60));
		TEST_TURN_ANGLE = Preferences.getInstance().getInt("Turn-Angle", 90);
		
		// Debugging
		RECORDING_LEDDAR_VALS = Preferences.getInstance().getBoolean("Leddar Recording", false);
	}
}
