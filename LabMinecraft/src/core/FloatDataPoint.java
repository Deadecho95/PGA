package core;
/**
 * This class create a datapoint with a value in float
 *
 * @author Spano Amara
 * @author Mayor Vincent
 * @author Chenaux Johan
 */
public class FloatDataPoint extends DataPoint{

	/********************* ATTRIBUTES *******************/
	protected float value;
	
	/******************* CONSTRUCTORS ********************/
	/****************************************************
	 *constructor to create a datapoint
	 * 
	 *@param label a name for the datapoint
	 *@param isInput to know if the datapoint is an input according to the field
	 *@param value is the value of the datapoint
	 ****************************************************/
	public FloatDataPoint(String label, boolean isInput, float value) {
		super(label, isInput);
		this.value = value;
		
	}
	/****************************************************
	 *constructor to create a datapoint with default value
	 * 
	 *@param label a name for the datapoint
	 *@param isInput to know if the datapoint is an input according to the field
	 ****************************************************/
	public FloatDataPoint(String label, boolean isInput) {
		super(label, isInput);
		this.value = 0;
		
	}
	
	/******************** METHODS ************************/
	
	/****************************************************
	 *get the value of this DataPoint
	 * 
	 *@return float value of the datapoint
	 ****************************************************/
	public float getValue() {
		
		return this.value;
	}
	
	
	/****************************************************
	 *set the value of this DataPoint
	 * 
	 * @param value replace the old value
	 * 
	 ****************************************************/
	public void setValue(float value) {
		this.value = value;
		updateValue();
	}
}
