package frc.team4330.screambunction.vision;

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
 * with "key1=value\nkey2=value\n\n" where an empty line indicates the end of the response.
 * If the server has no target data, it will respond with "\n" ( an empty line only ). 
 */
public class VisionComms {
	
	public static final int CONNECTION_TIMEOUT_SEC = 10;
	public static final String DEFAULT_VISION_BOARD_HOST = "tegra-ubuntu";
	public static final int DEFAULT_VISION_BOARD_PORT = 9001;
	
	// TODO update after know the mDNS name of the vision processing host
	private String host = DEFAULT_VISION_BOARD_HOST;
	private int port = DEFAULT_VISION_BOARD_PORT;
	
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private boolean active = false;
	private static final byte[] GET_DATA_COMMAND = "DATA\n".getBytes();
	private static final byte[] STOP_COMMAND = "STOP\n".getBytes();
	
	public static final String KEY_RELATIVE_BEARING = "rb";
	public static final String KEY_VERTICAL_ANGLE = "nya";
	
	/**
	 * This constructor will use the default values for hostname and constructor
	 */
	public VisionComms() {
		
	}
	
	/**
	 * Use this constructor to change the default values of hostname and constructor
	 * @param host
	 * @param port
	 */
	public VisionComms(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public synchronized void startUp() throws IOException {
		
		System.out.println("Opening connection to vision board on " + host + ":" + port + 
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
					System.out.println("Successfully connected to vision board");
				} catch ( Exception e ) {
					System.err.println("Error connecting to vision board on startup. " + e.getMessage());
				}
			}
			
		};
		
		connectThread.setDaemon(true);
		connectThread.start();

	}
	
	public synchronized void shutDown() throws IOException {
		System.out.println("Disconnecting from vision board");
		active = false;
		
		if ( os != null ) {
			os.write(STOP_COMMAND);
			os.flush();
			os.close();
		}
		if ( is != null ) {
			is.close();
		}
		if ( socket != null ) {
			socket.close();
			socket = null;
		}
		System.out.println("Successfully disconnected from vision board");
	}

	public synchronized Map<String, String> retrieveData() {
		if ( !active ) {
			// the client may still be trying to connect
			return new HashMap<String,String>();
		}
		try {
			return getMessages(os, is);
		} catch ( Exception e ) {
			System.err.println("Error getting messages from the vision board. " + e.getMessage());
			return new HashMap<String, String>();
		}
	}
	
	static protected Map<String, String> getMessages(OutputStream outputStream, InputStream inputStream) throws IOException {
		// send a request to server for data
		outputStream.write(GET_DATA_COMMAND);
		outputStream.flush();

		// read the response from the server
		ByteArrayOutputStream binaryData = new ByteArrayOutputStream();
		int lastChar = Integer.MIN_VALUE;
		int endLine = '\n';
		while (true) {
			int thisChar = inputStream.read();
			if (thisChar == endLine && (lastChar == endLine || lastChar == Integer.MIN_VALUE) ) {
				break;
			}
			binaryData.write(thisChar);
			lastChar = thisChar;
		}

		// had no data
		if ( binaryData.size() == 0 ) {
			return new HashMap<String,String>();
		}
		
		// get the data as a string
		String data = binaryData.toString();
		Map<String, String> map = new HashMap<String, String>();

		// parse the data into map
		LineNumberReader reader = new LineNumberReader(new StringReader(data));
		String line = null;
		while ((line = reader.readLine()) != null) {
			int index = line.indexOf("=");
			if (index == -1) {
				continue;
			}
			String key = line.substring(0, index);
			String value = line.substring(index + 1);
			map.put(key, value);
		}

		return map;
	}

}
