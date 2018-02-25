package com.archery.tessa.homescreen.UI;

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

public class TargetView extends SurfaceView implements Runnable {
    private Thread myThread;
    private SurfaceHolder mHolder2;
    private Bitmap mTarget;
    private Bitmap mArrow;
    private HitLocation hitLocation;
    private boolean isOK;
    private int x=200;
    private int y=200;

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


    @Override
    public void run() {
        while(isOK==true) {

            if (!mHolder2.getSurface().isValid()) {
                continue;
            }
            Canvas canvas = mHolder2.lockCanvas();

            if (canvas == null)
                return;

            Matrix matrix = new Matrix();
            matrix.setRotate(0, mTarget.getWidth() / 2, mTarget.getHeight() / 2);
            canvas.drawBitmap(mTarget, matrix, null);
            //mTarget.g
            canvas.drawBitmap(mArrow,x-(mArrow.getWidth()/2),y-(mArrow.getHeight()/2),null);
            mHolder2.unlockCanvasAndPost(canvas);
        }
    }
//

//
//    }
    public void setTargetImage(Bitmap target){this.mTarget=target;}
    public void setHitImage(Bitmap hit){this.mArrow=hit;}
    public void drawImage(){
        if(!mHolder2.getSurface().isValid()){
                //continue;

        }
        Canvas canvas2=mHolder2.lockCanvas();




        System.out.println("canvas :"+(canvas2==null));
        if(canvas2!=null){
            Matrix matrix=new Matrix();
            matrix.setRotate(0, mTarget.getWidth() / 2, mTarget.getHeight() / 2);
            System.out.println("Target"+(mTarget==null));
            canvas2.drawBitmap(mTarget,matrix,null);

            //canvas2.drawBitmap(mArrow,x-(mArrow.getWidth()*2),y-(mArrow.getHeight()*2)), new Rect(hitLocation.getX(),hitLocation.getY(),hitLocation.getX()+(mArrow.getWidth()*2),hitLocation.getY()+(mArrow.getHeight()*2)),null);
        }
        mHolder2.unlockCanvasAndPost(canvas2);
    }

    public void setHitlocation(float x, float y){
        hitLocation.setX((int)x);
        hitLocation.setY((int)y);

        //hit location doesn't exist

    }
    public void setX(float x){this.x=(int)x;}
    public void setY(float y){this.y=(int)y;}

    public void init(){
        myThread=new Thread(this);
        mHolder2=getHolder();
        System.out.println("mholder2:"+(mHolder2==null));
        isOK=true;
        //hitLocation.setY(20);
        //hitLocation.setX(20);
        mHolder2.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                myThread.start();
                //drawImage();

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
