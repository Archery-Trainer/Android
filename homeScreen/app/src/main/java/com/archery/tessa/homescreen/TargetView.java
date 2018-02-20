package com.archery.tessa.homescreen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.archery.tessa.homescreen.models.HitLocation;

import java.util.LinkedList;

/**
 * Created by mkkvj on 18.2.2018.
 */

public class TargetView extends SurfaceView{// implements Runnable {
    private Thread myThread2;
    private SurfaceHolder mHolder2;
    private Bitmap mTarget;
    private Bitmap mArrow;
    private HitLocation hitLocation;
    private boolean isOK;

    public TargetView(Context context) {
        super(context);
        init();
    }

    public TargetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TargetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


//    @Override
//    public void run() {
//        while(isOK==true){
//
//            if(!mHolder2.getSurface().isValid()){
//                continue;
//            }
//            Canvas canvas=mHolder2.lockCanvas();
//
//            if(canvas == null)
//                return;
//
//        Matrix matrix=new Matrix();
//        matrix.setRotate(0, mTarget.getWidth() / 2, mTarget.getHeight() / 2);
//        canvas.drawBitmap(mTarget,matrix,null);
//
//        canvas.drawBitmap(mArrow,new Rect(0,0,(mArrow.getWidth()*2),(mArrow.getHeight()*2)), new Rect(hitLocation.getX(),hitLocation.getY(),hitLocation.getX()+(mArrow.getWidth()*2),hitLocation.getY()+(mArrow.getHeight()*2)),null);
//        mHolder2.unlockCanvasAndPost(canvas);
//    }
//


//
//    }
    public void setTargetImage(Bitmap target){this.mTarget=target;}
    public void setHitImage(Bitmap hit){this.mArrow=hit;}
    public void drawImage(){

        Canvas canvas=mHolder2.lockCanvas();
        if(canvas!=null){
            Matrix matrix=new Matrix();
            matrix.setRotate(0, mTarget.getWidth() / 2, mTarget.getHeight() / 2);
            canvas.drawBitmap(mTarget,matrix,null);

            canvas.drawBitmap(mArrow,new Rect(0,0,(mArrow.getWidth()*2),(mArrow.getHeight()*2)), new Rect(hitLocation.getX(),hitLocation.getY(),hitLocation.getX()+(mArrow.getWidth()*2),hitLocation.getY()+(mArrow.getHeight()*2)),null);
        }
        mHolder2.unlockCanvasAndPost(canvas);
    }

    public void setHitlocation(float x, float y){
        hitLocation.setX((int)x);
        hitLocation.setY((int)y);

        //hit location doesn't exist

    }

    public void init(){
        //myThread2=new Thread(this);
        mHolder2=getHolder();
        isOK=true;
        mHolder2.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                //myThread.start();
                drawImage();

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });

    }
}
