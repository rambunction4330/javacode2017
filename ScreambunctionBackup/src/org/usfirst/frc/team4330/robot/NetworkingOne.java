package org.usfirst.frc.team4330.robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NetworkingOne {

    public static void main(String[] args) {
        NetworkTable.setClientMode();
        NetworkTable.setIPAddress("127.0.0.1");
        SmartDashboard.putNumber("set value", 0);
        double num = SmartDashboard.getNumber("set value");
        NetworkTable.getTable("SmartDashboard").putNumber("share value", num);
    }
}