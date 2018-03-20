package com.archery.tessa.homescreen.tasks;


import android.content.Context;
import android.content.Entity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.archery.tessa.homescreen.R;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Task that sets a score for a shot
 */
public class SetScoreForShotTask extends AsyncTask<Void, Void, HttpStatus>{
    private int shotId;
    private int score;
    private Context context;


    /**
     * Construct task
     *
     * @param shotId    Id of the shot to modify
     * @param score     Score of the shot
     * @param context   Context reference to the parent activity
     */
    public SetScoreForShotTask(int shotId, int score, Context context) {
        this.shotId = shotId;
        this.score = score;
        this.context = context;
    }

    /**
     * Send the request to the server
     *
     * @param params
     * @return  Http status of the response
     */
    @Override
    protected HttpStatus doInBackground(Void... params) {

        try {
            //Create rest template and json converter
            RestTemplate restTemplate = new RestTemplate();
            MappingJackson2HttpMessageConverter msgConverter = new MappingJackson2HttpMessageConverter();
            restTemplate.getMessageConverters().add(msgConverter);


            //Send POST
            String url = context.getString(R.string.back_end_url) + "/setScoreForShot";

            Integer[] shotIdandScore = new Integer[]{shotId, score};

            HttpEntity<Integer[]> entity = new HttpEntity<>(shotIdandScore);

            ResponseEntity<HttpStatus> res = restTemplate.postForEntity(url, entity, HttpStatus.class);
            
            HttpStatus status = res.getBody();

            return status;

        } catch (Exception e) {
            System.out.println(R.string.setScoreError);
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Show error message to the user if the request failed
     * @param status    The response
     */
    @Override
    protected void onPostExecute(HttpStatus status) {
        if(status != HttpStatus.OK)
            Toast.makeText(context, R.string.setScoreError, Toast.LENGTH_SHORT).show();

    }
}
