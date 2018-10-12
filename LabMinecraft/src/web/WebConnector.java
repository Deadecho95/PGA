package web;


/**
 * This class allows access to a web page
 *
 * @author Spano Amara
 * @author Mayor Vincent
 * @author Chenaux Johan
 */
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import connection.Connection;
import core.BoolDataPoint;
import core.DataPoint;
import core.FloatDataPoint;
import field.Coil;
import field.FieldConnector;

public class WebConnector extends WebSocketServer implements Connection  {

	/********************* ATTRIBUTES *******************/
	private static WebConnector instance = null;
	private ArrayList<WebSocket> wsArray;
	 
	


	
	/******************* CONSTRUCTORS ********************/
	

	/****************************************************
	 *private constructor for singleton
	 * @throws UnknownHostException 
	 ****************************************************/
	private WebConnector() throws UnknownHostException{
		super(new InetSocketAddress("localhost", 8888));
		this.start();
		wsArray = new ArrayList<>();
		
	}
	
			


	
	/******************** METHODS ************************/
	
	/****************************************************
	 *Singleton function to create the one and only instance
	 *@return WebConnector instance
	 ****************************************************/
	public static WebConnector getInstance() {
		
		// If no instance exist, create one
		if (instance == null) {
			try {
				instance = new WebConnector();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// return the one and only instance
		return instance;
	}

	/****************************************************
	 * Updates web page according to new data
	 * 
	 * @param data is the new data sent by the field
	 ****************************************************/
	@Override
	public void onNewValue(DataPoint data) {	
		
		// cast to acces child function
		if(data instanceof FloatDataPoint)
		{
			FloatDataPoint fdp = (FloatDataPoint)data;
			pushToWebPage(data.getlabel(), fdp.getValue());
		}
		else if (data instanceof BoolDataPoint)
		{
			BoolDataPoint bdp = (BoolDataPoint)data;
			pushToWebPage(data.getlabel(),bdp.getValue());
		}
		

	}

	/****************************************************
	 *Updates web according to modified data in field
	 * 
	 *  @param label is the name of new data 
	 *  @param value of new data
	 * 
	 ****************************************************/
	private void pushToWebPage(String label, boolean value) {
		
		// compute message
		String message = label + "=" + String.valueOf(value);
		
		// send to all web client
		for(WebSocket ws : wsArray) {
			ws.send(message);
		}
		
		System.out.println("	DEBUG: New data on web");
		System.out.println("	DEBUG: Label: " + label);
		System.out.println("	DEBUG: Value: " + value + "\r\n");
		

	}

	/****************************************************
	 *Updates web according to modified data in field
	 * 
	 *  @param label is the name of new data 
	 *  @param value of new data
	 ****************************************************/
	private void pushToWebPage(String label, float value) {
		
		// compute message
		String message = label + "=" + String.valueOf(value);
		System.out.println("\tDEBUG: sending to webpage message : "+message);
		
		// send to all web client
		for(WebSocket ws : wsArray) {
			ws.send(message);
			ws.send("Welcome to LabMinecraft monitoring web page");
																
		}
		
		System.out.println("	DEBUG: New data on web");
		System.out.println("	DEBUG: Label: " + label);
		System.out.println("	DEBUG: Value: " + value + "\r\n");
	}
	@Override
	public void onClose(WebSocket arg0, int arg1, String arg2, boolean arg3) {
		
		wsArray.remove(arg0);
	}
	@Override
	public void onError(WebSocket arg0, Exception arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMessage(WebSocket arg0, String arg1) {
		String[] message = arg1.split("=");
		boolean val = (message[1].contains("true"))? true : false;
		
		// update field
		System.out.println("	DEBUG: New data from web --> Label: "+message[0]+" and value: "+ message[1]);
		FieldConnector.getInstance().onNewValue(new BoolDataPoint(message[0], true, val));
		
	}
	@Override
	public void onOpen(WebSocket arg0, ClientHandshake arg1) {
		
		Coil[] fieldCoils = FieldConnector.getInstance().readAllCoils();
		
		this.wsArray.add(arg0);
		
		// update all coil on connection
		for(Coil coil: fieldCoils) {
			pushToWebPage(coil.readName(), coil.read());
		}
	}
}
