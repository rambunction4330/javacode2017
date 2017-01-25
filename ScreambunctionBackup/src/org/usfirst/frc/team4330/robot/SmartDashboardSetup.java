package org.usfirst.frc.team4330.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import frc.team4330.screambunction.smartdashboard.extensions.*;

public class SmartDashboardSetup {
	SendableChooser pinPosition;


	public SmartDashboardSetup() {
		pinPosition = new SendableChooser();
		pinPosition.addDefault("left", "left");
		pinPosition.addObject("middle", "middle");
		pinPosition.addObject("right", "right");
		SmartDashboard.putData("Pin Chooser", pinPosition);

		SmartDashboard.putNumber("Horizontal Position", 0);




	}

	public int startPosition() {
		return 3;
	}
	public int getAutoPosition() {
		return (int) pinPosition.getSelected();
	}
}
