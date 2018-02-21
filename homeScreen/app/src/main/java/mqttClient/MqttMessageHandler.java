package mqttClient;

import android.content.Context;

import com.amazonaws.services.iot.client.AWSIotException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MqttMessageHandler {

	private static List<String> messages;
	private static final int MAX_NUM_MESSAGES = 2000;
	private MqttClient client;

	public MqttMessageHandler(Context context) {
		messages = new LinkedList<>();

		//Callback that will be called when a message arrives
		OnMessageCallback cb = new AddToCollectionCallback(messages);

		System.out.println("Creating MQTT-client");
		client = new MqttClient(cb, context);
	}


	public void disconnect() {
		try {
			client.disconnect();
			client = null;
		} catch (AWSIotException e) {
			System.out.println("Unable to disconnect from AWS IOT");
			e.printStackTrace();
		}
	}

	public void addMsg(String msg){messages.add(msg);}
	public static String getNewestMessage() {

		int sz = messages.size();
		System.out.println("getNewestMsg: "+sz);

		if(sz != 0) {

			String res = messages.get(sz - 1);

			//List should be cleared sometimes.
			//Maybe after saving or discarding sessions
			if(sz > MAX_NUM_MESSAGES) {
				//Removing with iterator is O(1)
				Iterator<String> it = messages.listIterator(0);
				it.remove();
			}

			return res;
		}
		else
			return "";

	}

	public void cleanUp(){
		messages.clear();
	}
}
