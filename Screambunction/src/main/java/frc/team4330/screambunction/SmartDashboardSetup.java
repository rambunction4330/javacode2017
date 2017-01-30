package frc.team4330.screambunction;

import java.awt.Color;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4330.screambunction.smartdashboard.extensions.Slider;

public class SmartDashboardSetup {
	SendableChooser pinPosition;
	Slider position;


	public SmartDashboardSetup() {
		pinPosition = new SendableChooser();
		pinPosition.addDefault("left", "left");
		pinPosition.addObject("middle", "middle");
		pinPosition.addObject("right", "right");
		SmartDashboard.putData("Autonomous Position", pinPosition);



		position = new Slider();
		position.init();
		position.setBackground(Color.GREEN);
		position.setBounds(10, 10, 100, 20);

	}
	
	public int startPosition() {
		return 3;
	}
	public int getAutoPosition() {
		return (int) pinPosition.getSelected();
	}
}
