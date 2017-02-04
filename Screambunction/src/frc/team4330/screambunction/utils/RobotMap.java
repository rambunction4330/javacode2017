package frc.team4330.screambunction.utils;

public class RobotMap {

	// Motor Ports
	public final static int MOTOR_ONE_PORT = 0;
	public final static int MOTOR_TWO_PORT = 1;
	public final static int MOTOR_THREE_PORT = 2;
	public final static int MOTOR_FOUR_PORT = 3;
	public final static int MOTOR_CLIMB_PORT = 4;
	public final static int MOTOR_SHOOT_PORT = 5;
	
	// Relay Ports
	public final static int RELAY_FEED_PORT = 1;
	public final static int RELAY_LED_SWITCH_PORT = 0;
	
	// DigitalInput Ports
	public final static int MAXSONAR_PORT = 0;
	
	// Joystick Ports
	public final static int LEFT_JOYSTICK_PORT = 0;
	public final static int RIGHT_JOYSTICK_PORT = 1;
	public final static int SHOOT_JOYSTICK_PORT = 2;
	
	// Buttons
	public final static int CLIMB_SLOW_SPEED_BUTTON = 4;
	public final static int CLIMB_FAST_SPEED_BUTTON = 6;
	public final static int SHOOT_POWER_OFF_BUTTON = 3;
	public final static int SHOOT_POWER_ON_BUTTON = 2;
	public final static int SHOOT_ADD_POWER_BUTTON = 11;
	public final static int SHOOT_SUB_POWER_BUTTON = 12;
	public final static int FEED_POWER_BUTTON = 1;
	public final static int REVERSE_BUTTON = 3;

	// Motor Speeds
	public final static double SLOW_SPEED = .5;
	public final static double FAST_SPEED = .8;
	public final static double TEST_SPEED = .5;
	public final static double REEL_SPEED = .3;
	public final static double CLIMB_SPEED = .9;
	public final static double SHOOTING_SPEED = .4;
	public final static double INCREMENT_VALUE = .02;
	
	// Robot Measurements (m)
	public final static double ROBOT_WIDTH = 0.9017;

	// Field Distances (m)
	public final static double DISTANCE_TO_BASELINES = 2.8448 - ROBOT_WIDTH;
	
	// Angles
	public final static double TURN_ANGLE = 45;

}
