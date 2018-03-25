package com.archery.tessa.homescreen;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;

import com.archery.tessa.homescreen.models.MeasuredDataSet;
import com.archery.tessa.homescreen.models.Shot;
import com.archery.tessa.homescreen.tasks.GetSensorReadingsOfShotTask;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import com.archery.tessa.homescreen.UI.MuscleView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Timo on 21.2.2018.
 */

/**
 * Activity for replaying saved shots
 */
public class SavedRecordingActivity extends AppCompatActivity
{
    private Shot shot; //The shot to show

    private GetSensorReadingsOfShotTask getReadingstask;

    private GraphView graphView;

    private LineGraphSeries[] mSeries;
    private PointsGraphSeries<DataPoint> currentDisplayedPnt;

    private int count = 0;
    private int currentPlaybackPoint = 0;


    final int defaultPlaybackSpeed = 25;
    //Changed from a seek bar, 0-100
    private int playbackSpeed = defaultPlaybackSpeed;


    //Hack to prevent multiple calls to showMeasurementPoint when clicking on graph
    private long lastUpdateTime = 0;

    private List<MeasuredDataSet> measuredDataPoints;
    private MuscleView surfaceView;
    private Bitmap archerPic;

    private CheckBox[] ckBoxes;
    private boolean[] ckBoxesStatus = { true, true, true, true, true, true };

    private boolean playbackOn = false;
    private PlaybackTask playbackTask;

    private final int MAX_VAL_Y = 800;
    private final int MAX_VAL_X = 100;
    private final int NUM_SENSORS = 6;
    private final int LINE_WIDTH = 4;
    private final int MAX_DATA_POINTS = 5000;
    private final int PNTS_IN_CURRENT_PNT_MARKER_LINE = 25;


    /***********************************************************************
     When starting this activity, you should pass the Shot object like this:

     Intent i = new Intent(MainActivity.this, SavedRecordingActivity.class);

     Shot testShot = getShotFromSomewhere();
     i.putExtra("SHOT", testShot);

     startActivity(i);
     ************************************************************************/

    /**
     * Initialize the activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shot = (Shot) getIntent().getExtras().getSerializable("SHOT");

        /** Show info of the shot on the action bar **/
        setHeaderString(shot);

