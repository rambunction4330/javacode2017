package frc.team4330.robot

import edu.wpi.first.wpilibj.buttons.Button
import edu.wpi.first.wpilibj.buttons.JoystickButton
import frc.team4330.robot.commandgroups.AutomatedLiftProcedure
import frc.team4330.robot.commands.Climb
import frc.team4330.robot.commands.Reverse
import frc.team4330.robot.commands.SpinShooter
import frc.team4330.robot.commands.StartAgitator
import frc.team4330.robot.commands.StartClimb


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
class OI {

    internal var shootButton: Button = JoystickButton(Robot.buttonj, RobotMap.SHOOT_POWER_ON_BUTTON)
    internal var shootEndButton: Button = JoystickButton(Robot.buttonj, RobotMap.SHOOT_POWER_OFF_BUTTON)
    internal var feedButton: Button = JoystickButton(Robot.buttonj, RobotMap.FEED_POWER_BUTTON)
    internal var feedEndButton: Button = JoystickButton(Robot.buttonj, RobotMap.FEED_POWER_OFF_BUTTON)
    internal var liftButton: Button = JoystickButton(Robot.buttonj, 11)
    internal var liftEndButton: Button = JoystickButton(Robot.buttonj, 12)
    internal var reelButton: Button = JoystickButton(Robot.buttonj, RobotMap.CLIMB_SLOW_SPEED_BUTTON)
    internal var climbButton: Button = JoystickButton(Robot.buttonj, RobotMap.CLIMB_FAST_SPEED_BUTTON)
    internal var climbEndButton: Button = JoystickButton(Robot.buttonj, 7)
    internal var reverseButton: Button = JoystickButton(Robot.buttonj, 8)

    internal var spinCommand = SpinShooter()
    internal var agitatorCommand = StartAgitator()
    internal var startClimbingCommand = StartClimb()
    internal var climbingCommand = Climb()
    internal var liftCommands = AutomatedLiftProcedure()
    internal var reverse = Reverse()

    init {
        shootButton.whenPressed(spinCommand)
        shootEndButton.cancelWhenPressed(spinCommand)
        shootEndButton.cancelWhenPressed(agitatorCommand)
        feedButton.whenPressed(agitatorCommand)
        feedEndButton.cancelWhenPressed(agitatorCommand)
        liftButton.whenPressed(liftCommands)
        liftEndButton.cancelWhenPressed(liftCommands)
        reelButton.whenPressed(startClimbingCommand)
        climbButton.whenPressed(climbingCommand)
        climbEndButton.cancelWhenPressed(startClimbingCommand)
        climbEndButton.cancelWhenPressed(climbingCommand)
        reverseButton.whileHeld(reverse)
    }

}
