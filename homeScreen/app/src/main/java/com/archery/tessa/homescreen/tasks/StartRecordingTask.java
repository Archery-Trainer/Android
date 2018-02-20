package com.archery.tessa.homescreen.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.archery.tessa.homescreen.R;
import com.archery.tessa.homescreen.models.RecordingRequest;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Timo on 29.1.2018.
 */

public class StartRecordingTask extends AsyncTask<Context, Void, HttpStatus> {

    private RecordingRequest request;

    public StartRecordingTask(RecordingRequest _request) {
        super();
        request = _request;
    }

    @Override
    protected HttpStatus doInBackground(Context... params) {
        if(params.length != 1) {
            System.out.println("Pass a context to the task");
            return null;
        }

        try {
            //Create rest template and json converter
            RestTemplate restTemplate = new RestTemplate();
            MappingJackson2HttpMessageConverter msgConverter = new MappingJackson2HttpMessageConverter();
            restTemplate.getMessageConverters().add(msgConverter);


            Gson gson = new Gson();

            HttpEntity<RecordingRequest> entity = new HttpEntity<>(request);

            //Send POST
            String url = params[0].getString(R.string.back_end_url) + "/startRecording";
            ResponseEntity<ObjectNode> res = restTemplate.postForEntity(url, entity, ObjectNode.class);

            System.out.println("Requesting recording start: " + request.toString());

            return res.getStatusCode();

        } catch (Exception e) {
            Log.e("startRecording", e.getMessage(), e);
            return null;
        }
    }
}