        /** Get the measured data from the server **/
        measuredDataPoints = new LinkedList<>();
        getReadingstask = new GetSensorReadingsOfShotTask(this, shot.getId(), measuredDataPoints);
        getReadingstask.execute();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_recording_activity);

        /** Create the muscle map **/
        initArcherPic();

        /** Some graph view settings **/
        Viewport viewport = graphView.getViewport();
        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(0);
        viewport.setMaxX(MAX_VAL_X);
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(MAX_VAL_Y);
        viewport.setScrollable(true);
        viewport.setScrollableY(true);
        viewport.setScalable(true);
        viewport.setScalableY(true);
        viewport.setBackgroundColor(Color.LTGRAY);


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
                    int newX = (int) Math.round(dataPoint.getX());
                    showMeasurementPoint(newX);
                }
            });

            graphView.addSeries(mSeries[i]);
        }

        //Need to create a new series to display one point
        //This is a hack to display a line on the graph. Different y values needed to be added
        //Because the mark would disappear when the y value was not visible
        currentDisplayedPnt = new PointsGraphSeries<>(createPntsforCurrentPntSeries(0));

        currentDisplayedPnt.setCustomShape(new PointsGraphSeries.CustomShape() {
            @Override
            public void draw(Canvas canvas, Paint paint, float x, float y, DataPointInterface dataPoint) {
                paint.setStrokeWidth(10);
                paint.setColor(Color.BLACK);
                paint.setAlpha(175);
                canvas.drawLine(x, y - 100, x, y + 100, paint);
            }
        });


        /** Rescale the current point marker when zooming on the graph so that points distribute evenly **/
        graphView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                currentDisplayedPnt.resetData(createPntsforCurrentPntSeries(currentPlaybackPoint));
                return false;
            }
        });

        /** Add the series that represents the current tapped point in graph **/
        graphView.addSeries(currentDisplayedPnt);


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




        /** Set up the play/pause button **/
        final Button playButton = findViewById(R.id.buttonPlay);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean playedToEnd = (currentPlaybackPoint == (measuredDataPoints.size() - 1));


                if(playedToEnd)
                    //Start playback from start
                    currentPlaybackPoint = 0;

                togglePlayback();

                //Change text accordingly
                if(playbackOn)
                    playButton.setText(R.string.pauseRecordingLabel);
                else
                    playButton.setText(R.string.playRecordingLabel);
            }
        });

        /** Set up the slider that controls playback speed **/
        final SeekBar playbackSpeedBar = findViewById(R.id.playbackSpeedBar);
        playbackSpeedBar.setProgress(defaultPlaybackSpeed); //Set to halfway
        playbackSpeedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                playbackSpeed = i;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }


    /**
     * When the activity becomes visible, show the measurements in the graph
     */
    @Override
    protected void onResume() {
        super.onResume();
        sendMeasurementsToGraph();
    }



    /**
     * Toggle playback of the shot on and off. Start a task that updates the UI
     */
    private void togglePlayback() {
        if(!playbackOn) {
            playbackOn = true;

            playbackTask = new PlaybackTask();
            playbackTask.execute();

        } else {
            playbackTask.cancel(true);
            playbackOn = false;
            playbackTask = null;
        }


    }

    /**
     * Show a measured data set in the graph and the muscle map
     *
     * @param measNo    index of the measurement in member measuredDataPoints
     */
    private void showMeasurementPoint(int measNo) {

        if(measNo >= measuredDataPoints.size())
            return;

        //Allow repaints only if they are >200 msec apart to avoid muscle map flickering
        long now = System.currentTimeMillis();

        if(now - lastUpdateTime < 200)
            return;

        lastUpdateTime = now;

        currentPlaybackPoint = measNo;

        //Move the mark that displays the current point in the graph
        currentDisplayedPnt.resetData(createPntsforCurrentPntSeries(measNo));


        MeasuredDataSet dataPnt = measuredDataPoints.get(measNo);
        if (dataPnt != null)
            surfaceView.updateSurface(dataPnt, ckBoxesStatus);

    }

    /**
     * Show the measurements that were received from the server in the graph
     */
    private void sendMeasurementsToGraph() {
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
                        setMeasurements(measuredDataPoints);
                    }
                });

            }
        };
        new Thread(runnable).start();
    }


    /**
     * Set measurements to the point series that is displayed in the graph
     *
     * @param measDataSets  the data set to append
     */
    private void setMeasurements(final List<MeasuredDataSet> measDataSets) {

        new Thread(new Runnable() {

            public void run() {

                for(MeasuredDataSet measData : measDataSets) {
                    final int sensorVals[] = {
                            measData.getSensorData(0).getValue(),
                            measData.getSensorData(1).getValue(),
                            measData.getSensorData(2).getValue(),
                            measData.getSensorData(3).getValue(),
                            measData.getSensorData(4).getValue(),
                            measData.getSensorData(5).getValue()
                    };


                    graphView.post(new Runnable() {

                        @Override
                        public void run() {
                            count++;

                            for (int i = 0; i < NUM_SENSORS; i++) {
                                int val = sensorVals[i];

                                //Update graph for this sensor
                                mSeries[i].appendData(new DataPoint(count, val), false, MAX_DATA_POINTS);
                            }

                        }
                    });
                }
            }
        }).start();
    }


    /**
     * Add or remove the point series from the graph when checkbox state changes
     *
     * @param view  The affected checkbox
     */
    public void onChkClicked(View view){
        boolean checked=((CheckBox)view).isChecked();
        switch(view.getId()){
            case R.id.chkBox1:
                if(checked){
                    graphView.addSeries(mSeries[0]);
                    ckBoxesStatus[0] = true;
                }
                else{
                    graphView.removeSeries(mSeries[0]);
                    ckBoxesStatus[0] = false;
                }
                break;
            case R.id.chkBox2:
                if(checked){
                    graphView.addSeries(mSeries[1]);
                    ckBoxesStatus[1] = true;
                }
                else{
                    graphView.removeSeries(mSeries[1]);
                    ckBoxesStatus[1] = false;
                }
                break;
            case R.id.chkBox3:
                if(checked){
                    graphView.addSeries(mSeries[2]);
                    ckBoxesStatus[2] = true;
                }
                else{
                    graphView.removeSeries(mSeries[2]);
                    ckBoxesStatus[2] = false;
                }
                break;
            case R.id.chkBox4:
                if(checked){
                    graphView.addSeries(mSeries[3]);
                    ckBoxesStatus[3] = true;
                }
                else{
                    graphView.removeSeries(mSeries[3]);
                    ckBoxesStatus[3] = false;

                }
                break;
            case R.id.chkBox5:
                if(checked){
                    graphView.addSeries(mSeries[4]);
                    ckBoxesStatus[4] = true;
                }
                else{
                    graphView.removeSeries(mSeries[4]);
                    ckBoxesStatus[4] = false;
                }
                break;
            case R.id.chkBox6:
                if(checked){
                    graphView.addSeries(mSeries[5]);
                    ckBoxesStatus[5] = true;
                }
                else{
                    graphView.removeSeries(mSeries[5]);
                    ckBoxesStatus[5] = false;
                }
                break;
        }
    }


    /**
     * Show info of the displayed shot in the action bar
     * @param shot  The current shot
     */
    private void setHeaderString(Shot shot) {
        ActionBar actionBar = getSupportActionBar();
        String header = "Showing shot from " + shot.getDate() + " : " +
            shot.getTime() + ", Score: " + shot.getScore();

        actionBar.setTitle(header);
    }

    /**
     * Create a point series for the visualization line that represents the current playback point
     *
     * @param currentMeasurementNo  Where on x axis to place the points
     * @return  Points that are evenly distributed on the y axis
     */
    private DataPoint[] createPntsforCurrentPntSeries(int currentMeasurementNo) {

        Viewport vp = graphView.getViewport();
        int visiblePnts = (int) (vp.getMaxY(false) - vp.getMinY(false));

        DataPoint[] pnts = new DataPoint[PNTS_IN_CURRENT_PNT_MARKER_LINE];
        for(int i = 0; i < PNTS_IN_CURRENT_PNT_MARKER_LINE; i++) {
            pnts[i] = new DataPoint(
                    currentMeasurementNo,
                    (vp.getMinY(false) + (visiblePnts
                            / PNTS_IN_CURRENT_PNT_MARKER_LINE) * i)); //Evenly divide on y axis
        }

        return pnts;
    }

    /**
     * Initialize the muscle map image
     *
     */
    private void initArcherPic() {

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inMutable=true;
        surfaceView = (MuscleView)findViewById(R.id.archerSurfaceView);
        surfaceView.setZOrderOnTop(true);
        //pics.add(BitmapFactory.decodeResource(getResources(),R.drawable.archer_ind_right_2_2_18v3,options));
        archerPic = BitmapFactory.decodeResource(getResources(),R.drawable.archer_left_scaled,options);
        //System.out.println("checking"+archerPic.isMutable());

        surfaceView.setdPicsForDrawing(archerPic);
        graphView = (GraphView) findViewById(R.id.graph);

        surfaceView.setdPicsForDrawing(BitmapFactory.decodeResource(getResources(),R.drawable.left_trapezoid,options));
        surfaceView.setdPicsForDrawing(BitmapFactory.decodeResource(getResources(),R.drawable.right_trapezoid,options));

        surfaceView.setdPicsForDrawing(BitmapFactory.decodeResource(getResources(),R.drawable.left_deltoid,options));
        surfaceView.setdPicsForDrawing(BitmapFactory.decodeResource(getResources(),R.drawable.right_deltoid,options));

        surfaceView.setdPicsForDrawing(BitmapFactory.decodeResource(getResources(),R.drawable.left_triceps,options));
        surfaceView.setdPicsForDrawing(BitmapFactory.decodeResource(getResources(),R.drawable.right_triceps,options));
    }

    /**
     * Updater task for the recording playback
     */
    private class PlaybackTask extends AsyncTask<Void, Void, Void> {

        /**
         * Sleep a duration based on the speed slider and then update UI
         *
         * @param voids dummy
         * @return  dummy
         */
        @Override
        protected Void doInBackground(Void... voids) {


            for(int i = currentPlaybackPoint; i < measuredDataPoints.size(); i++) {
                //See if user tapped on point in graph
                if(Math.abs(currentPlaybackPoint - i) > 5)
                    i = currentPlaybackPoint;

                showMeasurementPoint(i);

                try {
                    int millisToSleep = 10000;
                    if(playbackSpeed != 0)
                        millisToSleep /= playbackSpeed;

                    Thread.sleep(millisToSleep); //sleep is 0.1-10 sec
                } catch (InterruptedException e) {
                    System.out.println("Playback interrupted");
                    break;

                }
            }

            return null;
        }


        /**
         * After the playback has completed. Reset the 'Play' button and activity state
         *
         * @param dummy
         */
        @Override
        protected void onPostExecute(Void dummy) {
            //Reset the text in the play button
            Button playButton = findViewById(R.id.buttonPlay);
            playButton.setText(R.string.playRecordingLabel);

            playbackOn = false;

        }
    }

}
