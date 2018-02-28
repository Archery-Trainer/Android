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

import com.archery.tessa.homescreen.models.MeasuredDataSet;

import java.util.LinkedList;
import java.util.List;

import static android.graphics.Color.parseColor;

/**
 * Created by mkkvj on 1.2.2018.
 */

public class OurView extends SurfaceView{
    private SurfaceHolder holder;
    boolean isOK=false;
    private LinkedList<Bitmap> pics;
    private LinkedList<Bitmap> originalPics;

    private final int TENSION_COLOR = Color.parseColor("#410000");
    private final int NUM_SENSORS = 6;
    private final int MAX_SENSOR_VAL = 750;

    public OurView(Context context) {
        super(context);
        init();
    }

    public OurView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    public OurView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private int standardizeSensorValue(int val) {
        return Math.round(((float)val / MAX_SENSOR_VAL) * 255);
    }


    /**
     * Update the muscle map picture
     * @param sensorvalues  A measurement of sensor values
     * @param showMuscle    Array telling which sensor values to visualize
     */
    public void updateSurface(MeasuredDataSet sensorvalues, boolean[] showMuscle){

        int[] muscleTensions = new int[6];

        for(int i = 0; i < NUM_SENSORS; i++) {
            int tension = 0;
            if(showMuscle[i])
                tension = standardizeSensorValue(sensorvalues.getSensorData(i).getValue());

            muscleTensions[i] = tension;
        }

        drawSurface(muscleTensions);

    }

    public void init(){

        holder=getHolder();

        pics= new LinkedList<>();
        originalPics = new LinkedList<>();
        isOK=true;
        System.out.println("init function");

        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                System.out.println("surfaceCreated");

                for(int i = 1; i <= 6; i++) {
                    setInitialColor(pics.get(i));
                }
                drawSurface(new int[]{0,0,0,0,0,0});
            }
            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }


            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
    }
    public void setdPicsForDrawing(Bitmap pic){
        System.out.println("adding pics");
        pics.add(pic);
        originalPics.add(pic);
    }
    public int getNumberOfPics(){return pics.size();}


    //Rectangles around the small muscle pics
    private final Rect srcRectLeftTrap = new Rect(0,0,(50*2), (141*2));
    private final Rect srcRectRightTrap = new Rect(0,0,(46*2), (143*2));
    private final Rect srcRectLeftDelt = new Rect(0,0,(71*2),(43*2));
    private final Rect srcRectRightDelt = new Rect(0,0,(69*2), (49*2));
    private final Rect srcRectLeftTricep = new Rect(0,0,(82*2),(19*2));
    private final Rect srcRectRightTricep = new Rect(0,0,(95*2), (39*2));

    //Rectangles where to place the muscle pics
    private final Rect dstRectLeftTrap =
            new Rect((292*2),(105*2),(292*2) + srcRectLeftTrap.right,(105*2) + srcRectLeftTrap.bottom);
    private final Rect dstRectRightTrap =
            new Rect((338*2),(107*2),(338*2) + srcRectRightTrap.right,(107*2) + srcRectRightTrap.bottom);
    private final Rect dstRectRightDelt =
            new Rect((372*2),(127*2),(372*2) + srcRectRightDelt.right ,(127*2) + srcRectRightDelt.bottom);
    private final Rect dstRectLeftDelt =
            new Rect((231*2),(131*2),(231*2) + srcRectLeftDelt.right,(131*2) + srcRectLeftDelt.bottom);
    private final Rect dstRectLeftTricep =
            new Rect((177*2),(166*2),(177*2)+ srcRectLeftTricep.right,(166*2) + srcRectLeftTricep.bottom);
    private final Rect dstRectRightTricep =
            new Rect((414*2),(142*2),(414*2) + srcRectRightTricep.right,(142*2) + srcRectRightTricep.bottom);



    public void drawSurface(int[] sensorValues){
        Canvas canvas=holder.lockCanvas();

        Matrix matrix = new Matrix();
        matrix.setRotate(0, pics.get(0).getWidth() / 2, pics.get(0).getHeight() / 2);

        if (canvas == null) {
            System.out.println("canvas null");
            return;
        }

        canvas.drawBitmap(pics.get(0), matrix, null);  // base image of archer

        Paint p = new Paint();
        p.setAlpha(sensorValues[0]);
        canvas.drawBitmap(pics.get(1), srcRectLeftTrap, dstRectLeftTrap, p);

        p.setAlpha(sensorValues[1]);
        canvas.drawBitmap(pics.get(2), srcRectRightTrap, dstRectRightTrap, p);

        p.setAlpha(sensorValues[2]);
        canvas.drawBitmap(pics.get(3), srcRectLeftDelt, dstRectLeftDelt, p);

        p.setAlpha(sensorValues[3]);
        canvas.drawBitmap(pics.get(4), srcRectRightDelt, dstRectRightDelt, p);

        p.setAlpha(sensorValues[4]);
        canvas.drawBitmap(pics.get(5), srcRectLeftTricep, dstRectLeftTricep, p);

        p.setAlpha(sensorValues[5]);
        canvas.drawBitmap(pics.get(6), srcRectRightTricep, dstRectRightTricep, p);

        holder.unlockCanvasAndPost(canvas);

    }

    private boolean okToDrawToPixel(Bitmap originalPic, int x, int y) {

        int color = originalPic.getPixel(x, y);

        if(color == 0) {
            //Pixel is white, part of background
            return false;
        }

        return true;
    }

    /**
     * Color the picture with the muscle tension color
     * @param bitmap    Picture to color
     * @return  true if successful, false if not
     */
    boolean setInitialColor(Bitmap bitmap)
    {
        // bitmap must be mutable and 32 bits per pixel:
        if((bitmap.getConfig() != Bitmap.Config.ARGB_8888) || !bitmap.isMutable())
            return false;

        for(int x = 0; x<bitmap.getWidth(); x++){
            for(int y = 0; y<bitmap.getHeight(); y++){
                if(okToDrawToPixel(bitmap, x, y)){

                    bitmap.setPixel(x, y, TENSION_COLOR);
                }
            }
        }

        return true;

    }
}
