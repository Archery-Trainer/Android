package mqttClient;

/**
 * An interface for a function wrapper that will be called when the MQTT-client receives a message
 */
public interface OnMessageCallback {
    void call(String message);
}
