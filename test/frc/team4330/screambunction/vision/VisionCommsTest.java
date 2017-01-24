package frc.team4330.screambunction.vision;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Map;

import org.junit.Test;

/**
 * @author rltiel
 *
 */
public class VisionCommsTest {
	
	public static void main ( String[] args ) {
		try {
			VisionComms comms = new VisionComms("10.10.20.61", 9001);
			comms.startUp();
			while ( true ) {
				Map<String,String> messages = comms.retrieveData();
				if ( messages.size() > 0 ) {
					System.out.println(new Date() + " - x=" + messages.get("rb"));
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetMessages0() throws Exception {
		// pattern is 0
		String traffic = "\n";
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ByteArrayInputStream is = new ByteArrayInputStream(traffic.getBytes());
		Map<String,String> messages = VisionComms.getMessages(os, is);
		assertEquals(0, messages.size());
		assertEquals("DATA\n", os.toString());
	}
	
	@Test
	public void testGetMessages1() throws Exception {
		// pattern is 1
		String traffic = "rb=8.89\n\n";
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ByteArrayInputStream is = new ByteArrayInputStream(traffic.getBytes());
		Map<String,String> messages = VisionComms.getMessages(os, is);
		assertEquals(1, messages.size());
		assertEquals("8.89", messages.get("rb"));
		assertEquals("DATA\n", os.toString());
	}
	
	@Test
	public void testGetMessages2() throws Exception {
		// pattern is 2
		String traffic = "rb=8.89\nry=1.53\n\n";
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ByteArrayInputStream is = new ByteArrayInputStream(traffic.getBytes());
		Map<String,String> messages = VisionComms.getMessages(os, is);
		assertEquals(2, messages.size());
		assertEquals("8.89", messages.get("rb"));
		assertEquals("1.53", messages.get("ry"));
		assertEquals("DATA\n", os.toString());
	}
	
	@Test
	public void testGetMessages1_1() throws Exception {
		// pattern is 1-1
		String traffic = "rb=8.89\n\nrb=10.58\n\n";
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ByteArrayInputStream is = new ByteArrayInputStream(traffic.getBytes());
		Map<String,String> messages = VisionComms.getMessages(os, is);
		assertEquals(1, messages.size());
		assertEquals("8.89", messages.get("rb"));
		assertEquals("DATA\n", os.toString());
		os.reset();
		messages = VisionComms.getMessages(os, is);
		assertEquals(1, messages.size());
		assertEquals("10.58", messages.get("rb"));
		assertEquals("DATA\n", os.toString());
	}
	
	@Test
	public void testGetMessages0_1_0_1_0() throws Exception {
		// pattern is 0-1-0-1-0
		String traffic = "\nrb=9.82\n\n\nrb=15.84\n\n\n";
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ByteArrayInputStream is = new ByteArrayInputStream(traffic.getBytes());
		
		Map<String,String> messages = VisionComms.getMessages(os, is);
		assertEquals(0, messages.size());
		assertEquals("DATA\n", os.toString());
		os.reset();
		
		messages = VisionComms.getMessages(os, is);
		assertEquals(1, messages.size());
		assertEquals("9.82", messages.get("rb"));
		assertEquals("DATA\n", os.toString());
		os.reset();
		
		messages = VisionComms.getMessages(os, is);
		assertEquals(0, messages.size());
		assertEquals("DATA\n", os.toString());
		os.reset();
		
		messages = VisionComms.getMessages(os, is);
		assertEquals(1, messages.size());
		assertEquals("15.84", messages.get("rb"));
		assertEquals("DATA\n", os.toString());
		os.reset();
		
		messages = VisionComms.getMessages(os, is);
		assertEquals(0, messages.size());
		assertEquals("DATA\n", os.toString());
	}
	
	@Test
	public void testGetMessages0_0_0_1() throws Exception {
		// pattern is 0-0-0-1
		String traffic = "\n\n\nrb=15.23\n\n";
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ByteArrayInputStream is = new ByteArrayInputStream(traffic.getBytes());
		
		Map<String,String> messages = VisionComms.getMessages(os, is);
		assertEquals(0, messages.size());
		assertEquals("DATA\n", os.toString());
		os.reset();
		
		messages = VisionComms.getMessages(os, is);
		assertEquals(0, messages.size());
		assertEquals("DATA\n", os.toString());
		os.reset();
		
		messages = VisionComms.getMessages(os, is);
		assertEquals(0, messages.size());
		assertEquals("DATA\n", os.toString());
		os.reset();
		
		messages = VisionComms.getMessages(os, is);
		assertEquals(1, messages.size());
		assertEquals("15.23", messages.get("rb"));
		assertEquals("DATA\n", os.toString());
	}
	
	@Test
	public void testGetMessages1_0_0_0() throws Exception {
		// pattern is 1-0-0-0
		String traffic = "rb=10.00\n\n\n\n\n";
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ByteArrayInputStream is = new ByteArrayInputStream(traffic.getBytes());
		
		Map<String,String> messages = VisionComms.getMessages(os, is);
		assertEquals(1, messages.size());
		assertEquals("10.00", messages.get("rb"));
		assertEquals("DATA\n", os.toString());
		os.reset();
		
		messages = VisionComms.getMessages(os, is);
		assertEquals(0, messages.size());
		assertEquals("DATA\n", os.toString());
		os.reset();
		
		messages = VisionComms.getMessages(os, is);
		assertEquals(0, messages.size());
		assertEquals("DATA\n", os.toString());
		os.reset();
		
		messages = VisionComms.getMessages(os, is);
		assertEquals(0, messages.size());
		assertEquals("DATA\n", os.toString());
	}
	
	@Test
	public void testGetMessages2_0_0_0_2() throws Exception {
		// pattern is 2-0-0-0-2
		String traffic = "rb=10.00\nry=14.5\n\n\n\n\nrb=11.53\nry=13.21\n\n";
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ByteArrayInputStream is = new ByteArrayInputStream(traffic.getBytes());
		
		Map<String,String> messages = VisionComms.getMessages(os, is);
		assertEquals(2, messages.size());
		assertEquals("10.00", messages.get("rb"));
		assertEquals("14.5", messages.get("ry"));
		assertEquals("DATA\n", os.toString());
		os.reset();
		
		messages = VisionComms.getMessages(os, is);
		assertEquals(0, messages.size());
		assertEquals("DATA\n", os.toString());
		os.reset();
		
		messages = VisionComms.getMessages(os, is);
		assertEquals(0, messages.size());
		assertEquals("DATA\n", os.toString());
		os.reset();
		
		messages = VisionComms.getMessages(os, is);
		assertEquals(0, messages.size());
		assertEquals("DATA\n", os.toString());
		os.reset();
		
		messages = VisionComms.getMessages(os, is);
		assertEquals(2, messages.size());
		assertEquals("11.53", messages.get("rb"));
		assertEquals("13.21", messages.get("ry"));
		assertEquals("DATA\n", os.toString());
	}

}
