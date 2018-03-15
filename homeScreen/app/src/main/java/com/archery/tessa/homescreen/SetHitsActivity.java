package com.archery.tessa.homescreen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.archery.tessa.homescreen.UI.TargetView;
import com.archery.tessa.homescreen.models.Shot;
import com.archery.tessa.homescreen.tasks.SetScoreForShotTask;
import com.archery.tessa.homescreen.tasks.StopRecordingTask;

import org.springframework.http.HttpStatus;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by mkkvj on 17.2.2018.
 */

public class SetHitsActivity extends AppCompatActivity implements View.OnTouchListener,View.OnClickListener {
    private static final String TAG = "SetHitsActivity";
    private TargetView tView;
    private Bitmap arrow;
    private Bitmap target;
    private TextView hitVal;
    private Button saveButton;
    private int score;
    private StopRecordingTask stopRecordingTask;

    private void stopRecording() {
        stopRecordingTask = new StopRecordingTask();
        stopRecordingTask.execute(this);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        stopRecording();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.hitselection_activity);
        System.out.println("new activity started");

        tView=findViewById(R.id.targetView);
        tView.setOnTouchListener(this);
        tView.setZOrderOnTop(true);
        hitVal=(TextView)findViewById(R.id.hitValue);
        saveButton=(Button)findViewById(R.id.saveButton);

        target = BitmapFactory.decodeResource(getResources(),R.drawable.archery_target_300px_80cm);
        System.out.println("h: "+target.getHeight());
        //test values

        Shot newShot=new Shot(new Date(System.currentTimeMillis()),new Time(System.currentTimeMillis()),Integer.parseInt(hitVal.getText().toString()),6);
        saveButton.setOnClickListener(this);

        tView.setTargetImage(target);
        //tView.setHitImage(arrow);


    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            float touchX = motionEvent.getX();
            float touchY = motionEvent.getY();
            // check if touch is on target area
            if(tView.isOnPictureArea((int)touchX, (int)touchY) ){
                //get the value of arrow hit location
                score = tView.getHitValue(motionEvent.getX(),motionEvent.getY());
                hitVal.setText(String.valueOf(score));
                tView.setX(motionEvent.getX());
                tView.setY(motionEvent.getY());
            }

        }
        if ((motionEvent.getAction()==MotionEvent.ACTION_UP)){
            if(tView.isOnPictureArea((int)motionEvent.getX(), (int)motionEvent.getY())) {

                tView.setX(motionEvent.getX());
                tView.setY(motionEvent.getY());
            }
        }
        if(motionEvent.getAction()==MotionEvent.ACTION_MOVE){
            tView.setX(motionEvent.getX());
            tView.setY(motionEvent.getY());
        }

        return false;


    }


    @Override
    public void onClick(View view) {
        System.out.println("onClick pressed");

        int shotId = stopRecordingTask.getShotId();

        if(shotId == -1)
            Toast.makeText(this, R.string.saveShotError, Toast.LENGTH_SHORT).show();
        SetScoreForShotTask setScoreTask = new SetScoreForShotTask(shotId, score, this);
        setScoreTask.execute();
        finish();
    }
}
