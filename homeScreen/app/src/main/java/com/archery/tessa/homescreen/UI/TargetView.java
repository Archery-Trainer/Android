package com.archery.tessa.homescreen.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.archery.tessa.homescreen.models.HitLocation;

import java.util.LinkedList;

import static java.lang.Math.sqrt;

/**
 * Created by mkkvj on 18.2.2018.
 */

public class TargetView extends SurfaceView implements Runnable {
    private Thread myThread;
    private SurfaceHolder mHolder2;
    private Bitmap mTarget;
    private Bitmap mArrow;

    private boolean isOK;
    // initial coordinate values for the arrow's hit location
    private int x=200;
    private int y=200;

    //target areas on archery target.
    // float values are pixels values on resource picture multiplied by 2.
    private float[] targetRadiuses={(219f*2),(176f*2),(132f*2),(87f*2),(43f*2),(21f*2)};
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


            Paint p=new Paint();
            p.setColor(Color.GRAY);

            //canvas.drawBitmap(mArrow,x-(mArrow.getWidth()/2),y-(mArrow.getHeight()/2),null);
            canvas.drawCircle((float)x,(float)y,20f,p);
            mHolder2.unlockCanvasAndPost(canvas);

        }
    }
//

//
//    }
    public void setTargetImage(Bitmap target){this.mTarget=target;}
    public void setHitImage(Bitmap hit){this.mArrow=hit;}

    /**returns hit value of x and y value on target. Possible values are 5...10 **/
    public int getHitValue(float x, float y) {
        int number=5;
        // r1+r2<Sqrt((x1-x2)^2+(y1-y2)^2), where r1=radius of hit area on target, r2=radius of arrow,
        // x1,y1 are center coordinate of hit area, x2,y2 are coordinates of arrow's center
        while (number>=0&&((getTargerRadiuses(number)+getArrowR()) < (sqrt(Math.pow((x - mTarget.getWidth() / 2), 2) + Math.pow((y - mTarget.getHeight() / 2), 2))))){
             number--;
        }
        System.out.println("Arrow hits to "+(number+5));
        return number+5;
    }
    /** returs true if x and y hit location are on target area. Otherwise returns false if arrow missed the target.**/
    public boolean isOnPictureArea(int x, int y) {
        /** r1>=Sqrt((x1-x2)^2+(y1-y2)^2)+r2, where r1=radius of whole target area, r2=radius of arrow,
         x1,y1 are center coordinate of target area, x2,y2 are coordinates of arrow's center **/
        return ((getTargetR()) >= (sqrt(Math.pow((x - mTarget.getWidth() / 2), 2) + Math.pow((y - mTarget.getHeight() / 2), 2))) + getArrowR());
    }
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
    //x-coordinate of arrow's hit location
    public void setX(float x){this.x=(int)x;}
    //y-coordinate of arrow's hit location
    public void setY(float y){this.y=(int)y;}
    //get radius of target area
    public float getTargetR(){return mTarget.getWidth()/2;}
    //radius of arrow
    public float getArrowR(){return mArrow.getWidth()/2;}
    // gets radiuses of target areas on arcery target. Possible indexes are 0...5,
    // 0 index means target area of 5 and index 5 means bulleye on archery target
    public float getTargerRadiuses(int index){return this.targetRadiuses[index];}

    public void init(){
        myThread=new Thread(this);
        mHolder2=getHolder();

        isOK=true;

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
