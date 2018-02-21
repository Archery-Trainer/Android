package com.archery.tessa.homescreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.services.iot.client.AWSIotException;
import com.archery.tessa.homescreen.models.MeasuredDataSet;
import com.archery.tessa.homescreen.models.RecordingRequest;
import com.archery.tessa.homescreen.tasks.GetSensorReadingsOfShotTask;
import com.archery.tessa.homescreen.tasks.StartRecordingTask;
import com.archery.tessa.homescreen.tasks.StopRecordingTask;
import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Timo on 21.2.2018.
 */

public class SavedRecordingActivity extends AppCompatActivity
{
    private int shotId; //Id of the shot to show

    GetSensorReadingsOfShotTask getReadingstask;

    private TextView[] textViews;

    private TextView[] maxTextViews;

    private GraphView graphView;

    private LineGraphSeries[] mSeries;
    private PointsGraphSeries<DataPoint> currentDisplayedPnt;

    private int count = 0;

    private List<MeasuredDataSet> measuredDataPoints;
    private OurView surfaceView;
    private Bitmap archerPic;

    private CheckBox[] ckBoxes;

    private Context context;

    private int maxVals[] = {0, 0, 0, 0, 0, 0};

    private final int NUM_SENSORS = 6;
    private final int LINE_WIDTH = 4;
    private final int MAX_DATA_POINTS = 500;


    /***********************************************************************
     When starting this activity, you should pass the shot id like this:

     Intent i = new Intent(RecordingActivity.this, SavedRecordingActivity.class);
     //Pass shot id to SavedRecordingActivity
     i.putExtra("SHOT_ID", 94);
     startActivity(i);
     ************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shotId = getIntent().getExtras().getInt("SHOT_ID");

        //Need to create a new series to display one point
        currentDisplayedPnt = new PointsGraphSeries<>(
                new DataPoint[] {
                        new DataPoint(0,0)});

        measuredDataPoints = new LinkedList<>();
        getReadingstask = new GetSensorReadingsOfShotTask(this, shotId, measuredDataPoints);
        getReadingstask.execute();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_recording_activity);

        System.out.println("Started SavedRecordingActivity");


        //Get the measured data from the server


        context = this;

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


        int muscleColors[] = GraphColors.colors;

        /** Add point series to graph **/
        mSeries = new LineGraphSeries[NUM_SENSORS];
        for(int i = 0; i < NUM_SENSORS; i++) {
            mSeries[i] = new LineGraphSeries();
            mSeries[i].setColor(muscleColors[i]);
            mSeries[i].setDrawAsPath(true);
            mSeries[i].setThickness(LINE_WIDTH);
            mSeries[i].setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    currentDisplayedPnt.resetData(new DataPoint[] {
                            new DataPoint(dataPoint.getX(), dataPoint.getY())
                    });

                    int measIx = (int) dataPoint.getX();
                    Toast.makeText(graphView.getContext(), "Series: On Data Point clicked: " + dataPoint, Toast.LENGTH_SHORT).show();

                    MeasuredDataSet dataPnt = measuredDataPoints.get(measIx);
                    if (dataPnt != null)
                        //@TODO: Overlapping lines cause problems, this gets called multiple times
                        surfaceView.updateSurface(dataPnt);

                }
            });

            graphView.addSeries(mSeries[i]);
        }

        //Add the series that represents the current tapped point in graph
        graphView.addSeries(currentDisplayedPnt);

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
        Viewport viewport = graphView.getViewport();
        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(0);
        viewport.setMaxX(100);
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(800);
        viewport.setScrollable(true);
        viewport.setScrollableY(true);
        viewport.setScalable(true);
        viewport.setScalableY(true);
        viewport.setBackgroundColor(Color.LTGRAY);

    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("STATUS: " + getReadingstask.getStatus().toString());
        startUpdaterThread();

    }

    private void startUpdaterThread() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {

                //Wait for readings to be fetched
                while (getReadingstask.getStatus() != AsyncTask.Status.FINISHED) {
                    try {
                        Thread.sleep(100);
                        continue;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //Start thread that updates UI
                handler.post(new Runnable(){
                    public void run() {
                        System.out.println("run called, " + measuredDataPoints.size());
                        for(MeasuredDataSet set : measuredDataPoints) {
                            showMeasurement(set);
                        }
                    }
                });

            }
        };
        new Thread(runnable).start();
    }


    private void showMeasurement(final MeasuredDataSet measData) {

        new Thread(new Runnable() {

            public void run() {

                final int sensorVals[] = {
                        measData.getSensorData(0).getValue(),
                        measData.getSensorData(1).getValue(),
                        measData.getSensorData(2).getValue(),
                        measData.getSensorData(3).getValue(),
                        measData.getSensorData(4).getValue(),
                        measData.getSensorData(5).getValue()
                };


                graphView.post(new Runnable() {

                    //Update ui elements when receiving message
                    @Override
                    public void run() {
                        count++;

                        for(int i = 0; i < NUM_SENSORS; i++) {
                            int val = sensorVals[i];

                            /*** Maybe don't show the texts in this view?

                            //Update text views
                            textViews[i].setText(Integer.toString(val));

                            int maxVal = maxVals[i];
                            if(val > maxVal) {
                                maxTextViews[i].setText(Integer.toString(val));
                                maxVals[i] = val;
                            }

                             */
                            //Update graph for this sensor
                            mSeries[i].appendData(new DataPoint(count, val), false, MAX_DATA_POINTS);
                        }

                        //update colors on muscle tension surfaceview
                        //surfaceView.updateSurface(measData);

                    }
                });
            }
        }).start();
    }




    public void onChkClicked(View view){
        boolean checked=((CheckBox)view).isChecked();
        switch(view.getId()){
            case R.id.chkBox1:
                if(checked){graphView.addSeries(mSeries[0]);}
                else{graphView.removeSeries(mSeries[0]);}
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

}
