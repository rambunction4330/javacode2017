package frc.team4330.sensors.distance;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * The vision board has a custom TCP/IP protocol to indicate where the target is located.
 * The client sends a "DATA\n" request to ask for target data.  The server will respond
 * with "seg=value\nseg=value\n" where an empty line indicates the end of the response.
 * If the server has no target data, it will respond with "\n" ( an empty line only ). 
 */
public class LeddarComms {
	
	public static final int CONNECTION_TIMEOUT_SEC = 10;
	public static final String DEFAULT_VISION_BOARD_HOST = "tegra-ubuntu.local";
	public static final int DEFAULT_VISION_BOARD_PORT = 9004;
	
	private String host = DEFAULT_VISION_BOARD_HOST;
	private int port = DEFAULT_VISION_BOARD_PORT;
	
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private boolean active = false;
	private static final byte[] GET_DATA_COMMAND = "DATA\n".getBytes();
	private static final byte[] STOP_COMMAND = "STOP\n".getBytes();
	
	/**
	 * This constructor will use the default values for hostname and constructor
	 */
	public LeddarComms() {
		
	}
	
	/**
	 * Use this constructor to change the default values of hostname and constructor
	 * @param host
	 * @param port
	 */
	public LeddarComms(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public synchronized void startUp() throws IOException {
		
		System.out.println("Opening connection to Jetson (Leddar) on " + host + ":" + port + 
				" with " + CONNECTION_TIMEOUT_SEC + " second timeout");
		
		socket = new Socket();
		
		// we don't want to block the calling thread for long time in case the
		// connection doesn't work immediately, so try to connect to the vision board 
		// on another thread
		Thread connectThread = new Thread() {

			@Override
			public void run() {
				try {
					// connect is a blocking call which will throw a SocketException if the socket
					// is asked to close while blocked, so this thread will die
					socket.connect(new InetSocketAddress(host,port), CONNECTION_TIMEOUT_SEC * 1000); 
					is = socket.getInputStream();
					os = socket.getOutputStream();
					active = true;
					os.write(GET_DATA_COMMAND);
					os.flush();
					
					System.out.println("Successfully connected to Jetson (Leddar).");
				} catch ( Exception e ) {
					System.err.println("Error connecting to Jetson (Leddar) on startup. " + e.getMessage());
				}
			}
			
		};
		
		connectThread.setDaemon(true);
		connectThread.start();

	}
	
	public synchronized void shutDown() throws IOException {
		System.out.println("Disconnecting from Jetson (Leddar).");
		active = false;
		
		if ( os != null ) {
			try {
				os.write(STOP_COMMAND);
				os.flush();
				os.close();
			} catch(Exception e) {
				//				e.printStackTrace();
			} finally {
				os = null;
			}
		}
		if ( is != null ) {
			try {
				is.close();
			}catch (Exception e) {

			}finally {
				is = null;
			}
		}
		if ( socket != null ) {
			try {
			socket.close();
			} catch (Exception e) {
				
			} finally {
			socket = null;
			}
		}
		System.out.println("Successfully disconnected from Jetson.");
	}

	public synchronized Map<Integer, Integer> retrieveData() {
		if ( !active ) {
			System.out.println("Leddar is not active.");
			// the client may still be trying to connect
			return new HashMap<Integer,Integer>();
		}
		try {
			return getMessages(os, is);
		} catch ( Exception e ) {
//			e.printStackTrace();
			System.err.println("Error getting messages from the Jetson. " + e.getMessage());
			return new HashMap<Integer,Integer>();
		}
	}
	
	static protected Map<Integer, Integer> getMessages(OutputStream os, InputStream is) throws IOException {
		if (os == null || is == null)
			return new HashMap<Integer,Integer>();
		
		// send a request to server for data
		os.write("DATA\n".getBytes());
		os.flush();

		// read the response from the server
		ByteArrayOutputStream binaryData = new ByteArrayOutputStream();
		int lastChar = Integer.MIN_VALUE;
		int endLine = '\n';
		while (true) {
			int thisChar = is.read();
			if (thisChar == -1 || (thisChar == endLine && (lastChar == endLine || lastChar == Integer.MIN_VALUE) )) {
				break;
			}
			binaryData.write(thisChar);
			lastChar = thisChar;
		}

		// had no data
		if ( binaryData.size() == 0 ) {
			System.out.println("Leddar has no data.");
			return new HashMap<Integer,Integer>();
		}
		
		// get the data as a string
		String data = binaryData.toString();
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();

		// parse the data into map
		LineNumberReader reader = new LineNumberReader(new StringReader(data));
		String line = null;
		while ((line = reader.readLine()) != null) {
//			System.out.println("Vision has read line " + line);
			int index = line.indexOf("=");
			if (index == -1) {
				continue;
			}
			Integer key = Integer.parseInt(line.substring(0, index));
			Integer value = Integer.parseInt(line.substring(index + 1));
			map.put(key, value);
		}

		return map;
	}

}
