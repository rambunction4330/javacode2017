package frc.team4330.robot;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.team4330.robot.commands.SpinShooter;
import frc.team4330.robot.commands.StartAgitator;
import frc.team4330.robot.commands.StartClimb;
import frc.team4330.robot.utils.RobotMap;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	
	Button shootButton = new JoystickButton(Robot.buttonj, RobotMap.SHOOT_POWER_ON_BUTTON),
			shootEndButton = new JoystickButton(Robot.buttonj, RobotMap.SHOOT_POWER_OFF_BUTTON),
			feedButton = new JoystickButton(Robot.buttonj, RobotMap.FEED_POWER_BUTTON),
			feedEndButton = new JoystickButton(Robot.buttonj, RobotMap.FEED_POWER_OFF_BUTTON);

	SpinShooter spinCommand = new SpinShooter();
	StartAgitator agitatorCommand = new StartAgitator();
	StartClimb startClimbingCommand = new StartClimb();
	
	public OI() {
		shootButton.whenPressed(spinCommand);
		shootEndButton.cancelWhenPressed(spinCommand);
		feedButton.whenPressed(agitatorCommand);
		feedEndButton.cancelWhenPressed(agitatorCommand);
	}
}
