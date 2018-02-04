package com.archery.tessa.homescreen.models;

import android.view.ViewDebug;

/**
 * Created by mkkvj on 25.1.2018.
 */

public class SensorData {
    private String sensorId;
    private int value;

    public SensorData(String sensorId, int value){
        this.sensorId=sensorId;
        this.value=value;
    }
    public void setValue(int val){this.value=val;}
    public String getSensorId(){return this.sensorId;}
    public int getValue(){return this.value;}

    public String toString(){
        return this.sensorId + ":" + Integer.toString(this.value);
    }
}
