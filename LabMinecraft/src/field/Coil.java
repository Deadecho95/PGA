package field;

import core.BoolDataPoint;
/**
 * This class represents a ModBusRegister holding a boolean value
 *
 * @author Spano Amara
 * @author Mayor Vincent
 * @author Chenaux Johan
 */
public class Coil extends ModbusRegister {
	
	/********************* ATTRIBUTES *******************/
	private BoolDataPoint valueDP;

	/******************* CONSTRUCTORS ********************/
	/****************************************************
	 *constructor to create a coil
	 *
	 *@param rtuAddress is the plc's address in minecraft
	 *@param regAddress is the value's address in the plc
	 *@param value to send/receive to/from minecraft
	 *@param label is the name of the value send/reveice
	 ****************************************************/
	public Coil(int rtuAddress, int regAddress, boolean value, String label) {
		super(rtuAddress,regAddress);
		this.valueDP = new BoolDataPoint(label, false, value);
	}
	
	/****************************************************
	 *constructor to create a coil
	 *
	 *@param rtuAddress is the plc's address in minecraft
	 *@param regAddress is the value's address in the plc
	 *@param label is the name of the value send/reveice
	 ****************************************************/
	public Coil(int rtuAddress, int regAddress, String label) {
		super(rtuAddress,regAddress);
		this.valueDP = new BoolDataPoint(label, false);
	}
	
	/******************** METHODS ************************/
	
	/****************************************************
	 *Write boolean value to register
	 * 
	 * @param newValue is new value to write
	 ****************************************************/
	public void write(boolean newValue) {
		valueDP.setValue(newValue);
	}
	
	/****************************************************
	 *Read binary value from register

	 * @return boolean register's value
	 ****************************************************/
	public boolean read(){
		return valueDP.getValue();
	}
	
	/****************************************************
	 * Read coil name
	 * 
	 * @return String register's name
	 ****************************************************/
	public String readName(){
		return valueDP.getlabel();
	}

}
