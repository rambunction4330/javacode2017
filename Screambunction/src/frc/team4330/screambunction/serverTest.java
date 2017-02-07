package frc.team4330.screambunction;
import java.io.*;
import java.net.*;

import frc.team4330.sensors.distance.LeddarDistanceSensor;
public class serverTest {

	public static void main(String[] args) throws IOException {
		start();
		// TODO Auto-generated method stub

	}
	public static void start() throws IOException
	{
		//LeddarDistanceSensor leddar = new LeddarDistanceSensor();
		//leddar.startUp();
		int portNumber=9003;
		ServerSocket listener=new ServerSocket(portNumber);
		try {
			while (true)
			{
				Socket socket=listener.accept();
				try {
				PrintWriter out=new PrintWriter(socket.getOutputStream(), true);
				out.println("hi");
				}
				finally {socket.close();}
			}
		}
		finally {listener.close();}
	}
}
