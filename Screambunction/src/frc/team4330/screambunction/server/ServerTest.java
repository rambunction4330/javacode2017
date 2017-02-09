package frc.team4330.screambunction.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest {

	public static void main(String[] args) throws IOException {
		start();
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
					BufferedReader in = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));
					//out.println("RIO socket success.");

					String input, output;

					LeddarDataProtocol ldp = new LeddarDataProtocol();

					while ((input = in.readLine()) != null) {
						if (input.equalsIgnoreCase("LEDDAR")) {
							ldp.start();
						} else if (input.equalsIgnoreCase("Over")) {
							ldp.end();
							break;
						} else {
							output = ldp.processDataRequests(input);
							out.println(output);
						}
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
