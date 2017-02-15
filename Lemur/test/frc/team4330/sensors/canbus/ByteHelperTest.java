package frc.team4330.sensors.canbus;

import org.junit.Assert;
import org.junit.Test;

public class ByteHelperTest {
	
	@Test
	public void bytesToHex() {		
		Assert.assertEquals("00", ByteHelper.bytesToHex(new byte[1]));
		Assert.assertEquals("null", ByteHelper.bytesToHex(null));
		Assert.assertEquals("", ByteHelper.bytesToHex(new byte[0]));
		Assert.assertEquals("fe", ByteHelper.bytesToHex(toByteArray(new int[] {0xfe})));
		Assert.assertEquals("0abe", ByteHelper.bytesToHex(toByteArray(new int[] {0x0a, 0xbe})));
		Assert.assertEquals("72", ByteHelper.bytesToHex(toByteArray(new int[] {0x72})));
		Assert.assertEquals("0123456789abcdef", ByteHelper.bytesToHex(toByteArray(new int[] {0x01, 0x23, 0x45, 0x67, 0x89, 0xab, 0xcd, 0xef})));
		Assert.assertEquals("ff008280817f", ByteHelper.bytesToHex(new byte[] {-1, 0, -126, -128, -127, 127}));
		Assert.assertEquals("000102030405060708090a0b0c0d0e0f", ByteHelper.bytesToHex(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}));
	}
	
	@Test
	public void getLSBValue() {
		Assert.assertEquals(toByte(10), ByteHelper.getLSBValue(toByte(0b10101010), 4));
		Assert.assertEquals(toByte(0), ByteHelper.getLSBValue(toByte(0b10101010), 1));
		Assert.assertEquals(toByte(1), ByteHelper.getLSBValue(toByte(0b10101011), 1));
		Assert.assertEquals(toByte(3), ByteHelper.getLSBValue(toByte(0b10101011), 2));
		Assert.assertEquals(toByte(43), ByteHelper.getLSBValue(toByte(0b10101011), 7));
		Assert.assertEquals(toByte(171), ByteHelper.getLSBValue(toByte(0b10101011), 8));
		Assert.assertEquals(toByte(0), ByteHelper.getLSBValue(toByte(0xff), 0));
		try {
			ByteHelper.getLSBValue(toByte(0b10101011), 10);
			Assert.fail("Should have complained since asked for 10 bits");
		} catch (RuntimeException e) {
			// expected
			Assert.assertEquals("numberBits parameter should be between 0 and 8 but was 10", e.getMessage());
		}


	}
	
	@Test
	public void getMSBValue() {
		Assert.assertEquals(toByte(0), ByteHelper.getMSBValue(toByte(0b00101011), 1));
		Assert.assertEquals(toByte(14), ByteHelper.getMSBValue(toByte(0b11101010), 4));
		Assert.assertEquals(toByte(0), ByteHelper.getMSBValue(toByte(0b00101011), 1));
		Assert.assertEquals(toByte(1), ByteHelper.getMSBValue(toByte(0b10101011), 1));
		Assert.assertEquals(toByte(2), ByteHelper.getMSBValue(toByte(0b10101011), 2));
		Assert.assertEquals(toByte(85), ByteHelper.getMSBValue(toByte(0b10101011), 7));
	}
	
	/**
	 * You need to up-cast everything.
	 * So advanced.
	 * ie byte => short; short => int
	 */
	@Test
	public void readShort() {
		byte[] stuff = toByteArray(new int[] {0xae, 0x03, 0xf9});
		Assert.assertEquals( 44547, ByteHelper.readShort(stuff, 0, false));
		Assert.assertEquals( 942, ByteHelper.readShort(stuff, 0, true));
		Assert.assertEquals( 63747, ByteHelper.readShort(stuff, 1, true));
		Assert.assertEquals( 1017, ByteHelper.readShort(stuff, 1, false));
		Assert.assertEquals( 22340, ByteHelper.readShort(toByteArray(new int[] {0x57, 0x44, 0xef}), 0, false));

	}
	
	private byte[] toByteArray(int[] intArray) {
		byte[] values = new byte[intArray.length];
		for ( int i = 0; i < intArray.length; i++ ) {
			values[i] = (byte) intArray[i];
		}
		return values;
	}
	
	private byte toByte(int value) {
		return (byte) value;
	}
	

}
