package field;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import utils.Utility;
/**
 * This class is used to communicate by modbus with minecraft
 *
 * @author Spano Amara
 * @author Mayor Vincent
 * @author Chenaux Johan
 */
public class ModbusAccessor {

	/********************* ATTRIBUTES *******************/
	private static ModbusAccessor instance = null;
	private Socket socket;
	private OutputStream out;
	private InputStream in;

	private final int COIL_STATUS_POS = 9;
	private final int INPUT_REGISTER_POS = 9;
	private final int WRITE_COIL = 5;
	private final int READ_FLOAT = 4;
	private final int READ_COIL = 1;
	private final int STD_PCK_LENGHT = 6;
	private final int N_READ_BOOL = 1;
	private final int N_READ_FLOAT = 2;

	/******************* CONSTRUCTORS ********************/

	/****************************************************
	 *private constructor for singleton
	 ****************************************************/
	private ModbusAccessor() {
		try {
			socket = new Socket("localhost", 1502);
			out = socket.getOutputStream();
			in = socket.getInputStream();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/******************** METHODS ************************/

	/****************************************************
	 *Singleton function to create the one and only instance
	 * @return ModbusAccessor instance
	 ****************************************************/
	public static ModbusAccessor getInstance() {

		// If no instance exist, create one
		if (instance == null) {
			instance = new ModbusAccessor();
		}

		// return the one and only instance
		return instance;
	}

	/****************************************************
	 *Read float register in minecraft
	 * 
	 * @param irf is the InputRegisterFloat to send to minecraft
	 * 
	 * @return register's float value
	 ****************************************************/
	public InputRegisterFloat readFloat(InputRegisterFloat irf) {

		byte[] returncommand;
		byte toSend[] = createPacket(0, 0, STD_PCK_LENGHT, irf.getRTUAddress(), READ_FLOAT, irf.getRegAddress(), N_READ_FLOAT);
		
		try {

			// request
			out.write(toSend);
			out.flush();
			
			// response
			returncommand = Utility.readBytes(in);
			
			irf.write(readFloatPacket(returncommand));
			System.out.println("	DEBUG: Returned float register value: " + irf.read() +"\r\n");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return irf;
	}

	/****************************************************
	 *Read bool register in minecraft
	 * 
	 * @param coil is sent to minecraft
	 * 
	 * @return coil's value
	 ****************************************************/
	public Coil readCoil(Coil coil) {

		byte[] returncommand;
		byte toSend[] = createPacket(0, 0, STD_PCK_LENGHT, coil.getRTUAddress(), READ_COIL,coil.getRegAddress(), N_READ_BOOL);

		try {

			// request
			out.write(toSend);
			out.flush();
			
			// response
			returncommand = Utility.readBytes(in);
			
			// Debug
			System.out.println("	DEBUG: Reading coil " + coil.readName() +"\r\n");
			coil.write(readCoilPacket(returncommand));
			//System.out.println("Returned coil value: " + coil.read());

		} catch (IOException e) {
			e.printStackTrace();
		}

		return coil;
	}

	/****************************************************
	 * Write binary register in minecraft
	 * 
	 * @param coil is sent to minecraft
	 ****************************************************/
	public void writeCoil(Coil coil) {
		
		byte toSend[] = createPacket(0, 0, STD_PCK_LENGHT, coil.getRTUAddress(), WRITE_COIL,coil.getRegAddress(), (coil.read() == true)? 0xFF00 : 0x0000);
		byte[] returncommand;
		boolean retVal = false;
		
		try {
			// request
			out.write(toSend);
			out.flush();
			
			// response
			returncommand = Utility.readBytes(in);
			retVal = readCoilPacket(returncommand);
			System.out.println("	DEBUG: Write coil " + coil.readName() +"\r\n");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/****************************************************
	 *Read packet (byte array) and returns float value (from an input register)
	 * 
	 * @param packet is a byteArray that contains the desired value
	 * 
	 * @return float value
	 ****************************************************/
	private float readFloatPacket(byte[] packet) {

		float retValue;
		ByteBuffer bb = ByteBuffer.wrap(packet);
		retValue = bb.getFloat(INPUT_REGISTER_POS);
		return retValue;

	}

	/****************************************************
	 * Read packet (byte array) and returns bool value (from a coil)
	 * 
	 * param: packet
	 * 
	 * return : bool value
	 ****************************************************/
	private boolean readCoilPacket(byte[] packet) {

		boolean retValue;
		ByteBuffer bb = ByteBuffer.wrap(packet);
		retValue = (bb.get(COIL_STATUS_POS) == 0) ? false : true;
		return retValue;

	}

	/****************************************************
	 * Create byte array according to modbus packet to send
	 * 
	 * @param packet is a byteArray that contains the desired value
	 * 
	 * @return byte array
	 ****************************************************/
	private byte[] createPacket(int transactionId, int protocolId, int length, int unitId, int functionCode,
			int startingAddr, int nbrOfInput) {

		byte[] byteArray;
		ByteBuffer bb = ByteBuffer.allocate(12);
		byte tempByte;
		short tempShort;

		tempShort = (short) (transactionId & 0xFFFF);
		bb.putShort(0, tempShort);

		tempShort = (short) (protocolId & 0xFFFF);
		bb.putShort(2, tempShort);

		tempShort = (short) (length & 0xFFFF);
		bb.putShort(4, tempShort);

		tempByte = (byte) (unitId & 0xFF);
		bb.put(6, tempByte);

		tempByte = (byte) (functionCode & 0xFF);
		bb.put(7, tempByte);

		tempShort = (short) (startingAddr & 0xFFFF);
		bb.putShort(8, tempShort);

		tempShort = (short) (nbrOfInput & 0xFFFF);
		bb.putShort(10, tempShort);

		byteArray = bb.array();
		return byteArray;

	}
	
//	public static void main(String[] args) {

		
//		/**
//		 * Coil test
//		 * 
//		 * write coil then read to verify both function
//		 * repeat process with another value to asses repeatability
//		 * 
//		 * check in parallel in minecraft
//		 */
		
//		boolean state = true;
//		Coil SolarCoil = new Coil(1, 201,true , "SOLAR_SW");
//		FieldConnector.getInstance();
//		
//		
//		while(true) { 
//			
//			// Write test
//			System.out.println("Set solar coil to "+ state);
//			SolarCoil.write(state);
//			ModbusAccessor.getInstance().writeCoil(SolarCoil);
//			
//			// Read test
//			ModbusAccessor.getInstance().readCoil(SolarCoil);
//			System.out.println("Acutal solar coil state is " + SolarCoil.read());
//
//			// switch coil
//			state = !state;
//			
//			Utility.waitSomeTime(3000);
//		}

//		/**
//		 * Input float register test
//		 * 
//		 * read float to verify  function
//		 * repeat process with another value to asses repeatability
//		 * 
//		 * check in parallel in minecraft
//		 */
		
//		InputRegisterFloat SolarUFloat= new InputRegisterFloat(1, 129,0, "Solar_U_Float");
//		
//		while(true) { 
//		
//			// Read test
//			ModbusAccessor.getInstance().readFloat(SolarUFloat);
//			System.out.println("Acutal solar state is " + SolarUFloat.read()*300);
//			
//			Utility.waitSomeTime(3000);
//		}

//	}
}
