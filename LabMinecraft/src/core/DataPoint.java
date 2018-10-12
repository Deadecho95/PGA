package core;
import java.util.HashMap;

import database.DataBaseConnector;
import field.FieldConnector;
import web.WebConnector;
/**
 *<pre>
 *a Datapoint represents a data with a value and a label
 *all new datapoints are stored in a map
 *</pre>
 *
 * @author Spano Amara
 * @author Mayor Vincent
 * @author Chenaux Johan
 */
public class DataPoint {

	/********************* ATTRIBUTES *******************/
	public static HashMap<String, DataPoint> pointlist = new HashMap<String, DataPoint>();
	protected String label;
	protected boolean isInput;
	
	
	/******************* CONSTRUCTORS ********************/
	/****************************************************
	 *constructor to create a datapoint
	 * 
	 *@param label a name for the datapoint
	 *@param isInput to know if the datapoint is an input according to the field
	 ****************************************************/
	public DataPoint(String label, boolean isInput) {
		this.label = label;
		this.isInput = isInput;
		DataPoint.pointlist.put(label,this);
	}
	
	

	/******************** METHODS ************************/

	/****************************************************
	 *Get label from this DataPoint
	 * 
	 * @return String label of the datapoint
	 ****************************************************/
	public String getlabel(){
	
		return label;
	}
	
	
	/****************************************************
	 * get a datapoint form the list according to its label
	 * 
	 *@param label is the name of the DataPoint returned
	 *  
	 *@return chosen Datapoint
	 ****************************************************/
	public DataPoint getDataPointFromLabel(String label) {
		DataPoint getData;
		getData = pointlist.get(label);
		return getData;	
	}
	
	
	/****************************************************
	 *check if the datapoint is an input
	 * 
	 *  
	 *@return true if input
	 ****************************************************/
	public boolean getIfInput() {
		return isInput;
		
	}

	/****************************************************
	 * Updates value everywhere
	 * 
	 ****************************************************/
	protected void updateValue() {
		
		// push to field only if input
		if(isInput) {
			FieldConnector.getInstance().onNewValue(this);	
		}
		DataBaseConnector.getInstance().onNewValue(this);
		WebConnector.getInstance().onNewValue(this);
	}

	
	
	
	


}
