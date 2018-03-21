package mqttClient;

/**
 * An interface for a function wrapper that will be called when the MQTT-client receives a message
 */
public interface OnMessageCallback {

    /**
     * Operation to perform when receiving a message
     * @param message   The received message
     */
    void call(final String message);
}
