package com.archery.tessa.homescreen.models;

import java.util.List;

/**
 * A set of sensor measurements
 */
public class MeasuredDataSet {
    private long timestamp;
    private List<SensorData> sensors;

    /**
     * @return The sensor objects that belong to this dataset
     */
    public List<SensorData> getSensors(){return this.sensors;}

    public long getTimestamp(){return this.timestamp;}

    public void setTimestamp(int timestamp){this.timestamp=timestamp;}


    public void setSensors(List<SensorData> sensors){this.sensors=sensors;}


    /**
     * Get the sensor data of a single sensor in this set
     * @param indx  Index of the sensor whose data to get
     * @return  The sensor data
     */
    public SensorData getSensorData(int indx) {
        if(indx < sensors.size())
            return this.sensors.get(indx);
        else
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
