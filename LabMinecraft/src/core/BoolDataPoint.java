package core;
/**
 * This class create a datapoint with a value in boolean
 *
 * @author Spano Amara
 * @author Mayor Vincent
 * @author Chenaux Johan
 */
public class BoolDataPoint extends DataPoint{

	/********************* ATTRIBUTES *******************/
	protected boolean value;
	
	/******************* CONSTRUCTORS ********************/
	/****************************************************
	 *constructor to create a datapoint
	 * 
	 *@param label a name for the datapoint
	 *@param isInput to know if the datapoint is an input according to the field
	 *@param value is the value of the datapoint
	 ****************************************************/
	public BoolDataPoint(String label, boolean isInput, boolean value) {
		super(label, isInput);
		this.value = value;
	}
	/****************************************************
	 *constructor to create a datapoint with default value
	 * 
	 *@param label a name for the datapoint
	 *@param isInput to know if the datapoint is an input according to the field
	 ****************************************************/
	public BoolDataPoint(String label, boolean isInput) {
		super(label, isInput);
		this.value = false;
	}


	/******************** METHODS ************************/
	
	/****************************************************
	 *get the value of this DataPoint
	 * 
	 *@return boolean value of the datapoint
	 ****************************************************/
	public boolean getValue() {
		
		return this.value;
	}
	
	
	/****************************************************
	 *set the value of this DataPoint
	 * 
	 * @param value replace the old value
	 * 
	 ****************************************************/
	public void setValue(boolean value) {
		if (value != this.value) {
			this.value = value;
			updateValue();
			
			// Debug
			System.out.println("	DEBUG: changed boolean value");
		}
		else
		{
			// Debug
			System.out.println("	DEBUG: boolean value is the same-> no change\r\n");
		}
		
	}
}
