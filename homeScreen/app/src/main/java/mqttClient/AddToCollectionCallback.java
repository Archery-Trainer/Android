package mqttClient;

import com.archery.tessa.homescreen.models.MeasuredDataSet;
import com.google.gson.Gson;

import java.util.Collection;

/**
 * Utility to add messages to a list when they are received. The list is provided by the caller. 
 *
 */
public class AddToCollectionCallback implements OnMessageCallback{

	private Collection<String> storage;
	
	/**
	 * Set up the utility
	 * 
	 * @param collection The storage where message are stored
	 */
	public AddToCollectionCallback(Collection<String> collection) {
		storage = collection;
	}
	
	/**
	 * Callback that will be called by the TopicListener
	 * @param message Message received from the MQTT-topic
	 */
	public void call(String message) {

		//System.out.println("added msg: "+message);
		//Gson gson= new Gson();
		//MeasuredDataSet tmp = gson.fromJson(message, MeasuredDataSet.class);
		//System.out.println("datapoint tmp " + tmp.getSensorData(0));

        //measuredDataPoints.add(tmp);
		storage.add(message);


	}

}
