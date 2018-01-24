package mqttClient;

import android.content.Context;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MqttMessageHandler {

	private static List<String> messages;
	private static final int MAX_NUM_MESSAGES = 2000;

	public MqttMessageHandler(Context context) {
		messages = new LinkedList<>();

		//Callback that will be called when a message arrives
		OnMessageCallback cb = new AddToCollectionCallback(messages);

		System.out.println("Creating MQTT-client");
		MqttClient c = new MqttClient(cb, context);
		//The client is not needed anymore
	}

	public static String getNewestMessage() {

		int sz = messages.size();

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

	public void cleanUp() {
		messages.clear();
	}
}
