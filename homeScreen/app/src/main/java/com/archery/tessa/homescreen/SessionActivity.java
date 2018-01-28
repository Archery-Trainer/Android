package com.archery.tessa.homescreen;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;;
import android.util.Log;
import android.widget.TextView;

import com.archery.tessa.homescreen.models.MeasuredDataSet;
import com.archery.tessa.homescreen.models.SensorData;
import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import mqttClient.AddToCollectionCallback;
import mqttClient.MqttClient;
import mqttClient.MqttMessageHandler;
import mqttClient.OnMessageCallback;

import static mqttClient.MqttMessageHandler.getNewestMessage;


public class SessionActivity extends AppCompatActivity implements OnMessageCallback {

    private TextView txtv1;
    private TextView txtv2;
    private TextView txtv3;
    private TextView txtv4;
    private TextView txtv5;
    private TextView txtv6;
    private TextView textvmax;
    private TextView textvmax2;
    private TextView textvmax3;
    private TextView textvmax4;
    private TextView textvmax5;
    private TextView textvmax6;
    private int startTimestmp=0;
    private GraphView graphView;
    private LineGraphSeries mSeries1;
    private LineGraphSeries mSeries2;
    private List<MeasuredDataSet> measuredDataPoints;
    private Gson gson;

    private static final String TAG = "SessionActivity";
    public static int max1, max2, max3, max4, max5, max6 = 0;
    //MqttMessageHandler msgHandler;
    //private static Random rand = new Random();
    private static List<String> messages;
    //max value in views
    //final int LIMIT = 700;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_activity);
        measuredDataPoints = new LinkedList<>();
        graphView = (GraphView) findViewById(R.id.graph);
        mSeries1=new LineGraphSeries<>();
        mSeries1.setDrawAsPath(true);
        mSeries2 = new LineGraphSeries<>();

        mSeries2.setDrawAsPath(true);
        graphView.addSeries(mSeries1);
        graphView.addSeries(mSeries2);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(100);



    }

    @Override
    protected void onResume() {
        //msgHandler = new MqttMessageHandler(getApplication());
        messages = new LinkedList<>();


        //Callback that will be called when a message arrives
        //OnMessageCallback cb = new AddToCollectionCallback(messages);

        System.out.println("Creating MQTT-client");
        MqttClient c = new MqttClient(this, getApplication());
        gson = new Gson();
        /** defining textboxes for sensor data **/
        txtv1 = findViewById(R.id.sensorval1);

        txtv2 = findViewById(R.id.sensorval2);
        txtv3 = findViewById(R.id.sensorval3);
        txtv4 = findViewById(R.id.sensorval4);
        txtv5 = findViewById(R.id.sensorval5);
        txtv6 = findViewById(R.id.sensorval6);

        /** defining textboxes for sensor data max values**/
        textvmax = findViewById(R.id.sensormaxval1);
        textvmax2 = findViewById(R.id.sensormaxval2);
        textvmax3 = findViewById(R.id.sensormaxval3);
        textvmax4 = findViewById(R.id.sensormaxval4);
        textvmax5 = findViewById(R.id.sensormaxval5);
        textvmax6 = findViewById(R.id.sensormaxval6);

        super.onResume();


    }

    @Override
    public void call(final String message) {

        // a potentially  time consuming task
        //final Handler handler = new Handler();
        //Runnable runnable = new Runnable() {
        new Thread(new Runnable() {

            public void run() {
                System.out.println("added this msg: " + message);

                final MeasuredDataSet measData = gson.fromJson(message, MeasuredDataSet.class);
                measuredDataPoints.add(measData);
                // sensor values
                final int sensor1 = measData.getSensorData(0).getValue();
                final int sensor2 = measData.getSensorData(1).getValue();
                final int sensor3 = measData.getSensorData(2).getValue();
                final int sensor4 = measData.getSensorData(3).getValue();
                final int sensor5 = measData.getSensorData(4).getValue();
                final int sensor6 = measData.getSensorData(5).getValue();

                // get start time for later use
                if (startTimestmp==0){startTimestmp=measData.getTimestamp();}
//                handler.post(new Runnable() {
//                    public void run() {
                txtv1.post(new Runnable() {


                    @Override
                    public void run() {
                        int currentTime=measData.getTimestamp()-startTimestmp;
                        if (sensor1 > max1) {
                            textvmax.setText(Integer.toString(max1));
                            max1 = sensor1;
                        }
                        txtv1.setText(Integer.toString(sensor1));
                        // append data to graph
                        mSeries1.appendData(new DataPoint((double)currentTime,(double)sensor1),true,100);
                        if (sensor2 > max2) {
                            textvmax2.setText(Integer.toString(max2));
                            max2 = sensor2;
                        }
                        txtv2.setText(Integer.toString(sensor2));
                        mSeries2.appendData(new DataPoint((double)currentTime,(double)sensor2),true,100);
                        if(sensor3 > max3) {
                            textvmax3.setText(Integer.toString(max3));
                            max3 = sensor3;
                        }
                        txtv3.setText(Integer.toString(sensor3));


                        if(sensor4 > max4) {
                           textvmax4.setText(Integer.toString(max4));
                           max4 = sensor4;
                        }
                        txtv4.setText(Integer.toString(sensor4));

                        if(sensor5> max5) {
                           textvmax5.setText(Integer.toString(max5));
                            max5 = sensor5;
                        }
                        txtv5.setText(Integer.toString(sensor5));

                        if(sensor6 > max6) {
                            textvmax6.setText(Integer.toString(max6));
                            max6 = sensor6;
                        }
                        txtv6.setText(Integer.toString(sensor6));
                    }
                });
            }
        }).start();
        //new Thread(runnable).start();


    }//end call

}// end sessionactivity