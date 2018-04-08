package com.archery.tessa.homescreen.models;

import android.view.ViewDebug;

/**
 * A single measurement from a sensor
 *
 */
public class SensorData {
    private int sensorId;
    private int value;

    public SensorData() {}

    public SensorData(int sensorId, int value){
        this.sensorId=sensorId;
        this.value=value;
    }
    public void setValue(int val){this.value=val;}
    public int getSensorId(){return this.sensorId;}
    public int getValue(){return this.value;}

    public String toString(){
        return this.sensorId + ":" + Integer.toString(this.value);

    }
}
