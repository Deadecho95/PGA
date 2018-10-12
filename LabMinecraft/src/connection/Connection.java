package connection;

import core.DataPoint;

/**
 * This class is a interface to force all connectors to react when a new value is entered
 *
 * @author Spano Amara
 * @author Mayor Vincent
 * @author Chenaux Johan
 */
public interface Connection {

    /**
     * Get a new datapoint
     *
     * @param data a datapoint as new value

     */
	public void onNewValue(DataPoint data);
	
}
