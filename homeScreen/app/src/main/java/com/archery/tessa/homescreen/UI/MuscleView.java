package com.archery.tessa.homescreen.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.archery.tessa.homescreen.models.MeasuredDataSet;

import java.util.LinkedList;

/**
 * Created by mkkvj on 1.2.2018.
 */

public class MuscleView extends SurfaceView{
    private SurfaceHolder holder;
    boolean isOK=false;
    private LinkedList<Bitmap> pics;
    private LinkedList<Bitmap> originalPics;
    private LinkedList<Rect> musclePicAreas;

    private final int TENSION_COLOR = Color.parseColor("#410000");
    private final int NUM_SENSORS = 6;
    private final int MAX_SENSOR_VAL = 750;

    private final int LEFT_TRAPEZOID=0;
    private final int RIGHT_TRAPEZOID=1;
    private final int LEFT_DELTOID=2;
    private final int RIGHT_DELTOID=3;
    private final int LEFT_TRICEPS=4;
    private final int RIGHT_TRICEPS=5;

    public MuscleView(Context context) {
        super(context);
        init();
    }

    public MuscleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    public MuscleView(Context context, AttributeSet attrs, int defStyleAttr) {
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

        int[] muscleTensions = new int[NUM_SENSORS];

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
        //set the holder to transparent so that view's background color shows around archer bitmap
        holder.setFormat(PixelFormat.TRANSPARENT);
        musclePicAreas=new LinkedList<>();
        pics= new LinkedList<>();
        originalPics = new LinkedList<>();
        isOK=true;
        System.out.println("init function");

        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                System.out.println("surfaceCreated");

                for(int i = 1; i <= NUM_SENSORS; i++) {
                    setInitialColor(pics.get(i));
                    musclePicAreas.add(getPicDimension(pics.get(i)));
                    System.out.println("Adding muscle picture area: "+musclePicAreas.getLast().toShortString());
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


//    //Rectangles around the small muscle pics
//    private final Rect srcRectLeftTrap = new Rect(0,0,(50*2), (141*2));
//    private final Rect srcRectRightTrap = new Rect(0,0,(46*2), (143*2));
//    private final Rect srcRectLeftDelt = new Rect(0,0,(71*2),(43*2));
//    private final Rect srcRectRightDelt = new Rect(0,0,(69*2), (49*2));
//    private final Rect srcRectLeftTricep = new Rect(0,0,(82*2),(19*2));
//    private final Rect srcRectRightTricep = new Rect(0,0,(95*2), (39*2));
//
//    //Left and top coordinates of the muscle pics on the archer pic
//    private final int[] posLeftTrap = {294*2, 107*2};
//    private final int[] posRightTrap = {341*2, 107*2};
//    private final int[] posLeftDelt = {234*2, 134*2};
//    private final int[] posRightDelt = {375*2, 129*2};
//    private final int[] posLeftTricep = {179*2, 167*2};
//    private final int[] posRightTricep = {416*2, 144*2};


//    //Rectangles where to place the muscle pics
//    private final Rect dstRectLeftTrap = new Rect(posLeftTrap[0], posLeftTrap[1],
//                    posLeftTrap[0] + srcRectLeftTrap.right,posLeftTrap[1] + srcRectLeftTrap.bottom);
//
//    private final Rect dstRectRightTrap = new Rect(posRightTrap[0], posRightTrap[1],
//                    posRightTrap[0] + srcRectRightTrap.right,posRightTrap[1] + srcRectRightTrap.bottom);
//
//    private final Rect dstRectLeftDelt = new Rect(posLeftDelt[0], posLeftDelt[1],
//                    posLeftDelt[0] + srcRectLeftDelt.right,posLeftDelt[1] + srcRectLeftDelt.bottom);
//
//    private final Rect dstRectRightDelt = new Rect(posRightDelt[0], posRightDelt[1],
//            posRightDelt[0] + srcRectRightDelt.right ,posRightDelt[1] + srcRectRightDelt.bottom);
//
//    private final Rect dstRectLeftTricep = new Rect(posLeftTricep[0], posLeftTricep[1],
//            posLeftTricep[0] + srcRectLeftTricep.right,posLeftTricep[1] + srcRectLeftTricep.bottom);
//
//    private final Rect dstRectRightTricep = new Rect(posRightTricep[0], posRightTricep[1],
//            posRightTricep[0] + srcRectRightTricep.right,posRightTricep[1] + srcRectRightTricep.bottom);

    /** reads image muscle imagefiles and **/

    public void getMuscleCropAreas(int number_of_muscles){
        for(int i=1;i<number_of_muscles;i++){
            // get muscle bitmap's top left and bottom right coordinate points
            musclePicAreas.add(getPicDimension(pics.get(i)));
        }


    }

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

        canvas.drawBitmap(pics.get(1), musclePicAreas.get(LEFT_TRAPEZOID),musclePicAreas.get(LEFT_TRAPEZOID),p);

        p.setAlpha(sensorValues[1]);


        canvas.drawBitmap(pics.get(2), musclePicAreas.get(RIGHT_TRAPEZOID),musclePicAreas.get(RIGHT_TRAPEZOID),p);
        p.setAlpha(sensorValues[2]);
        canvas.drawBitmap(pics.get(3), musclePicAreas.get(LEFT_DELTOID), musclePicAreas.get(LEFT_DELTOID), p);

        p.setAlpha(sensorValues[3]);
        canvas.drawBitmap(pics.get(4), musclePicAreas.get(RIGHT_DELTOID), musclePicAreas.get(RIGHT_DELTOID), p);

        p.setAlpha(sensorValues[4]);
        canvas.drawBitmap(pics.get(5), musclePicAreas.get(LEFT_TRICEPS), musclePicAreas.get(LEFT_TRICEPS), p);

        p.setAlpha(sensorValues[5]);
        canvas.drawBitmap(pics.get(6), musclePicAreas.get(RIGHT_TRICEPS), musclePicAreas.get(RIGHT_TRICEPS), p);

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
    public Rect getPicDimension(Bitmap bitmap){

        int x_upper_left=bitmap.getWidth();

        int y_upper_left=bitmap.getHeight();
        int x_lower_right=0;
        int y_lower_right=0;
        System.out.println("initial top x:"+x_upper_left+" top y:"+y_upper_left);

        for(int y=0; y<bitmap.getHeight();y++){
            for(int x=0;x<bitmap.getWidth();x++){
                if(okToDrawToPixel(bitmap,x,y)){
                    if(x<x_upper_left){
                        x_upper_left=x;
                        if(x==138){
                            System.out.println("löytyi");
                        }
                    }
                    if(y<y_upper_left){y_upper_left=y;}
                    if(x>x_lower_right){x_lower_right=x;}
                    if(y>y_lower_right){
                        y_lower_right=y;}
                }
            }
        }
        Rect res=new Rect(x_upper_left,y_upper_left,x_lower_right,y_lower_right);
        return res;
    }
}
