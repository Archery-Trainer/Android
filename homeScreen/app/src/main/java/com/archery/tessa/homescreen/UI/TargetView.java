package com.archery.tessa.homescreen.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static java.lang.Math.sqrt;

/**
 *  Creates canvas for drawing archery target and reads user selected score by placing
 *  image of arrow to hit location. Updates to archery target and image of selected
 *  arrow it location.
 */

public class TargetView extends SurfaceView implements Runnable {
    private Thread myThread;
    private SurfaceHolder mHolder2;
    private Bitmap mTarget;
    private float mArrow;
    private boolean isOK;
    private float displayScalingFactor=getResources().getDisplayMetrics().density;
    // initial coordinate values for the arrow's hit location
    private int x=(int)(100*displayScalingFactor);
    private int y=(int)(100*displayScalingFactor);

    //target areas on archery target.
    // float values are pixels values on resource picture multiplied by displayScalingFactor.
    private float[] targetRadiuses={(219f*displayScalingFactor),(176f*displayScalingFactor),(132f*displayScalingFactor),(87f*displayScalingFactor),(43f*displayScalingFactor),(21f*displayScalingFactor)};
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

    /**
     *  Creates drawing canvas and draws target and arrow images.
     */
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
            matrix.setRotate(0, mTarget.getWidth() / displayScalingFactor, mTarget.getHeight() / displayScalingFactor);
            canvas.drawBitmap(mTarget, matrix, null);
            Paint p=new Paint();
            p.setColor(Color.GRAY);

            //canvas.drawBitmap(mArrow,x-(mArrow.getWidth()/2),y-(mArrow.getHeight()/2),null);
            canvas.drawCircle((float)x,(float)y,getArrowR(),p);
            mHolder2.unlockCanvasAndPost(canvas);

        }
    }

    /**
     * Adds image of target to be drawn.
     * @param target bitmap image of target
     */
    public void setTargetImage(Bitmap target){this.mTarget=target;}

    /**
     * Returns hit value of x and y value on target. Possible values are 5...10
     * @param x coordinate of hit location
     * @param y coordinate of hit location
     * @return returns score based on hit location
     */
    public int getHitValue(float x, float y) {

        int number=5;
        // r1+r2<Sqrt((x1-x2)^2+(y1-y2)^2), where r1=radius of hit area on target, r2=radius of arrow,
        // x1,y1 are center coordinate of target area, x2,y2 are coordinates of arrow's center
        while (number>=0&&((getTargerRadiuses(number)+getArrowR()) < (sqrt(Math.pow((x - mTarget.getWidth() / displayScalingFactor), 2) + Math.pow((y - mTarget.getHeight() / displayScalingFactor), 2))))){
             number--;
        }
        System.out.println("Arrow hits to "+(number+5));
        return number+5;
    }

    /**
     * Returs true if x and y hit location are on target area. Otherwise returns false if arrow missed the target.
     * @param x coordinate of hit location
     * @param y coordinate of hit location
     * @return returns true if touch location is on target or false if outside of target area.
     */
    public boolean isOnPictureArea(int x, int y) {
        /** r1>=Sqrt((x1-x2)^2+(y1-y2)^2)+r2, where r1=radius of whole target area, r2=radius of arrow,
         x1,y1 are center coordinate of target area, x2,y2 are coordinates of arrow's center **/
        return ((getTargetR()) >= (sqrt(Math.pow((x - mTarget.getWidth() / displayScalingFactor), 2) + Math.pow((y - mTarget.getHeight() / displayScalingFactor), 2))) + getArrowR());
    }

    //x-coordinate of arrow's hit location
    public void setX(float x){this.x=(int)x;}
    //y-coordinate of arrow's hit location
    public void setY(float y){this.y=(int)y;}
    //get radius of target area
    public float getTargetR(){return mTarget.getWidth()/displayScalingFactor;}
    //get radius of target area
    public float getArrowR(){return mArrow/displayScalingFactor;}
    // gets radiuses of target areas on arcery target. Possible indexes are 0...5,
    // 0 index means target area of 5 and index 5 means bulleye on archery target
    public float getTargerRadiuses(int index){return this.targetRadiuses[index];}


    public void init(){
        myThread=new Thread(this);
        mHolder2=getHolder();
        mHolder2.setFormat(PixelFormat.TRANSPARENT);

        mArrow=20*displayScalingFactor;
        isOK=true;

        mHolder2.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                myThread.start();
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
