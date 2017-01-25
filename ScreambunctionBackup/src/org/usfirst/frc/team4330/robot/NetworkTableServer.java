package org.usfirst.frc.team4330.robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * Server for running Network tables.
 * You must add the networktables-desktop.jar file to your classpath.
 */
public class NetworkTableServer {

    public static void main(String[] args) throws Exception {
        NetworkTable.setServerMode();
        NetworkTable.getTable("");
        
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        while (true) {
            Thread.sleep(100L);
        }
    }

}