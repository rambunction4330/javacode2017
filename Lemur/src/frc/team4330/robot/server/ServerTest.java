package frc.team4330.robot.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import frc.team4330.robot.Robot;

public class ServerTest {
	Thread thread;
	//	public static void main(String[] args) throws IOException {
	//		start();
	//	}

	public void start() throws IOException {
		int portNumber=9003;
		ServerSocket listener=new ServerSocket(portNumber);

		thread = new Thread() {
			public void run() {
				try {
					while (Robot.serverOn) {
						Socket socket=listener.accept();
						System.out.println("server on");
						try {
							PrintWriter out=new PrintWriter(socket.getOutputStream(), true);
							BufferedReader in = new BufferedReader(
									new InputStreamReader(socket.getInputStream()));

							String input, output;
							System.out.println("we halfway there");
							ServerProtocol proto = new ServerProtocol();

							while ((input = in.readLine()) != null) {
								output = proto.processDataRequests(input);
								System.out.println("input is " + input);
								System.out.println("output is " + output);

								out.println(output);

							}
						}
						finally { socket.close(); }
					}
				} catch(Exception e) {}
				finally {
					try { listener.close(); } catch (IOException e) {}
				}
			}
		};

		thread.start();
	}
	
	public void stop() throws IOException {
		if (thread != null)
			thread.interrupt();
	}
}
