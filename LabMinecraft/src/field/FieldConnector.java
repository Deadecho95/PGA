package field;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import connection.Connection;
import core.BoolDataPoint;
import core.DataPoint;
import core.FloatDataPoint;
import web.WebConnector;
/**
 * This class allows access to minecraft
 *
 * @author Spano Amara
 * @author Mayor Vincent
 * @author Chenaux Johan
 */
public class FieldConnector implements Connection {

	/********************* ATTRIBUTES *******************/
	private static FieldConnector instance = null;
	private HashMap<String, Coil> coilmap = new HashMap<>();
	private LinkedList<InputRegisterFloat> floatList = new LinkedList<>();
	private final static int RTU_ADDR = 1;
	private Timer pollTimer = new Timer();

	/******************* CONSTRUCTORS ********************/

	/****************************************************
	 *private constructor for singleton
	 ****************************************************/
	private FieldConnector() {

		// create float (input) registers
		System.out.println("	DEBUG: Creating float registers");
		floatList.push(new InputRegisterFloat(RTU_ADDR, 1, 0, "SOLAR_STRING0_I_FLOAT"));
		floatList.push(new InputRegisterFloat(RTU_ADDR, 9, 0, "SOLAR_STRING1_I_FLOAT"));
		floatList.push(new InputRegisterFloat(RTU_ADDR, 113, 0, "SOLAR_STRING2_I_FLOAT"));
		floatList.push(new InputRegisterFloat(RTU_ADDR, 121, 0, "SOLAR_STRING3_I_FLOAT"));
		floatList.push(new InputRegisterFloat(RTU_ADDR, 129, 0, "SOLAR_U_FLOAT"));
		floatList.push(new InputRegisterFloat(RTU_ADDR, 137, 0, "SOLAR_P_FLOAT"));
		floatList.push(new InputRegisterFloat(RTU_ADDR, 49, 0, "BATT_CHRG_FLOAT"));
		floatList.push(new InputRegisterFloat(RTU_ADDR, 57, 0, "BATT_P_FLOAT"));
		floatList.push(new InputRegisterFloat(RTU_ADDR, 65, 0, "COAL_T_FLOAT"));
		floatList.push(new InputRegisterFloat(RTU_ADDR, 73, 0, "COAL_U_FLOAT"));
		floatList.push(new InputRegisterFloat(RTU_ADDR, 145, 0, "COAL_P_FLOAT"));
		floatList.push(new InputRegisterFloat(RTU_ADDR, 89, 0, "GRID_U_FLOAT"));
		floatList.push(new InputRegisterFloat(RTU_ADDR, 97, 0, "GRID_P_FLOAT"));
		floatList.push(new InputRegisterFloat(RTU_ADDR, 105, 0, "BAR_U_FLOAT"));
		System.out.println("	DEBUG: Float registers created");

		// create coil

		System.out.println("	DEBUG: Creating coils without value");
		coilmap.put("SOLAR_SW", (new Coil(RTU_ADDR, 201, "SOLAR_SW")));
		coilmap.put("COAL_SW", (new Coil(RTU_ADDR, 209, "COAL_SW")));
		coilmap.put("BATT_SW", (new Coil(RTU_ADDR, 217, "BATT_SW")));
		coilmap.put("GRID_SW", (new Coil(RTU_ADDR, 225, "GRID_SW")));
		System.out.println("	DEBUG: 'Empty' coils created");

		// configure timer to poll field each XXX sec, with a short delay to insure field is completely created
		pollTimer.scheduleAtFixedRate(new PollTask(), 5000, 2000);
	}

	/******************** METHODS ************************/

	/****************************************************
	 *Singleton function to create the one and only instance
	 * @return FieldConnector instance
	 ****************************************************/
	public static FieldConnector getInstance() {

		// If no instance exist, create one
		if (instance == null) {
			
			instance = new FieldConnector();

			// update coils -> read first time from field
			System.out.println("	DEBUG: Reading coil on init");
			instance.coilmap.replace("SOLAR_SW",ModbusAccessor.getInstance().readCoil(instance.coilmap.get("SOLAR_SW")));
			instance.coilmap.replace("COAL_SW", ModbusAccessor.getInstance().readCoil(instance.coilmap.get("COAL_SW")));
			instance.coilmap.replace("BATT_SW", ModbusAccessor.getInstance().readCoil(instance.coilmap.get("BATT_SW")));
			instance.coilmap.replace("GRID_SW", ModbusAccessor.getInstance().readCoil(instance.coilmap.get("GRID_SW")));
			System.out.println("	DEBUG: Coils initialized");
		}

		// return the one and only instance
		return instance;
	}

	/****************************************************
	 *Updates field according to new data
	 * 
	 * @param data is new data sent by the web
	 ****************************************************/
	@Override
	public void onNewValue(DataPoint data) {

		// cast to access child function
		if (data instanceof BoolDataPoint) {
			BoolDataPoint bdp = (BoolDataPoint) data;
			coilmap.get(data.getlabel()).write(((BoolDataPoint) data).getValue());
			pushToField(data.getlabel(), bdp.getValue());
		}
//		else if (data instanceof FloatDataPoint) {
//			FloatDataPoint fdp = (FloatDataPoint)data;
//
//		} else 

	}

	/****************************************************
	 *Updates field according to modified data from web
	 * 
	 * @param label is name of new data value : value of new data
	 ****************************************************/
	private void pushToField(String label, boolean value) {

		ModbusAccessor.getInstance().writeCoil(coilmap.get(label));
		
		// Debug
		System.out.println("	DEBUG: New data pushed in field");
		System.out.println("	DEBUG: Label: " + label);
		System.out.println("	DEBUG: Value: " + value + "\r\n");
		
	}
	
	/****************************************************
	 *Returns all coil present in field

	 * @return a coil array with all coils
	 ****************************************************/
	
	public Coil[] readAllCoils() {
		Coil[] retVal = new Coil[coilmap.size()];
		int i = 0;
		
		for(HashMap.Entry<String, Coil> coil : coilmap.entrySet()) {
			retVal[i] = coil.getValue();
			i++;
		}
		
		return retVal;
	}
	
	/****************************************************
	 * Class called periodically to send informations from the field
	 * Implemented in field connector to have access to the floatlist
	 * 
	 ****************************************************/
	public class PollTask extends TimerTask {

		// Attributes
		boolean init = false;
		
		// for debug
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		
		// Constructors
		public PollTask() {
			super();
		}
		
		// Methods
		
		
		/****************************************************
		 * function to read periodically the floats in the field
		 ****************************************************/
		@Override
		public void run() {
		
			// Timestamp for debug
			System.out.println("\tDEBUG: time of polling " + dtf.format(LocalDateTime.now()));
		    
			// synchronized(floatList) {
			for (InputRegisterFloat iter : FieldConnector.getInstance().floatList) {
				iter = ModbusAccessor.getInstance().readFloat(iter);
		
			}
		}

	}
	
	/****************************************************
	*main to run complete program
	 ****************************************************/
	public static void main(String[] args) {
		FieldConnector.getInstance();
		WebConnector.getInstance();
		while(true){}
	}

}
