package database;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

import connection.Connection;
import core.BoolDataPoint;
import core.DataPoint;
import core.FloatDataPoint;
/**
 * This class allows access to an database on the web
 *
 * @author Spano Amara
 * @author Mayor Vincent
 * @author Chenaux Johan
 */
public class DataBaseConnector implements Connection {

	/********************* ATTRIBUTES *******************/
	private static DataBaseConnector instance = null;

	/******************* CONSTRUCTORS ********************/
	/****************************************************
	 *private constructor for singleton
	 ****************************************************/
	private DataBaseConnector() {

	}

	/******************** METHODS ************************/

	/****************************************************
	 *Singleton function to create the one and only instance
	 * @return DataBaseConnector instance
	 ****************************************************/
	public static DataBaseConnector getInstance() {
		// If no instance exist, create one
		if (instance == null) {
			instance = new DataBaseConnector();
		}
		// return the one and only instance
		return instance;
	}

	/******************** METHODS ************************/


	/****************************************************
	 *constructor to create a datapoint with default value
	 * 
	 *@param data is new value to push in database
	 ****************************************************/
	@Override
	public void onNewValue(DataPoint data) {

		// cast to access child function
		if (data instanceof FloatDataPoint) {
			FloatDataPoint fdp = (FloatDataPoint) data;
			pushToDB(data.getlabel(), fdp.getValue());
		} else if (data instanceof BoolDataPoint) {
			BoolDataPoint bdp = (BoolDataPoint) data;
			pushToDB(data.getlabel(), bdp.getValue());
		}

	}

	/****************************************************
	 *Updates the database according to modified data from field
	 * 
	 * @param label is the name of new data 	
	 * @param value of new data

	 ****************************************************/
	private void pushToDB(String label, boolean value) {
		
		String urlString = "http://vlesdi.hevs.ch:8086/write?db=SIn03";
		URL url;
		HttpURLConnection HTTPsocket;
		OutputStream outStream;
		InputStream inStream;
		OutputStreamWriter outStreamWriter;
		InputStreamReader inStreamReader;
		
		String toEncode = "SIn03:0a9bab50a292656afd9881bf9310513f";
		byte[] byteUTF8;
		String stringEncoded;
		String bodyMsg = label + " value=" + value;

			
			try {
				
				// Create url and connection for database
				url = new URL(urlString);
			
				// create connection
				HTTPsocket = (HttpURLConnection) url.openConnection();
				
				//  indicate we want to post to server
				HTTPsocket.setRequestMethod("POST");
				
				HTTPsocket.setDoOutput(true);			// indicate we have a message body

		
		
				// encode headers
				byteUTF8 = toEncode.getBytes("UTF-8");
				stringEncoded = Base64.getEncoder().encodeToString(byteUTF8);
				HTTPsocket.addRequestProperty("Authorization", "Basic " + stringEncoded);
				HTTPsocket.addRequestProperty("Content-Type", "binary/octet-stream");
				
				// Create output stream
				outStream = HTTPsocket.getOutputStream();
				outStreamWriter = new OutputStreamWriter(outStream);
			
				// send
				outStreamWriter.write(bodyMsg);
				outStreamWriter.flush();
				
				// Create input stream
				inStream = HTTPsocket.getInputStream();
				inStreamReader = new InputStreamReader(inStream);
				
				// read data base response
	            BufferedReader input   = new BufferedReader (inStreamReader);
	            System.out.println("[receive frome server]" + input.readLine());
	            String line;
	            while ((line = input.readLine()) != null) {
	                System.out.println(line);
	            }
		            
	             // verify response
		                
                // close connection
                
		                
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		System.out.println("	DEBUG: New data pushed in database");
		System.out.println("	DEBUG: Label: " + label);
		System.out.println("	DEBUG: Value: " + value + "\r\n");
		
		
	}

	/****************************************************
	 * \ Updates the database according to modified data from field
	 * 
	 * @param label is name of new data value 
	 * @param value of new data
	 ****************************************************/
	private void pushToDB(String label, float value) {

		String urlString = "http://vlesdi.hevs.ch:8086/write?db=SIn03";
		URL url;
		HttpURLConnection HTTPsocket;
		OutputStream outStream;
		InputStream inStream;
		OutputStreamWriter outStreamWriter;
		InputStreamReader inStreamReader;
		
		String toEncode = "SIn03:0a9bab50a292656afd9881bf9310513f";
		byte[] byteUTF8;
		String stringEncoded;
		String bodyMsg = label + " value=" + value;

			
			try {
				
				// Create url and connection for database
				url = new URL(urlString);
			
				// create connection
				HTTPsocket = (HttpURLConnection) url.openConnection();
				
				//  indicate we want to post to server
				HTTPsocket.setRequestMethod("POST");
				
				HTTPsocket.setDoOutput(true);			// indicate we have a message body

		
		
				// encode headers
				byteUTF8 = toEncode.getBytes("UTF-8");
				stringEncoded = Base64.getEncoder().encodeToString(byteUTF8);
				HTTPsocket.addRequestProperty("Authorization", "Basic " + stringEncoded);
				HTTPsocket.addRequestProperty("Content-Type", "binary/octet-stream");
				
				// Create output stream
				outStream = HTTPsocket.getOutputStream();
				outStreamWriter = new OutputStreamWriter(outStream);
			
				// send
				outStreamWriter.write(bodyMsg);
				outStreamWriter.flush();
				
				// Create input stream
				inStream = HTTPsocket.getInputStream();
				inStreamReader = new InputStreamReader(inStream);
				
				// read data base response
	            BufferedReader input   = new BufferedReader (inStreamReader);
	            System.out.println("[receive frome server] > " + input.readLine());
	            String line;
	            while ((line = input.readLine()) != null) {
	                System.out.println(line);
	            }
		            
	             // verify response
		                
                // close connection
                
		                
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		System.out.println("	DEBUG: New data pushed in database");
		System.out.println("	DEBUG: Label: " + label);
		System.out.println("	DEBUG: Value: " + value + "\r\n");

	}
	

}
