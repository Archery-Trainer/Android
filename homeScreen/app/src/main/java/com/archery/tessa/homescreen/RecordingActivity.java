package com.archery.tessa.homescreen;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.archery.tessa.homescreen.models.MeasuredDataSet;
import com.archery.tessa.homescreen.models.RecordingRequest;
import com.archery.tessa.homescreen.tasks.StartRecordingTask;
import com.archery.tessa.homescreen.tasks.StopRecordingTask;
import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import mqttClient.MqttClient;
import mqttClient.OnMessageCallback;

import static org.springframework.http.HttpMethod.HEAD;


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
    private int count=0;

    private List<MeasuredDataSet> measuredDataPoints;
    private Gson gson;
    private OurView surfaceView;
    private Bitmap archerPic;
    private Bitmap muscle1;

    private CheckBox ckBox1;
    private CheckBox ckBox2;
    private CheckBox ckBox3;
    private CheckBox ckBox4;
    private CheckBox ckBox5;
    private CheckBox ckBox6;

    private static final String TAG = "RecordingActivity";

    private Context context;


    public static int max1, max2, max3, max4, max5, max6 = 0;
    //MqttMessageHandler msgHandler;
    //private static Random rand = new Random();
    private static List<String> messages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording_activity);

        context = this;

        //Initialize the 'recording' switch
        setRecordingSwitch();

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inMutable=true;
        surfaceView = (OurView)findViewById(R.id.archerSurfaceView);

        //pics.add(BitmapFactory.decodeResource(getResources(),R.drawable.archer_ind_right_2_2_18v3,options));
        archerPic = BitmapFactory.decodeResource(getResources(),R.drawable.archer_ind_right_2_2_18v3,options);
        //System.out.println("checking"+archerPic.isMutable());

        surfaceView.setdPicsForDrawing(archerPic);
        graphView = (GraphView) findViewById(R.id.graph);
        surfaceView.setdPicsForDrawing(BitmapFactory.decodeResource(getResources(),R.drawable.archer_left_triceps_crop_177_166,options));
        surfaceView.setdPicsForDrawing(BitmapFactory.decodeResource(getResources(),R.drawable.archer_left_delt_crop_231_131,options));
        surfaceView.setdPicsForDrawing(BitmapFactory.decodeResource(getResources(),R.drawable.archer_left_trapez_crop_292_105,options));

        surfaceView.setdPicsForDrawing(BitmapFactory.decodeResource(getResources(),R.drawable.archer_right_triceps_crop_414_142,options));
        surfaceView.setdPicsForDrawing(BitmapFactory.decodeResource(getResources(),R.drawable.archer_right_delt_crop_372_172,options));
        surfaceView.setdPicsForDrawing(BitmapFactory.decodeResource(getResources(),R.drawable.archer_right_trapez_crop_338_107,options));



        measuredDataPoints = new LinkedList<>();

        mSeries1=new LineGraphSeries<>();
        mSeries1.setColor(Color.DKGRAY);
        mSeries1.setDrawAsPath(true);
        mSeries1.setThickness(3);


        mSeries2 = new LineGraphSeries<>();
        mSeries2.setColor(Color.MAGENTA);
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



