package com.archery.tessa.homescreen.models;

import java.util.Date;
import java.util.List;

/**
 * Created by mkkvj on 25.1.2018.
 */

public class MeasuredDataSet {
    private long timestamp;
    private int counter;
    private List<SensorData> sensors;


    public List<SensorData> getSensors(){return this.sensors;}
    public long getTimestamp(){return this.timestamp;}

    public int getCounter() {
        return counter;
    }

    public void setTimestamp(int timestamp){this.timestamp=timestamp;}
    public void setSensors(List<SensorData> sensors){this.sensors=sensors;}


    public SensorData getSensorData(int indx) {
        if(indx < sensors.size())
            return this.sensors.get(indx);
        else
            //@TODO Change sensordatas id to int
            return new SensorData(indx, 0);
    }


    public String toString(){
        String result=new String("");
        for(SensorData data: sensors){
            result += data.toString() + "\n";
        }
        return result;
    }
}
