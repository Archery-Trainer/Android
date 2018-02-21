package com.archery.tessa.homescreen.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.archery.tessa.homescreen.R;
import com.archery.tessa.homescreen.models.MeasuredDataSet;
import com.archery.tessa.homescreen.models.Shot;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Timo on 20.2.2018.
 */

public class GetSensorReadingsOfShotTask extends AsyncTask<Void, Void, List<MeasuredDataSet>> {

    private Context context;
    private int shotId;

    public GetSensorReadingsOfShotTask(Context context, int shotId) {
        super();
        this.context = context;
        this.shotId = shotId;
    }

    @Override
    protected List<MeasuredDataSet> doInBackground(Void... voids) {
        System.out.println("Requesting for sensor values of shot " + shotId);

        List<MeasuredDataSet> readings = new LinkedList<>();


        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter msgConverter = new MappingJackson2HttpMessageConverter();
        restTemplate.getMessageConverters().add(msgConverter);

        HttpEntity<?> entity = new HttpEntity<>(shotId);

        //Send POST
        String url = context.getString(R.string.back_end_url) + "/getSensorDataOfShot";

        ResponseEntity<MeasuredDataSet[]> res = restTemplate.postForEntity(url, entity, MeasuredDataSet[].class);


        if(res == null) {
            System.out.println("Unable to fetch shots");
            return readings;
        }

        for(MeasuredDataSet s : res.getBody()) {
            readings.add(s);
        }

        return readings;




    }


    @Override
    protected void onPostExecute(List<MeasuredDataSet> readings) {
        if(readings == null || readings.isEmpty()) {
            System.out.println("Unable to fetch sensor readings");
            return;
        }

        //Show in UI...

        for(MeasuredDataSet s : readings)
            System.out.println(s.toString());

    }

}