package frc.team4330.robot.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import frc.team4330.robot.Robot;

public class ServerTest {
	
//	public static void main(String[] args) throws IOException {
//		start();
//	}

	public void start() throws IOException
	{
		//LeddarDistanceSensor leddar = new LeddarDistanceSensor();
		//leddar.startUp();
		int portNumber=9003;
		ServerSocket listener=new ServerSocket(portNumber);

		try {
			while (Robot.serverOn)
			{
				Socket socket=listener.accept();

				try {
					PrintWriter out=new PrintWriter(socket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));

					String input, output;
					System.out.println("we halfway there");
					LeddarDataProtocol ldp = new LeddarDataProtocol();
					NavXProtocol navp = new NavXProtocol();

					while ((input = in.readLine()) != null) {
						output = navp.processDataRequests(input);
						System.out.println("input is " + input);
						System.out.println("output is " + output);

//						output = ldp.processDataRequests(input);
						out.println(output);
						
					}
				}
				finally {
					socket.close();
				}
			}
		}
		finally {
			listener.close();
		}
	}
}
