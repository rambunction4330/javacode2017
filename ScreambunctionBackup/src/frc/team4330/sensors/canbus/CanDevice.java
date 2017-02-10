package frc.team4330.sensors.canbus;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import edu.wpi.first.wpilibj.can.CANJNI;
import edu.wpi.first.wpilibj.can.CANMessageNotFoundException;

public abstract class CanDevice {
	
	/**
	 * 
	 * @param messageId
	 * @return the can message will be returned which may have a null data part but the messageId
	 * will always be populated
	 * @throws CANMessageNotFoundException thrown if there are no CAN messages with any of
	 * the messageIds of interest in the receive queue
	 */
	protected CANMessage receiveData(int messageId) throws CANMessageNotFoundException {
		
		if ( messageId < 0 ) {
			throw new RuntimeException("canMessage cannot have a negative integer for the messageId");
		}
		
		ByteBuffer targetedMessageID = ByteBuffer.allocateDirect(4);
	    targetedMessageID.order(ByteOrder.LITTLE_ENDIAN);
	    targetedMessageID.asIntBuffer().put(0, messageId);

	    ByteBuffer timeStamp = ByteBuffer.allocateDirect(4);

	    // Get the data using full 29 bits for CAN message id mask
	    ByteBuffer dataBuffer = CANJNI.FRCNetCommCANSessionMuxReceiveMessage(
	    	targetedMessageID.asIntBuffer(), CANJNI.CAN_IS_FRAME_REMOTE, timeStamp);

	    byte[] data;
	    if ( dataBuffer == null || dataBuffer.capacity() == 0 ) {
	    	data = null;
	    } else {
	    	int size = dataBuffer.capacity();
	    	data = new byte[size];
	    	for (int i = 0; i < size; i++) {
	    		data[i] = dataBuffer.get(i);
	    	}
	    }

	    return new CANMessage(messageId, data);
	}
	
	/**
	 * Sends the given messageId with the given data
	 * @param canMessage messageId should not be null, but data may be null but or a 
	 * 		byte array of max length 8 since CAN only supports data packets 8 bytes in length
	 */
	protected void sendData(CANMessage canMessage) {
		if ( canMessage.messageId < 0 ) {
			throw new RuntimeException("canMessage cannot have a negative integer for the messageId");
		}
		ByteBuffer buffer;
		byte[] data = canMessage.data;
		if (data != null) {
			int dataSize = data.length;
			if ( dataSize > 8 ) {
				throw new RuntimeException("sendData bad parameters(data too long): arrayLength=" +
						data.length + " but CAN protocol only support max of 8 bytes of data");
			}
			buffer = ByteBuffer.allocateDirect(dataSize);
			for (byte i = 0; i < dataSize; i++) {
				buffer.put(i, data[i]);
			}
		} else {
			buffer = null;
		}

		CANJNI.FRCNetCommCANSessionMuxSendMessage(canMessage.messageId, buffer, CANJNI.CAN_SEND_PERIOD_NO_REPEAT);
	}
	
	public class CANMessage {
		int messageId;
		byte[] data;
		public CANMessage(int messageId, byte[] data) {
			this.messageId = messageId;
			this.data = data;
		}
		
		public int getMessageId() {
			return messageId;
		}

		public byte[] getData() {
			return data;
		}

		public String toString() {
			return "messageId=" + messageId + " data=" + ByteHelper.bytesToHex(data);
		}
	}

}
