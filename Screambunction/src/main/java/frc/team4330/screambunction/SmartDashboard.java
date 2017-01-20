package frc.team4330.screambunction;

import java.awt.Color;

import edu.wpi.first.smartdashboard.*;
import frc.team4330.screambunction.smartdashboard.extensions.*;

public class SmartDashboard {
	Slider position = new Slider();

	public SmartDashboard() {
		position.init();
		position.setBackground(Color.GREEN);
		
	}
}
