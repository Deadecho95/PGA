package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Random;


/**
 * This class contains some useful Java methods to manipulate buffered data.
 * 
 * @author Patrice RUDAZ
 */

public class Utility {
	
	/** Default size for the TCP input stream */
	public static final int	TCP_BUFFER_SIZE 	= 4096;
	
	/** Object to get some random values... */
	public static Random rnd = new Random(1);

	/**
	 * Utility method to convert a byte array in a string made up of hex (0,.. 9, a,..f)
	 * 
	 * @param b The array of byte to convert in HEX string.
	 * @return A String representing the HEX value of each byte of the array.
	 */
	public static String getHexString( byte[] b ) throws Exception 
	{
		return getHexString( b, 0, b.length );
	}
	
	/**
	 * Utility method to convert a byte array in a string made up of hex (0,.. 9, a,..f)
	 * 
	 * @param b			The byte array to convert in HEX string
	 * @param offset	The index where we start to convert from.
	 * @param length	The amount of bytes to convert in HEX string
	 * @return A String representing the HEX values of the selected bytes of the array.
	 */
	public static String getHexString( byte[] b, int offset, int length ) {
		  String result = "";
		  for ( int i = offset ; i < offset+length ; i++ ) {
		    result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16 ).substring( 1 ) ;
		  }
		  return result;
	}


	/**
	 * Retrieves a random value rounded to 2 decimal...
	 * 
	 * @param factor A coefficient which the random value is multiplied with.
	 * @return A random value rounded to 2 decimal converted to a String.
	 */
	public static String getStringRndVal( int factor ) {
		float val = (float) rnd.nextDouble()*factor*10;
		return String.format("%.2f", val ).replace( ",", "." );
	}
	

	/**
	 * Reads the incoming data from an input stream as long as there is 
	 * something to read and saved the data in an array of bytes.
	 * 
	 * The method is blocking !
	 * This method blocks until input data is available, end of file is detected,
	 * or an exception is thrown.
	 * 
	 * If the length of of read data is zero, then no bytes are read and an empty 
	 * array of bytes is returned; otherwise, there is an attempt to read at least
	 * one byte. If no byte is available because the stream is at the end of the
	 * file, the value -1 is returned; otherwise, at least one byte is read and
	 * returned as an array of bytes. 
	 * 
	 * @param  in	The input Stream where to read the data coming from.
	 * @return The read data as an <code>array of bytes</code>. Or null if the
	 * 		   has been closed by the peer while waiting for incoming data.
	 * @throws IOException  If the first byte cannot be read for any reason other
	 *                      than the end of the file, if the input stream has been
	 *                      closed, or if some other I/O error occurs.
	 */
	public static byte[] readBytes( InputStream in ) throws IOException 
	{
		byte[] buffer = new byte[ TCP_BUFFER_SIZE ];
		
		// Read the incoming data
		System.out.println( "[Transaction] readbytes() > Reading incoming data ... " );
		int b = in.read( buffer );
		System.out.println( "[Transaction] readbytes() > Amount of read values: " + String.valueOf( b ) );
		
		// Creates an array of bytes with the right size
		if( b > 0 ) {
			System.out.println( "[Transaction] readbytes() > Creating the array of bytes to be returned ... " );
			byte[] rBytes = new byte[b];
			System.arraycopy( buffer, 0, rBytes, 0, b );
			return rBytes;	// The received values
		} else if ( b == 0 )
			return new byte[0];
		
		return null;
	}
	

	/**
	 * Reads from the given input stream an amount of bytes and retrieves these data as
	 * an array of bytes. This method is 
	 * 
	 * The method is blocking !
	 * 
	 * @param in	The input Stream where to read the data coming from.
	 * @return	The read data as an <code>array of bytes</code>.
	 * @throws IOException  If the first byte cannot be read for any reason other
	 *                      than the end of the file, if the input stream has been
	 *                      closed, or if some other I/O error occurs.
	 */
	public static byte[] readNBytes( InputStream in, int len ) throws IOException 
	{
		byte[] buffer = new byte[ len ];

		// Read the incoming data
		int b = 0;
		while( b < len ) {
			b += in.read( buffer, b, len-b );
		}
		System.out.println( "[Transaction] readNBytes() > Amount of read values: " + String.valueOf( b ) );
		
		// Creates an array of bytes with the right size
		if( b == -1 )		return null;		// the connection has been canceled by the peer
		else if( b == 0 ) 	return new byte[0];	// empty data
		else 				return buffer;		// The received data
	}
	
	/**
	 * Reads a line of text. A line is considered to be terminated by any one of a 
	 * line feed ('\n'), a carriage return ('\r'), or a carriage return followed 
	 * immediately by a line feed.
	 * 
	 * @param in The Input Stream to read from.
	 * @return An array of bytes containing the contents of the line, not including any 
	 * 	       line-termination characters, or null if the end of the stream has 
	 *         been reached.
	 * @throws IOException	If an I/O error occurs
	 */
	public static byte[] readLine( InputStream in ) throws IOException 
	{
		BufferedReader reader = new BufferedReader( new InputStreamReader( in ) );
	
		// Read a complete line with Cariage Return and/or Line Feed
		String line = reader.readLine();
		
		// Handle the value to return
		if( line != null )
			return line.replace( "\n", "" ).replace( "\r", "" ).getBytes();
		else
			return null;
	}

	
	/**
	 * Converts an unsigned byte to a signed integer.
	 * 
	 * @param from an unsigned byte to convert to a signed integer
	 * @return int a signed integer
	 */
	public static int unsignedByteToSignedInt( byte from )
	{
		return 0x000000FF & (int)from;
	}

	/**
	 * Send the data contained in the given array of bytes through the output
	 * stream. It raises an IOException if something goes wrong. 
	 * The method returns the amount of bytes sent.
	 * 
	 * @param out		The Output Stream to send the data to.
	 * @param toSend	The data to send
	 * @throws IOException	If an I/O error occurs
	 */
	public static void writeLine( OutputStream out, byte[] toSend ) throws IOException {
		int size = toSend.length + 2;
		byte tmp[] = new byte[size];

		System.arraycopy(toSend, 0, tmp, 0, toSend.length);
		tmp[size- 2] = (byte) '\r';
		tmp[size- 1] = (byte) '\n';
		
		out.write( tmp );
		out.flush();
	}

	
	/**
	 * To wait some times ...
	 * 
	 * @param ms The milliseconds to wait...
	 */
	public static void waitSomeTime( int ms ) 
	{
		try {
			Thread.sleep( ms );
		} catch( InterruptedException e ) {
			System.out.println( "[Utility] waitSomeTime() > Exception : " + e.getMessage() );
		}
	}
}
