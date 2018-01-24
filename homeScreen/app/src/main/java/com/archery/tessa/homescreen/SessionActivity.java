package com.archery.tessa.homescreen;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;;
import android.widget.TextView;

import java.util.Random;


public class SessionActivity extends AppCompatActivity {


        public static int max1, max2, max3, max4, max5, max6 = 0;

        private static Random rand = new Random();

        //max value in views
        final int LIMIT = 700;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.session_activity);

        }

        @Override
        protected void onResume() {

                //Start a thread that gets the "sensor values" and updates the view
                final Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                        public void run() {
                                while (true) {
                                        try {
                                                Thread.sleep(200);
                                        }
                                        catch (InterruptedException e) {
                                                e.printStackTrace();
                                        }


                                        handler.post(new Runnable(){
                                                public void run() {

                                                        //Get random ints and place them in the view
                                                        int val1 = rand.nextInt(LIMIT);
                                                        if(val1 > max1) {
                                                                TextView textvmax =  findViewById(R.id.sensormaxval1);
                                                                textvmax.setText(Integer.toString(max1));
                                                                max1 = val1;
                                                        }
                                                        TextView txtv1 = findViewById(R.id.sensorval1);
                                                        String val1str = Integer.toString(val1);
                                                        txtv1.setText(val1str);

                                                        int val2 = rand.nextInt(LIMIT);
                                                        if(val2 > max2) {
                                                                TextView textvmax =  findViewById(R.id.sensormaxval2);
                                                                textvmax.setText(Integer.toString(max2));
                                                                max2 = val2;
                                                        }
                                                        TextView txtv2 = findViewById(R.id.sensorval2);
                                                        txtv2.setText(Integer.toString(val2));

                                                        int val3 = rand.nextInt(LIMIT);
                                                        if(val3 > max3) {
                                                                TextView textvmax =  findViewById(R.id.sensormaxval3);
                                                                textvmax.setText(Integer.toString(max3));
                                                                max3 = val3;
                                                        }
                                                        TextView txtv3 = findViewById(R.id.sensorval3);
                                                        txtv3.setText(Integer.toString(val3));

                                                        int val4 = rand.nextInt(LIMIT);
                                                        if(val4 > max4) {
                                                                TextView textvmax =  findViewById(R.id.sensormaxval4);
                                                                textvmax.setText(Integer.toString(max4));
                                                                max4 = val4;
                                                        }
                                                        TextView txtv4 = findViewById(R.id.sensorval4);
                                                        txtv4.setText(Integer.toString(val4));

                                                        int val5 = rand.nextInt(LIMIT);
                                                        if(val5 > max5) {
                                                                TextView textvmax =  findViewById(R.id.sensormaxval5);
                                                                textvmax.setText(Integer.toString(max5));
                                                                max5 = val5;
                                                        }
                                                        TextView txtv5 = findViewById(R.id.sensorval5);
                                                        txtv5.setText(Integer.toString(val2));

                                                        int val6 = rand.nextInt(LIMIT);
                                                        if(val6 > max6) {
                                                                TextView textvmax =  findViewById(R.id.sensormaxval6);
                                                                textvmax.setText(Integer.toString(max6));
                                                                max6 = val6;
                                                        }
                                                        TextView txtv6 = findViewById(R.id.sensorval6);
                                                        txtv6.setText(Integer.toString(val2));

                                                }
                                        });
                                }
                        }
                };
                new Thread(runnable).start();


                super.onResume();

        }
}