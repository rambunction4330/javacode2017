package frc.team4330.screambunction;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest {

	public ServerTest() throws IOException {
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
					out.println("RIO socket success.");
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
