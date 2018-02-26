package com.archery.tessa.homescreen.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
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

public class OurView extends SurfaceView{ // implements Runnable{
    private Thread myThread;
    private SurfaceHolder holder;
    boolean isOK=false;
    private LinkedList<Bitmap> pics;
    private LinkedList<Bitmap> originalPics;

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

    public void updateSurface(MeasuredDataSet sensorvalues, boolean[] showMuscle){
        //Bitmap tmp=pics.get(0);

        int muscleTension = 0;

        muscleTension = (showMuscle[0]) ? sensorvalues.getSensorData(0).getValue() : 0;
        changeColor(pics.get(1),
            originalPics.get(1),
            getMuscleColor(muscleTension));

        muscleTension = (showMuscle[1]) ? sensorvalues.getSensorData(1).getValue() : 0;
        changeColor(pics.get(2),
                originalPics.get(2),
                getMuscleColor(muscleTension));

        muscleTension = (showMuscle[2]) ? sensorvalues.getSensorData(2).getValue() : 0;
        changeColor(pics.get(3),
                originalPics.get(3),
                getMuscleColor(muscleTension));

        muscleTension = (showMuscle[3]) ? sensorvalues.getSensorData(3).getValue() : 0;
        changeColor(pics.get(4),
                originalPics.get(4),
                getMuscleColor(muscleTension));

        muscleTension = (showMuscle[4]) ? sensorvalues.getSensorData(4).getValue() : 0;
        changeColor(pics.get(5),
                originalPics.get(5),
                getMuscleColor(muscleTension));

        muscleTension = (showMuscle[5]) ? sensorvalues.getSensorData(5).getValue() : 0;
        changeColor(pics.get(6),
                originalPics.get(6),
                getMuscleColor(muscleTension));

        drawSurface();
    }

