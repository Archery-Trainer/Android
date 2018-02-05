package com.archery.tessa.homescreen;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.archery.tessa.homescreen.models.MeasuredDataSet;
import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.LinkedList;
import java.util.List;

import mqttClient.MqttClient;
import mqttClient.OnMessageCallback;


public class RecordingActivity extends AppCompatActivity implements OnMessageCallback {


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
    private LineGraphSeries mSeries3;
    private LineGraphSeries mSeries4;
    private LineGraphSeries mSeries5;
    private LineGraphSeries mSeries6;

    private List<MeasuredDataSet> measuredDataPoints;
    private Gson gson;
    private OurView surfaceView;
    private Bitmap archerPic;

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

        /** Creating bitmap from archer picture**/
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inMutable=true;
        archerPic = BitmapFactory.decodeResource(getResources(),R.drawable.archer_ind_right_2_2_18v3,options);
        setContentView(R.layout.recording_activity);
        surfaceView = (OurView)findViewById(R.id.archerSurfaceView);
        surfaceView.setdPicsForDrawing(archerPic);

        measuredDataPoints = new LinkedList<>();
        graphView = (GraphView) findViewById(R.id.graph);
        mSeries1=new LineGraphSeries<>();
        mSeries1.setColor(Color.GREEN);
        mSeries1.setDrawAsPath(true);
        mSeries1.setThickness(3);


        mSeries2 = new LineGraphSeries<>();
        mSeries2.setColor(Color.YELLOW);
        mSeries2.setDrawAsPath(true);
        mSeries2.setThickness(3);


        mSeries3=new LineGraphSeries<>();
        mSeries3.setColor(Color.BLUE);
        mSeries3.setDrawAsPath(true);
        mSeries3.setThickness(3);


        mSeries4 = new LineGraphSeries<>();
        mSeries4.setColor(Color.CYAN);
        mSeries4.setDrawAsPath(true);
        mSeries4.setThickness(3);


        mSeries5=new LineGraphSeries<>();
        mSeries5.setColor(Color.RED);
        mSeries5.setDrawAsPath(true);
        mSeries5.setThickness(3);


        mSeries6 = new LineGraphSeries<>();
        mSeries6.setColor(Color.BLACK);
        mSeries6.setDrawAsPath(true);
        mSeries6.setThickness(3);


        graphView.addSeries(mSeries1);
        graphView.addSeries(mSeries2);
        graphView.addSeries(mSeries3);
        graphView.addSeries(mSeries4);
        graphView.addSeries(mSeries5);
        graphView.addSeries(mSeries6);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(100);


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

//    @Override
//    protected void onResume() {
//        //msgHandler = new MqttMessageHandler(getApplication());
//        messages = new LinkedList<>();
//
//
//        //Callback that will be called when a message arrives
//        //OnMessageCallback cb = new AddToCollectionCallback(messages);
//
//        System.out.println("Creating MQTT-client");
//        MqttClient c = new MqttClient(this, getApplication());
//        gson = new Gson();
//        /** defining textboxes for sensor data **/
//        txtv1 = findViewById(R.id.sensorval1);
//
//        txtv2 = findViewById(R.id.sensorval2);
//        txtv3 = findViewById(R.id.sensorval3);
//        txtv4 = findViewById(R.id.sensorval4);
//        txtv5 = findViewById(R.id.sensorval5);
//        txtv6 = findViewById(R.id.sensorval6);
//
//        /** defining textboxes for sensor data max values**/
//        textvmax = findViewById(R.id.sensormaxval1);
//        textvmax2 = findViewById(R.id.sensormaxval2);
//        textvmax3 = findViewById(R.id.sensormaxval3);
//        textvmax4 = findViewById(R.id.sensormaxval4);
//        textvmax5 = findViewById(R.id.sensormaxval5);
//        textvmax6 = findViewById(R.id.sensormaxval6);
//
//        super.onResume();
//
//
//    }

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
                        mSeries3.appendData(new DataPoint((double)currentTime,(double)sensor3),true,100);


                        if(sensor4 > max4) {
                            textvmax4.setText(Integer.toString(max4));
                            max4 = sensor4;
                        }
                        txtv4.setText(Integer.toString(sensor4));
                        mSeries4.appendData(new DataPoint((double)currentTime,(double)sensor4),true,100);

                        if(sensor5> max5) {
                            textvmax5.setText(Integer.toString(max5));
                            max5 = sensor5;
                        }
                        txtv5.setText(Integer.toString(sensor5));
                        mSeries5.appendData(new DataPoint((double)currentTime,(double)sensor5),true,100);

                        if(sensor6 > max6) {
                            textvmax6.setText(Integer.toString(max6));
                            max6 = sensor6;
                        }
                        txtv6.setText(Integer.toString(sensor6));
                        mSeries6.appendData(new DataPoint((double)currentTime,(double)sensor6),true,100);
                    }
                });
            }
        }).start();
        //new Thread(runnable).start();


    }//end call

}// end sessionactivity
