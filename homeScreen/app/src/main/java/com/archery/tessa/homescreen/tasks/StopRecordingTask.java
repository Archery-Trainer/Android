package com.archery.tessa.homescreen.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.archery.tessa.homescreen.R;
import com.archery.tessa.homescreen.models.RecordingRequest;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Task that requests the server to stop recording
 */
public class StopRecordingTask extends AsyncTask<Context, Void, Integer> {

    private RecordingRequest request;
    private int shotId;

    /**
     * Construct the task
     */
    public StopRecordingTask() {
        super();
        shotId = -1;
    }

    /**
     * Get the index of the shot that the server created
     * @return  The id of the shot
     */
    public int getShotId() {
        return shotId;
    }

    /**
     * Send a stop recording request to server. Sensor data of the shot is stored to database.
     * @param params    none needed
     * @return          Id of the created Shot object
     */
    @Override
    protected Integer doInBackground(Context... params) {
        if(params.length != 1) {
            System.out.println("Pass a context to the task");
            return -1;
        }

        try {
            //Create rest template and json converter
            RestTemplate restTemplate = new RestTemplate();
            MappingJackson2HttpMessageConverter msgConverter = new MappingJackson2HttpMessageConverter();
            restTemplate.getMessageConverters().add(msgConverter);

            HttpEntity<String> entity = new HttpEntity<String>("");

            System.out.println("Requesting recording stop");

            //Send POST
            String url = params[0].getString(R.string.back_end_url) + "/stopRecording";
            ResponseEntity<Integer> res = restTemplate.postForEntity(url, null, Integer.class);

            if(res == null)
                return -1;

            shotId = res.getBody().intValue();

            System.out.println("Created a shot with id " + shotId);

            return shotId;

        } catch (Exception e) {
            Log.e("stopRecording", e.getMessage(), e);
            return -1;
        }
    }
}
