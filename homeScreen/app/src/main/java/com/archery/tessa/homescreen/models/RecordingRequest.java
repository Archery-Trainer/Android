package com.archery.tessa.homescreen.models;


/**
 * Request that is sent to the server when the user wants it to start recording the MQTT-channel
 */
public class RecordingRequest {
    private String archerEmail;
    private long timestamp;

    public RecordingRequest(String _archerEmail, long _timestamp) {
        archerEmail = _archerEmail;
        timestamp = _timestamp;
    }

    public String getArcherEmail() {
        return archerEmail;
    }

    public void setArcherEmail(String archerEmail) {
        this.archerEmail = archerEmail;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String toString() {
        return "Request for " + archerEmail + ", time " + timestamp;
    }
}
