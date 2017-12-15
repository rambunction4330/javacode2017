package frc.team4330.sensors.canbus;

public class ByteHelper {

	public static int readShort(byte[] data, int offset, boolean littleEndianByteOrder) {
		if(data == null) {
			throw new RuntimeException("readShort bad parameters(null byte array passed)");
		}

		if ( offset + 1 >= data.length ) {
            throw new RuntimeException("readShort bad parameters(index too big for arrayLength): data=" +
				ByteHelper.bytesToHex(data) + " arrayLength=" + data.length +
				" offset=" + offset);
		}

        int lo = 0;
		int hi = 0;
		int mask = 0xff;
		if ( littleEndianByteOrder ) {
			lo = data[offset] & mask;
			hi = data[offset + 1] & mask;
		} else {
			hi = data[offset] & mask;
			lo = data[offset + 1] & mask;
		}
		return (hi << 8) | lo;
	}

    public static byte getMSBValue(byte data, int numberBits) {
        if (numberBits < 0 || numberBits > 8)
			throw new RuntimeException("numberBits parameter should be between 0 and 8 but was " + numberBits);
		return (byte) ((data & 0xff) >> (8 - numberBits));
	}

    public static byte getLSBValue(byte data, int numberBits) {
        if (numberBits < 0 || numberBits > 8)
			throw new RuntimeException("numberBits parameter should be between 0 and 8 but was " + numberBits);
		int mask = 0xff >> (8 - numberBits);
		return (byte) (data & mask);
	}

	public static String bytesToHex(byte[] bytes) {
		final char[] hexArray = "0123456789abcdef".toCharArray();
		if (bytes == null) {
			return "null";
		} else {
			char[] hexChars = new char[bytes.length * 2];
			for (int j = 0; j < bytes.length; j++) {
				int v = bytes[j] & 0xFF;
				hexChars[j * 2] = hexArray[v >> 4];
				hexChars[j * 2 + 1] = hexArray[v & 0x0F];
			}
			return new String(hexChars);
		}
	}

}
