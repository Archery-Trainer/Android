package com.archery.tessa.homescreen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.LinkedList;

import static android.graphics.Color.parseColor;

/**
 * Created by mkkvj on 1.2.2018.
 */

public class OurView extends SurfaceView implements Runnable {
    private Thread myThread;
    private SurfaceHolder holder;
    boolean isOK=false;
    private LinkedList<Bitmap> pics;

    private int sensor1;
    private int sensor2;
    private int sensor3;
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




    private void init(){

        myThread=new Thread(this);
        holder=getHolder();
        pics= new LinkedList<>();
        isOK=true;
        System.out.println("init function");
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                System.out.println("surfaceCreated");
                myThread.start();

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                boolean retry=true;
                isOK=false;
                //myThread.setRunning(false);
                while (retry){
                    try {
                        myThread.join();
                        retry=false;
                    }catch (InterruptedException e){

                    }
                }
            }
        });
    }
    public void setdPicsForDrawing(Bitmap pic){
        pics.add(pic);
    }

    @Override
    public void run() {
        while(isOK==true){

            if(!holder.getSurface().isValid()){
                continue;
            }
            Canvas canvas=holder.lockCanvas();
            Matrix matrix = new Matrix();
            matrix.setRotate(0,pics.get(0).getWidth()/2,pics.get(0).getHeight()/2);
            //canvas.drawBitmap(pics.get(0),matrix,null);
            //matrix.setRotate(0,pics.get(1).getWidth()/2,pics.get(1).getHeight()/2);
            //Paint paint = new Paint();
            //paint.setColor(Color.RED);

            System.out.println("ismutable "+ pics.get(0).isMutable());
            Bitmap tmp =pics.get(0);
            // changing muscle colors to base values
            changeColor(tmp, "#FFAEC9","#00FFFB");
            changeColor(tmp, "#FFC90E","#00FFFB");
            changeColor(tmp, "#52D132","#00FFFB");


            //@TODO: canvas can be null
            canvas.drawBitmap(tmp,matrix,null);
            holder.unlockCanvasAndPost(canvas);


        }

    }
    boolean changeColor(Bitmap bitmap, String originalColor, String newColor)
    {
        // bitmap must be mutable and 32 bits per pixel:
        if((bitmap.getConfig() != Bitmap.Config.ARGB_8888) || !bitmap.isMutable())
            return false;
        int orgcolor=parseColor(originalColor);
        int newcolor=parseColor(newColor);
        System.out.println("rEd"+orgcolor);

        int pixelCount = bitmap.getWidth() * bitmap.getHeight()*2;
        //IntBuffer buffer = IntBuffer.allocate(pixelCount);
        //bitmap.copyPixelsToBuffer(buffer);
        System.out.println("chancecolor " + orgcolor );
        int count=0;

        for(int x = 0; x<bitmap.getWidth(); x++){
            for(int y = 0; y<bitmap.getHeight(); y++){
                if(bitmap.getPixel(x, y) == orgcolor){
                    bitmap.setPixel(x, y, newcolor);
                    count++;
                }
            }
        }
        System.out.println(count);
        return true;

//        int[] array = buffer.array();
//        int count=0;
//        for(int i = 0; i < pixelCount; i++)
//        {
//            if(array[i] == originalColor)
//                array[i] = newColor;
//                count++;
//        }
//        bitmap.copyPixelsFromBuffer(buffer);
//        System.out.println(count);
//        return true;
    }

}
