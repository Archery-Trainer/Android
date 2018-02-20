package com.archery.tessa.homescreen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by mkkvj on 17.2.2018.
 */

public class SetHitsActivity extends AppCompatActivity implements View.OnTouchListener {
    private static final String TAG = "SetHitsActivity";
    private TargetView targetView;
    private Bitmap arrow;
    private Bitmap target;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("new activity started");

        targetView=(TargetView)findViewById(R.id.targetview);
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inMutable=true;

        target = BitmapFactory.decodeResource(getResources(),R.drawable.own_512px_80_cm_archery_target,options);
        arrow=BitmapFactory.decodeResource(getResources(),R.drawable.archer_right_triceps_15_2_18v3,options);

        if(targetView==null){System.out.println("targetview is null");}
        if(arrow==null){System.out.println("arrow is null");}
        targetView.setTargetImage(target);
        targetView.setHitImage(arrow);


    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        float touchLocX;
        float touchLocY;
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            touchLocX = motionEvent.getX();
            touchLocY = motionEvent.getY();
            Log.d("touched X & Y",(touchLocX+" & "+touchLocY));
            //int id = getResources().getIdentifier("bullethole.png", "drawable",this.getPackageName());
            targetView.setHitlocation(touchLocX,touchLocY);
            //Log.d(TAG, "onCreate: "+id);
            //imageView.setImageResource(id);
        }

        return false;
    }
}
