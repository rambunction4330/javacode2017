package frc.team4330.robot

import edu.wpi.first.wpilibj.Preferences

object RobotMap {

    // Motor Ports
    val MOTOR_ONE_PORT = 2
    val MOTOR_TWO_PORT = 0
    val MOTOR_THREE_PORT = 3
    val MOTOR_FOUR_PORT = 1
    val MOTOR_CLIMB_PORT = 4
    val MOTOR_SHOOT_PORT = 5
    val MOTOR_FEED_PORT = 6

    // DIO Ports
    val ENCODER_LEFT_ONE_PORT = 0
    val ENCODER_LEFT_TWO_PORT = 1
    val ENCODER_RIGHT_ONE_PORT = 2
    val ENCODER_RIGHT_TWO_PORT = 3
    val MAXSONAR_PORT = 0

    // Relay Ports
    val RELAY_LED_SWITCH_PORT = 3

    // Joystick Ports
    val LEFT_JOYSTICK_PORT = 0
    val RIGHT_JOYSTICK_PORT = 1
    val SHOOT_JOYSTICK_PORT = 2

    // Button Joystick Buttons
    var CLIMB_SLOW_SPEED_BUTTON = Preferences.getInstance().getInt("Reel-In-Button", 2)
    var CLIMB_FAST_SPEED_BUTTON = Preferences.getInstance().getInt("Climb-Up-Button", 1)
    //	public static int CLIMB_REVERSE_BUTTON = Preferences.getInstance().getInt("Reverse-Climb-Button", 7);
    var FEED_POWER_BUTTON = Preferences.getInstance().getInt("Feeder-On-Button", 4)
    var FEED_POWER_OFF_BUTTON = Preferences.getInstance().getInt("Feeder-Off-Button", 6)
    var SHOOT_POWER_OFF_BUTTON = Preferences.getInstance().getInt("Shooter-Off-Button", 5)
    var SHOOT_POWER_ON_BUTTON = Preferences.getInstance().getInt("Shooter-On-Button", 3)

    // Left Joystick Buttons
    var REVERSE_BUTTON = Preferences.getInstance().getInt("Reverse-Button", 3)

    // Motor Speeds
    val SLOW_SPEED = .5
    val FAST_SPEED = .8
    val TEST_SPEED = .5
    val REEL_SPEED = .4
    val CLIMB_SPEED = .95
    val SHOOTING_SPEED = .99
    val FEEDING_SPEED = .8

    // Robot Measurements (m)
    val ROBOT_WIDTH = 0.9017 // w bumpers
    val DISTANCE_PER_PULSE = 0.00132994 * 1.55

    // Field Distances (m)
    val WALL_TO_BASELINE = 2.37
    val INITIAL_LEFTRIGHT_DISTANCE = 2.0
    val ESTIMATE_DIST_TO_LIFT = .5
    val ONE_FOOT = 0.3048
    var TEST_DRIVE_DISTANCE = Preferences.getInstance().getDouble("Test Drive Distance", 5.0)
    val DESIRED_DISTANCE_FROM_WALL = .08 // TODO change back
    val BOILER_TO_CENTER_OF_ROBOT = 58.5

    // Angles
    val TURN_ANGLE = 45.0
    var TEST_TURN_ANGLE = Preferences.getInstance().getInt("Test-Turn-Angle", 90).toDouble()

    // stuff
    val LEDDAR_SEGMENT = 7
    val WAIT_TIMES = .5

    // Debugging
    var RECORDING_LEDDAR_VALS = Preferences.getInstance().getBoolean("Leddar Recording", false)

    fun updateVals() {
        // Button Joystick Buttons
        CLIMB_SLOW_SPEED_BUTTON = Preferences.getInstance().getInt("Reel-In-Button", 2)
        CLIMB_FAST_SPEED_BUTTON = Preferences.getInstance().getInt("Climb-Up-Button", 1)
        FEED_POWER_BUTTON = Preferences.getInstance().getInt("Feeder-On-Button", 4)
        FEED_POWER_OFF_BUTTON = Preferences.getInstance().getInt("Feeder-Off-Button", 6)
        SHOOT_POWER_OFF_BUTTON = Preferences.getInstance().getInt("Shooter-Off-Button", 5)
        SHOOT_POWER_ON_BUTTON = Preferences.getInstance().getInt("Shooter-On-Button", 3)

        // Left Joystick Buttons
        REVERSE_BUTTON = Preferences.getInstance().getInt("Reverse-Button", 3)

        // Field Measurements
        TEST_DRIVE_DISTANCE = Preferences.getInstance().getDouble("Test Drive Distance", 5.0)

        // Angles
        TEST_TURN_ANGLE = Preferences.getInstance().getInt("Turn-Angle", 90).toDouble()

        // Debugging
        RECORDING_LEDDAR_VALS = Preferences.getInstance().getBoolean("Leddar Recording", false)
    }
}
