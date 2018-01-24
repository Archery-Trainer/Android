package com.archery.tessa.homescreen.tasks;

import com.archery.tessa.homescreen.SessionActivity;
import com.archery.tessa.homescreen.models.Archer;
import com.archery.tessa.homescreen.R;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class CreateArcherTask extends AsyncTask<Context, Void, Archer> {

    private Archer archer;

    /**
     * Construct task that will send an archer to serveer
     *
     * @param a     The archer to create
     */
    public CreateArcherTask(Archer a) {
        super();
        archer = a;
    }


    /**
     * Send a http request to the server to create a new archer
     *
     * @param params    reference to the caller activity's context
     * @return
     */
    @Override
    protected Archer doInBackground(Context... params) {
        if(params.length != 1) {
            System.out.println("Pass a context to the task");
            return null;
        }

        try {
            //Create rest template and json converter
            RestTemplate restTemplate = new RestTemplate();
            MappingJackson2HttpMessageConverter msgConverter = new MappingJackson2HttpMessageConverter();
            restTemplate.getMessageConverters().add(msgConverter);

            //Create test archer and a HTTP entity from it
            Gson gson = new Gson();
            HttpEntity<Archer> entity = new HttpEntity<Archer>(archer);

            //Send POST
            String url = params[0].getString(R.string.back_end_url) + "/createArcher";
            ResponseEntity<ObjectNode> res = restTemplate.postForEntity(url, entity, ObjectNode.class);

            System.out.println("Archer " + archer.toString() + " sent to server");

            //@TODO: Maybe we should return a boolean that tells whether creation was successfull
            Archer responseObject = gson.fromJson(res.getBody().toString(), Archer.class);
            return responseObject;

        } catch (Exception e) {
            Log.e("createArcher", e.getMessage(), e);
            return null;
        }

    }

    @Override
    protected void onPostExecute(Archer a) {
        if(a == null) {
            System.out.println("Response was null");
            return;
        }

        System.out.println("Response from server: " + a.toString());
    }

}
