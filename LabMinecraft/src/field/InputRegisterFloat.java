package field;


import core.FloatDataPoint;
/**
 * This class represents a ModBusRegister holding a float value
 *
 * @author Spano Amara
 * @author Mayor Vincent
 * @author Chenaux Johan
 */
public class InputRegisterFloat extends ModbusRegister{
	
	/********************* ATTRIBUTES *******************/
	private FloatDataPoint valueDP;
	

	/******************* CONSTRUCTORS ********************/
	/****************************************************
	 *constructor to create a coil
	 *
	 *@param rtuAddress is the plc's address in minecraft
	 *@param regAddress is the value's address in the plc
	 *@param value to send/receive to/from minecraft
	 *@param label is the name of the value send/reveice
	 ****************************************************/
	public InputRegisterFloat(int rtuAddress, int regAddress, float value, String label) {
		super(rtuAddress,regAddress);
		this.valueDP = new FloatDataPoint(label, true,value);
	}
	/****************************************************
	 *constructor to create a coil
	 *
	 *@param rtuAddress is the plc's address in minecraft
	 *@param regAddress is the value's address in the plc
	 *@param label is the name of the value send/reveice
	 ****************************************************/
	public InputRegisterFloat(int rtuAddress, int regAddress, String label) {
		super(rtuAddress,regAddress);
		this.valueDP = new FloatDataPoint(label, true);
	}
	
	/******************** METHODS ************************/
	
	/****************************************************
	 *Read float value from register
	 * 
	 *@return float register's value
	 ****************************************************/
	public float read(){
		return valueDP.getValue();
		
	}
	
	/****************************************************
	 *Write float value to register
	 * 
	 * @param newValue is new value to write
	 ****************************************************/
	public void write(float newValue) {
		valueDP.setValue(newValue);
	}
}
