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

    public void updateSurface(MeasuredDataSet sensorvalues){
        //Bitmap tmp=pics.get(0);

        changeColor(pics.get(1), pics.get(1).getPixel(20,7),getMuscleColor(sensorvalues.getSensorData(0).getValue()));  //getPixel((227 * 2), (175 * 2))
        changeColor(pics.get(2), pics.get(2).getPixel(35,21),getMuscleColor(sensorvalues.getSensorData(1).getValue())); //getPixel((272 * 2), (150 * 2)
        changeColor(pics.get(3), pics.get(3).getPixel(25,70),getMuscleColor(sensorvalues.getSensorData(2).getValue())); //getPixel((320 * 2), (165 * 2))
        changeColor(pics.get(4), pics.get(4).getPixel(47,19), getMuscleColor(sensorvalues.getSensorData(3).getValue())); //getPixel((455 * 2), (158 * 2))
        changeColor(pics.get(5), pics.get(5).getPixel(34,24), getMuscleColor(sensorvalues.getSensorData(4).getValue())); //getPixel((400 * 2), (146 * 2))
        changeColor(pics.get(6), pics.get(6).getPixel(23,71), getMuscleColor(sensorvalues.getSensorData(5).getValue())); //getPixel((360 * 2), (160 * 2))
        drawSurface();

    }
    /** Returns mucle tension color based on sensor value**/
    private String getMuscleColor(int sensor_value){
        if(sensor_value<50){return "#00FFFB";}
        if(sensor_value<100){return "#0066FF";}
        if(sensor_value<225){return "#0008FF";}
        if(sensor_value<350){return "#9900FF";}
        if(sensor_value<475){return "#FF00FF";}
        if(sensor_value<550){return "#FF0095";}
        return "#FF0000";
    }

    public void init(){

        //myThread=new Thread(this);
        holder=getHolder();

        pics= new LinkedList<>();
        isOK=true;
        System.out.println("init function");

        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                System.out.println("surfaceCreated");
                //myThread.start();
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
    }
    public int getNumberOfPics(){return pics.size();}



    public void drawSurface(){
        Canvas canvas=holder.lockCanvas();
        System.out.println("run method");
        Matrix matrix = new Matrix();
        matrix.setRotate(0, pics.get(0).getWidth() / 2, pics.get(0).getHeight() / 2);

        //@TODO: canvas can be null
        if (canvas == null) {
            System.out.println("canvas null");
        }
        //System.out.println("num of pics" + pics.size());
        //canvas.drawBitmap(pics.get(0), matrix, null);  // base image of archer
        System.out.println("update picture");
        //System.out.println("num of pics" + pics.size());
        canvas.drawBitmap(pics.get(0), matrix, null);  // base image of archer
        canvas.drawBitmap(pics.get(1),new Rect(0,0,(82*2),(19*2)),new Rect((177*2),(166*2),(177*2)+(82*2),(166*2)+(18*2)), null);  //
        canvas.drawBitmap(pics.get(2),new Rect(0,0,(71*2),(43*2)),new Rect((231*2),(131*2),(231*2)+(71*2),(131*2)+(43*2)), null);
        canvas.drawBitmap(pics.get(3), new Rect(0,0,(50*2), (141*2)),new Rect((292*2),(105*2),(292*2)+(50*2),(105*2)+(141*2)), null);
        canvas.drawBitmap(pics.get(4),new Rect(0,0,(95*2), (39*2)),new Rect((414*2),(142*2),(414*2)+(95*2),(142*2)+(39*2)), null);
        canvas.drawBitmap(pics.get(5), new Rect(0,0,(69*2), (49*2)),new Rect((372*2),(127*2),(372*2)+(69*2),(127*2)+(49*2)), null);
        canvas.drawBitmap(pics.get(6), new Rect(0,0,(46*2), (143*2)),new Rect((338*2),(107*2),(338*2)+(46*2),(107*2)+(143*2)), null);

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



    boolean changeColor(Bitmap bitmap, int originalColor, String newColor)
    {
        // bitmap must be mutable and 32 bits per pixel:
        if((bitmap.getConfig() != Bitmap.Config.ARGB_8888) || !bitmap.isMutable())
            return false;
        //int orgcolor=parseColor(originalColor);
        int newcolor=parseColor(newColor);
        //System.out.println("rEd"+originalColor);


        int count=0;

        for(int x = 0; x<bitmap.getWidth(); x++){
            for(int y = 0; y<bitmap.getHeight(); y++){
                if(bitmap.getPixel(x, y) == originalColor){
                    bitmap.setPixel(x, y, newcolor);
                    count++;
                }
            }
        }
        System.out.println(count);
        return true;


    }

}