    /** Returns mucle tension color based on sensor value**/
    private String getMuscleColor(int sensor_value){

        /** Grayscale **/
        /*
        if(sensor_value<50){return "#FFFFFF";}
        if(sensor_value<100){return "#EEEEEE";}
        if(sensor_value<150){return "#DDDDDD";}
        if(sensor_value<200){return "#CCCCCC";}
        if(sensor_value<250){return "#BBBBBB";}
        if(sensor_value<300){return "#AAAAAA";}
        if(sensor_value<350){return "#999999";}
        if(sensor_value<400){return "#888888";}
        if(sensor_value<450){return "#777777";}
        if(sensor_value<500){return "#666666";}
        if(sensor_value<550){return "#555555";}
        if(sensor_value<600){return "#444444";}
        if(sensor_value<650){return "#333333";}
        if(sensor_value<700){return "#222222";}
        if(sensor_value<750){return "#111111";}
        return "#000000";
        */

        /** Red **/

        if(sensor_value<50){return "#FFFFFF";}
        if(sensor_value<100){return "#F5EEEE";}
        if(sensor_value<150){return "#EBDDDD";}
        if(sensor_value<200){return "#E1CCCC";}
        if(sensor_value<250){return "#D7BBBB";}
        if(sensor_value<300){return "#CDAAAA";}
        if(sensor_value<350){return "#C39999";}
        if(sensor_value<400){return "#B98888";}
        if(sensor_value<450){return "#AF7777";}
        if(sensor_value<500){return "#A56666";}
        if(sensor_value<550){return "#9B5555";}
        if(sensor_value<600){return "#8C4444";}
        if(sensor_value<650){return "#7D3333";}
        if(sensor_value<700){return "#692222";}
        if(sensor_value<750){return "#551111";}
        return "#410000";


        /** Blue **/
        /*
        if(sensor_value<50){return "#FFFFFF";}
        if(sensor_value<100){return "#EEEEF5";}
        if(sensor_value<150){return "#DDDDEB";}
        if(sensor_value<200){return "#CCCCE1";}
        if(sensor_value<250){return "#BBBBD7";}
        if(sensor_value<300){return "#AAAACD";}
        if(sensor_value<350){return "#9999C3";}
        if(sensor_value<400){return "#8888B9";}
        if(sensor_value<450){return "#7777AF";}
        if(sensor_value<500){return "#6666A5";}
        if(sensor_value<550){return "#55559B";}
        if(sensor_value<600){return "#44448C";}
        if(sensor_value<650){return "#33337D";}
        if(sensor_value<700){return "#222269";}
        if(sensor_value<750){return "#111155";}
        return "#000041";
        */
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
                drawSurface();
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



    public void drawSurface(){
        Canvas canvas=holder.lockCanvas();

        Matrix matrix = new Matrix();
        matrix.setRotate(0, pics.get(0).getWidth() / 2, pics.get(0).getHeight() / 2);

        //@TODO: canvas can be null
        if (canvas == null) {
            System.out.println("canvas null");
            return;
        }
        //System.out.println("num of pics" + pics.size());
        //canvas.drawBitmap(pics.get(0), matrix, null);  // base image of archer
        System.out.println("update picture");
        //System.out.println("num of pics" + pics.size());
        canvas.drawBitmap(pics.get(0), matrix, null);  // base image of archer
        canvas.drawBitmap(pics.get(1), srcRectLeftTrap, dstRectLeftTrap, null);
        canvas.drawBitmap(pics.get(2), srcRectRightTrap, dstRectRightTrap, null);
        canvas.drawBitmap(pics.get(3), srcRectLeftDelt, dstRectLeftDelt, null);
        canvas.drawBitmap(pics.get(4), srcRectRightDelt, dstRectRightDelt, null);
        canvas.drawBitmap(pics.get(5), srcRectLeftTricep, dstRectLeftTricep, null);
        canvas.drawBitmap(pics.get(6), srcRectRightTricep, dstRectRightTricep, null);

        holder.unlockCanvasAndPost(canvas);

    }
//=======
//    @Override
//    public void run() {
//        while(isOK==true){
//
//            if(!holder.getSurface().isValid()){
//                continue;
//            }
//            Canvas canvas=holder.lockCanvas();
//
//            if(canvas == null)
//                return;
//
//            Matrix matrix = new Matrix();
//            matrix.setRotate(0,pics.get(0).getWidth()/2,pics.get(0).getHeight()/2);
//            //canvas.drawBitmap(pics.get(0),matrix,null);
//            //matrix.setRotate(0,pics.get(1).getWidth()/2,pics.get(1).getHeight()/2);
//            //Paint paint = new Paint();
//            //paint.setColor(Color.RED);
//
//            System.out.println("ismutable "+ pics.get(0).isMutable());
//            Bitmap tmp =pics.get(0);
//            // changing muscle colors to base values
//            changeColor(tmp, "#FFAEC9","#00FFFB");
//            changeColor(tmp, "#FFC90E","#00FFFB");
//            changeColor(tmp, "#52D132","#00FFFB");
//
//            canvas.drawBitmap(tmp,matrix,null);
//            holder.unlockCanvasAndPost(canvas);
//>>>>>>> cc8b04dd08db2f8c8d37dbd8acb903fcc0f4988c


    private boolean okToDrawToPixel(Bitmap originalPic, int x, int y) {

        int color = originalPic.getPixel(x, y);

        if(color == 0) {
            //Pixel is white, part of background
            return false;
        }

        return true;
    }

    boolean changeColor(Bitmap bitmap, Bitmap originalPic, String newColor)
    {
        // bitmap must be mutable and 32 bits per pixel:
        if((bitmap.getConfig() != Bitmap.Config.ARGB_8888) || !bitmap.isMutable())
            return false;
        int newcolor=parseColor(newColor);

        for(int x = 0; x<bitmap.getWidth(); x++){
            for(int y = 0; y<bitmap.getHeight(); y++){
                if(okToDrawToPixel(originalPic, x, y)){

                    bitmap.setPixel(x, y, newcolor);
                }
            }
        }

        return true;


    }

}
