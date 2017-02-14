package frc.team4330.sensors.canbus;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

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
	protected synchronized CANMessage receiveData(int messageId) throws CANMessageNotFoundException {
		
		if ( messageId < 0 ) {
			throw new RuntimeException("canMessage cannot have a negative integer for the messageId");
		}

	    // Get the data using full 29 bits for CAN message id mask
	    // Expected that this call will throw a CANMessageNotFoundException if no messages of that
	    // id are available
	    // TODO try CANJNI.CAN_IS_FRAME_11BIT
		
	    ByteBuffer dataBuffer = CANJNI.FRCNetCommCANSessionMuxReceiveMessage(
	    		ByteBuffer.allocateDirect(4).asIntBuffer().wrap(new int[] {messageId}), 
	    		CANJNI.CAN_IS_FRAME_REMOTE, ByteBuffer.allocateDirect(4));

	    // make a copy of the data from buffer since not sure if it will be changed later on
	    int size = dataBuffer.remaining();
	    byte[] data = new byte[size];
	    for ( int i = 0; i < size; i++ ) {
	    	data[i] = dataBuffer.get();
	    }

	    return new CANMessage(messageId, data);
	}
	
	/**
	 * Sends the given messageId with the given data
	 * @param canMessage messageId should not be null, but data may be null but or a 
	 * 		byte array of max length 8 since CAN only supports data packets 8 bytes in length
	 */
	protected synchronized void sendData(CANMessage canMessage) {
		if ( canMessage.messageId < 0 ) {
			throw new RuntimeException("canMessage cannot have a negative integer for the messageId");
		}
		ByteBuffer sendDataBuffer = null;
		
		byte[] data = canMessage.data;
		if (data != null) {
			int dataSize = data.length;
			if ( dataSize > 8 ) {
				throw new RuntimeException("sendData bad parameters(data too long): arrayLength=" +
						data.length + " but CAN protocol only support max of 8 bytes of data");
			}
			sendDataBuffer = ByteBuffer.allocateDirect(data.length).wrap(data);
		} else {
			sendDataBuffer = ByteBuffer.allocateDirect(0);
		}

		CANJNI.FRCNetCommCANSessionMuxSendMessage(canMessage.messageId, sendDataBuffer, CANJNI.CAN_SEND_PERIOD_NO_REPEAT);
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
