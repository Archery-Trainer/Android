package com.archery.tessa.homescreen;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.view.View;
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

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import mqttClient.MqttClient;
import mqttClient.OnMessageCallback;


public class RecordingActivity extends AppCompatActivity implements OnMessageCallback {

    private TextView[] textViews;

    private TextView[] maxTextViews;

    private GraphView graphView;

    private LineGraphSeries[] mSeries;

    private int count=0;

    private List<MeasuredDataSet> measuredDataPoints;
    private Gson gson;
    private OurView surfaceView;
    private Bitmap archerPic;

    private CheckBox[] ckBoxes;

    private static final String TAG = "RecordingActivity";

    private Context context;

    private int maxVals[] = {0, 0, 0, 0, 0, 0};

    private final int NUM_SENSORS = 6;
    private final int LINE_WIDTH = 4;
    private final int MAX_DATA_POINTS = 50;


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

        int muscleColors[] = {
                R.color.colorSensor1,
                R.color.colorSensor2,
                R.color.colorSensor3,
                R.color.colorSensor4,
                R.color.colorSensor5,
                R.color.colorSensor6
        };

        /** Add point series to graph **/
        mSeries = new LineGraphSeries[NUM_SENSORS];
        for(int i = 0; i < NUM_SENSORS; i++) {
            mSeries[i] = new LineGraphSeries();
            mSeries[i].setColor(muscleColors[i]);
            mSeries[i].setDrawAsPath(true);
            mSeries[i].setThickness(LINE_WIDTH);

            graphView.addSeries(mSeries[i]);
        }

        /** defining textboxes for sensor data **/
        textViews = new TextView[]{
                findViewById(R.id.sensorval1),
                findViewById(R.id.sensorval2),
                findViewById(R.id.sensorval3),
                findViewById(R.id.sensorval4),
                findViewById(R.id.sensorval5),
                findViewById(R.id.sensorval6)
        };
        /** defining textboxes for sensor data max values**/
        maxTextViews = new TextView[] {
                findViewById(R.id.sensormaxval1),
                findViewById(R.id.sensormaxval2),
                findViewById(R.id.sensormaxval3),
                findViewById(R.id.sensormaxval4),
                findViewById(R.id.sensormaxval5),
                findViewById(R.id.sensormaxval6)
        };


        /** Checkboxes for graphs**/
        ckBoxes = new CheckBox[] {
                (CheckBox)findViewById(R.id.chkBox1),
                (CheckBox)findViewById(R.id.chkBox2),
                (CheckBox)findViewById(R.id.chkBox3),
                (CheckBox)findViewById(R.id.chkBox4),
                (CheckBox)findViewById(R.id.chkBox5),
                (CheckBox)findViewById(R.id.chkBox6)
        };

        for(CheckBox b : ckBoxes)
            b.setChecked(true);


        /** Some graph view settings **/
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(50);
        Viewport viewport = graphView.getViewport();
        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(0);
        viewport.setMaxX(100);
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(800);


        System.out.println("Creating MQTT-client");
        MqttClient c = new MqttClient(this, getApplication());

        gson = new Gson();
    }



    @Override
    public void call(final String message) {

        new Thread(new Runnable() {

            public void run() {
                System.out.println("added this msg: " + message);

                final MeasuredDataSet measData = gson.fromJson(message, MeasuredDataSet.class);
                measuredDataPoints.add(measData);
                System.out.println(measData.getTimestamp());
                // sensor values

                final int sensorVals[] = {
                        measData.getSensorData(0).getValue(),
                        measData.getSensorData(1).getValue(),
                        measData.getSensorData(2).getValue(),
                        measData.getSensorData(3).getValue(),
                        measData.getSensorData(4).getValue(),
                        measData.getSensorData(5).getValue()
                };


                textViews[0].post(new Runnable() {

                    //Update ui elements when receiving message
                    @Override
                    public void run() {
                        count++;

                        for(int i = 0; i < NUM_SENSORS; i++) {

                            //Update text views
                            int val = sensorVals[i];
                            textViews[i].setText(Integer.toString(val));

                            int maxVal = maxVals[i];
                            if(val > maxVal) {
                                maxTextViews[i].setText(Integer.toString(val));
                                maxVals[i] = val;
                            }

                            //Update graph for this sensor, if the checkbox is checked
                            if(ckBoxes[i].isChecked())
                                mSeries[i].appendData(
                                        new DataPoint(count, val), true, MAX_DATA_POINTS);
                        }

                        //update colors on muscle tension surfaceview
                        surfaceView.updateSurface(measData);

                    }
                });
            }
        }).start();
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
                if(checked){graphView.addSeries(mSeries[0]);}
                else{graphView.removeSeries(mSeries[0]);}
                Toast.makeText(context,"Checkbox1 checked"+ckBoxes[0].isChecked(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.chkBox2:
                if(checked){graphView.addSeries(mSeries[1]);}
                else{graphView.removeSeries(mSeries[1]);}
                break;
            case R.id.chkBox3:
                if(checked){graphView.addSeries(mSeries[2]);}
                else{graphView.removeSeries(mSeries[2]);}
                break;
            case R.id.chkBox4:
                if(checked){graphView.addSeries(mSeries[3]);}
                else{graphView.removeSeries(mSeries[3]);}
                    break;
            case R.id.chkBox5:
                if(checked){graphView.addSeries(mSeries[4]);}
                else{graphView.removeSeries(mSeries[4]);}
                    break;
            case R.id.chkBox6:
                if(checked){graphView.addSeries(mSeries[5]);}
                else{graphView.removeSeries(mSeries[5]);}
                    break;
        }
    }

}// end sessionactivity
