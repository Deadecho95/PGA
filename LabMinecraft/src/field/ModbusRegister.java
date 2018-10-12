package field;
/**
 * This class is a packet that will be send to the modbus
 *
 * @author Spano Amara
 * @author Mayor Vincent
 * @author Chenaux Johan
 */
public class ModbusRegister {
	
	/********************* ATTRIBUTES *******************/
	private int rtuAddress;
	private int regAddress;

	/******************* CONSTRUCTORS ********************/
	/****************************************************
	 *constructor to create a modbusregister
	 *
	 *@param rtuAddress is the plc's address in minecraft
	 *@param regAddress is the value's address in the plc

	 ****************************************************/
	public ModbusRegister(int rtuAddress, int regAddress) {
		this.rtuAddress = rtuAddress;
		this.regAddress = regAddress;
	}
	
	/******************** METHODS ************************/
	
	/****************************************************
	 * Gets register's RTU address
	 * 
	 * @return int RTU address
	 ****************************************************/
	public int getRTUAddress() {
		return rtuAddress;
	}
	
	/****************************************************
	 *Gets register's address
	 * 
	 * @return int  register's address
	 ****************************************************/
	public int getRegAddress() {
		return regAddress;
	}
}
