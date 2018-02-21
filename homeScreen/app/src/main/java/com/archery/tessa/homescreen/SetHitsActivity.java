package com.archery.tessa.homescreen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.archery.tessa.homescreen.UI.TargetView;

/**
 * Created by mkkvj on 17.2.2018.
 */

public class SetHitsActivity extends AppCompatActivity implements View.OnTouchListener {
    private static final String TAG = "SetHitsActivity";
    private TargetView tView;
    private Bitmap arrow;
    private Bitmap target;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hitselection_activity);
        System.out.println("new activity started");

        tView=findViewById(R.id.targetView);
        tView.setOnTouchListener(this);
        //BitmapFactory.Options options=new BitmapFactory.Options();
        //options.inMutable=true;

        target = BitmapFactory.decodeResource(getResources(),R.drawable.own_442px_80_cm_archery_target);
        arrow = BitmapFactory.decodeResource(getResources(),R.drawable.arrow);

        if(tView==null){System.out.println("targetview is null");}
        if(arrow==null){System.out.println("arrow is null");}

        tView.setTargetImage(target);
        tView.setHitImage(arrow);


    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        float touchLocX;
        float touchLocY;
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){

            tView.setX(motionEvent.getX());
            tView.setY(motionEvent.getY());
            //Log.d(TAG, "onCreate: "+id);
            //imageView.setImageResource(id);
        }
        if ((motionEvent.getAction()==MotionEvent.ACTION_UP)){

            tView.setX(motionEvent.getX());
            tView.setY(motionEvent.getY());
        }

        return false;
    }
}
