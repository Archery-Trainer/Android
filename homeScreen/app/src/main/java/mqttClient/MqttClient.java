package mqttClient;

import android.content.Context;
import android.content.res.Resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.archery.tessa.homescreen.R;


/**
 * Class for listening the MQTT topic where messages from the embedded device are published.
 *
 *
 * Add
 compile 'com.amazonaws:aws-iot-device-sdk-java:1.1.1'

 to build.cradle
 and the keystore file to src/main/assets
 */
public class MqttClient {

	private static AWSIotMqttClient awsIotClient;

	//CLIENT_ID needs to be unique for every client and during testing the connection gets messed up if I 
	//make multiple subsequent connections with the same id. So here's a hacky one-liner to generate a random string
	private static final String CLIENT_ID = Long.toHexString(Double.doubleToLongBits(Math.random()));
	private static final AWSIotQos TestTopicQos = AWSIotQos.QOS0; 	//Don't know what this is

	private static Context context;
	
	/**
	 * Initialize the MQTT-client
	 * 
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void init() 
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
	
		if (awsIotClient == null) {
			
			/*
			 * @TODO: This key store thing is pretty awkward
			 * 
			 *  Basically I ran
			 *  	openssl pkcs12 -export -in <certificate-file> -inkey <private-key-file> -out p12.keystore -name alias
			 *  
			 *  where <certificate-file> was the path to the certificate and 
			 *  <private-key-file> to the key I got from Amazon IoT.
			 *  This created a keystore file whose path is below.
			 *
			 *  On android I needed to change the key store type. See https://stackoverflow.com/questions/11117486/wrong-version-of-keystore-on-android-call
			 *
			 *  How to handle the certificates and these passwords?
			 */
			String keyStorePassword = "salasana";
			String keyPassword = "salasana";
	
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

			InputStream is = context.getAssets().open("androidkeystore");
			keyStore.load(is, keyStorePassword.toCharArray());

			//Initialize the client with our credentials
			String clientEndpoint = context.getString(R.string.MQTT_server_url);
			awsIotClient = new AWSIotMqttClient(clientEndpoint, CLIENT_ID, keyStore, keyPassword);
			
			System.out.println("Connected to " + clientEndpoint + " as " + CLIENT_ID);
			
		
       }
		
       if (awsIotClient == null) {
            throw new IllegalArgumentException("Failed to construct client due to missing certificate or credentials.");
       }

	}
	
	/**
	 * 
	 * @param c the client to set
	 */
	public static void setClient(AWSIotMqttClient c) {
		awsIotClient = c;
	}
	
	
	/**
	 * MQTT-message listener
	 */
	private class TopicListener extends AWSIotTopic {
		
		OnMessageCallback onMsg;

		
		/**
		 * Construct a TopicListener
		 * 
		 * @param topic	Topic to subscribe to
		 * @param qos	?
		 */
		public TopicListener(String topic, AWSIotQos qos, OnMessageCallback onMsgCallback) {
		    super(topic, qos);
		    onMsg = onMsgCallback;
		}

		/**
		 * This function gets called when a message is published to the topic
		 * 
		 * @param message	The message read from the topic
		 */
		@Override
		public void onMessage(AWSIotMessage message) {
			onMsg.call(message.getStringPayload());
			
		}

	}
		
	
	/**
	 * Construct client and subscribe to topic #. 
	 * 
	 */
	public MqttClient(OnMessageCallback onMsgCallback, Context c) {
		try {
			context = c;

		    init();

		    awsIotClient.connect();

		    String topicName = context.getString(R.string.MQTT_topic);

		    AWSIotTopic topic = new TopicListener(topicName, TestTopicQos, onMsgCallback);
		    awsIotClient.subscribe(topic, true);

    		System.out.println("Subscribed to topic " + topicName);

		} catch (IOException e) {
			System.out.println("Exception: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception: " + e.getClass());
		}
}
}


