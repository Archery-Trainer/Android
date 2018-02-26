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

import com.archery.tessa.homescreen.UI.TargetView;
import com.archery.tessa.homescreen.models.Shot;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hitselection_activity);
        System.out.println("new activity started");

        tView=findViewById(R.id.targetView);
        tView.setOnTouchListener(this);
        hitVal=(TextView)findViewById(R.id.hitValue);
        saveButton=(Button)findViewById(R.id.saveButton);

        target = BitmapFactory.decodeResource(getResources(),R.drawable.own_442px_80_cm_archery_target);

        arrow = BitmapFactory.decodeResource(getResources(),R.drawable.arrow);
        //test values

        Shot newShot=new Shot(new Date(System.currentTimeMillis()),new Time(System.currentTimeMillis()),Integer.parseInt(hitVal.getText().toString()),6);
        saveButton.setOnClickListener(this);

        if(tView==null){System.out.println("targetview is null");}
        if(arrow==null){System.out.println("arrow is null");}

        tView.setTargetImage(target);
        tView.setHitImage(arrow);


    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){


            float touchX = motionEvent.getX();
            float touchY = motionEvent.getY();
            // check if touch is on target area
            if(tView.isOnPictureArea((int)touchX, (int)touchY) ){
                //get the value of arrow hit location
                int hitval=tView.getHitValue(motionEvent.getX(),motionEvent.getY());
                hitVal.setText(String.valueOf(hitval));
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
        //Date d = new Date();
        //Shot mShot=new Shot(),)
        finish();
    }
}