//        graphView.addSeries(mSeries1);
//
//        graphView.addSeries(mSeries2);
//        graphView.addSeries(mSeries3);
//        graphView.addSeries(mSeries4);
//        graphView.addSeries(mSeries5);
//        graphView.addSeries(mSeries6);


        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(50);


        //LinkedList<Bitmap> pics =new LinkedList<>();

        /** Creating bitmap from archer picture**/



        Viewport viewport = graphView.getViewport();
        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(0);
        viewport.setMaxX(100);
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(800);
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

        //super.onResume();
        /** Checkboxes for graphs**/
        ckBox1=(CheckBox)findViewById(R.id.chkBox1);
        ckBox2=(CheckBox)findViewById(R.id.chkBox2);
        ckBox3=(CheckBox)findViewById(R.id.chkBox3);
        ckBox4=(CheckBox)findViewById(R.id.chkBox4);
        ckBox5=(CheckBox)findViewById(R.id.chkBox5);
        ckBox6=(CheckBox)findViewById(R.id.chkBox6);

        ckBox1.setChecked(true);
        ckBox2.setChecked(true);
        ckBox3.setChecked(true);
        ckBox4.setChecked(true);
        ckBox5.setChecked(true);
        ckBox6.setChecked(true);

        graphView.addSeries(mSeries1);

        graphView.addSeries(mSeries2);
        graphView.addSeries(mSeries3);
        graphView.addSeries(mSeries4);
        graphView.addSeries(mSeries5);
        graphView.addSeries(mSeries6);



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
                System.out.println(measData.getTimestamp());
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
                        count++;

                        int currentTime=measData.getTimestamp()-startTimestmp;
                        if (sensor1 > max1) {
                            textvmax.setText(Integer.toString(max1));
                            max1 = sensor1;
                        }
                        txtv1.setText(Integer.toString(sensor1));
                        // append data to graph
                        mSeries1.appendData(new DataPoint(count,sensor1),true,50);
                        System.out.println("appending data");
                        if (sensor2 > max2) {
                            textvmax2.setText(Integer.toString(max2));
                            max2 = sensor2;
                        }
                        txtv2.setText(Integer.toString(sensor2));
                        mSeries2.appendData(new DataPoint(count,sensor2),true,50);

                        if(sensor3 > max3) {
                            textvmax3.setText(Integer.toString(max3));
                            max3 = sensor3;
                        }
                        txtv3.setText(Integer.toString(sensor3));
                        mSeries3.appendData(new DataPoint(count,sensor3),true,50);


                        if(sensor4 > max4) {
                            textvmax4.setText(Integer.toString(max4));
                            max4 = sensor4;
                        }
                        txtv4.setText(Integer.toString(sensor4));
                        mSeries4.appendData(new DataPoint(count,sensor4),true,50);

                        if(sensor5> max5) {
                            textvmax5.setText(Integer.toString(max5));
                            max5 = sensor5;
                        }
                        txtv5.setText(Integer.toString(sensor5));
                        mSeries5.appendData(new DataPoint(count,sensor5),true,50);

                        if(sensor6 > max6) {
                            textvmax6.setText(Integer.toString(max6));
                            max6 = sensor6;
                        }
                        txtv6.setText(Integer.toString(sensor6));
                        mSeries6.appendData(new DataPoint(count,sensor6),true,50);

                        //update colors on muscle tension surfaceview
                        surfaceView.updateSurface(measData);

                    }
                });
            }
        }).start();
        //new Thread(runnable).start();


    }//end call

    /**
     * Create a click listener for the recording switch
     *
     * @TODO: Seems that clicking the switch blocks the thread and freezes the app when server is offline
     */
    private void setRecordingSwitch() {
        Switch recordingSwitch = (Switch) findViewById(R.id.switch1);

        if (recordingSwitch == null) {
            System.out.println("Couldn't find recording switch!");
            return;
        }

        recordingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked) {

                    //@TODO: get current archer's email
                    RecordingRequest req = new RecordingRequest("test@test.com", new Date().getTime());

                    StartRecordingTask task = new StartRecordingTask(req);
                    task.execute(context);
 /*//Don't care if recording works

                    HttpStatus response = HttpStatus.BAD_REQUEST;
                    try {
                        response = task.execute(context).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    if(response == HttpStatus.OK)
                        System.out.println("Successfully started recording");
                        //@TODO: Show error message
                    else
                        System.out.println("Got status " + response.toString() + " when starting recoding");
*/
                }

                else {
                    StopRecordingTask task = new StopRecordingTask();
                    task.execute(context);
 /*//Don't care if recording works

                    HttpStatus response = HttpStatus.BAD_REQUEST;

                    try {
                        response = task.execute(context).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    if(response == HttpStatus.OK)
                        System.out.println("Successfully stopped recording");
                    else
                        //@TODO: show error message
                        System.out.println("Got status " + response.toString() + " when stopping recoding");
                */
                }


            }
        });
    }

    public void onChkClicked(View view){
        boolean checked=((CheckBox)view).isChecked();
        switch(view.getId()){
            case R.id.chkBox1:
                if(ckBox1.isChecked()){graphView.addSeries(mSeries1);}
                else{graphView.removeSeries(mSeries1);}
                Toast.makeText(context,"Checkbox1 checked"+ckBox1.isChecked(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.chkBox2:
                if(checked){graphView.addSeries(mSeries2);}
                else{graphView.removeSeries(mSeries2);}
                break;
            case R.id.chkBox3:
                if(checked){graphView.addSeries(mSeries3);}
                else{graphView.removeSeries(mSeries3);}
                break;
            case R.id.chkBox4:
                if(checked){graphView.addSeries(mSeries4);}
                else{graphView.removeSeries(mSeries4);}
                    break;
            case R.id.chkBox5:
                if(checked){graphView.addSeries(mSeries5);}
                else{graphView.removeSeries(mSeries5);}
                    break;
            case R.id.chkBox6:
                if(checked){graphView.addSeries(mSeries6);}
                else{graphView.removeSeries(mSeries6);}
                    break;
        }
    }

}// end sessionactivity
