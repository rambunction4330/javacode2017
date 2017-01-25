package org.usfirst.frc.team4330.utils;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;

public class Registrar {
	
	private static Map<String,Object> map = new HashMap<String,Object>();
	
	public static Talon talon(int port) {
		String key = "talon:" + port;
		if ( map.get(key) == null ) {
			map.put(key, new Talon(port));
		}
		return (Talon) map.get(key);
	}
	
	public static Relay relay(int port) {
		String key = "relay:" + port;
		if ( map.get(key) == null ) {
			map.put(key, new Relay(port));
		}
		return (Relay) map.get(key);
	}
	
	public static Spark spark(int port) {
		String key = "spark:" + port;
		if ( map.get(key) == null ) {
			map.put(key, new Spark(port));
		}
		return (Spark) map.get(key);
	}
	
	public static Joystick joystick(int port) {
		String key = "joystick:" + port;
		if ( map.get(key) == null ) {
			map.put(key, new Joystick(port));
		}
		return (Joystick) map.get(key);
	}

}
