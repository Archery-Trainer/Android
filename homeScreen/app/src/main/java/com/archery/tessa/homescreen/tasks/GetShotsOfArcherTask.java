package com.archery.tessa.homescreen.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.archery.tessa.homescreen.R;
import com.archery.tessa.homescreen.models.Archer;
import com.archery.tessa.homescreen.models.Shot;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;


public class GetShotsOfArcherTask extends AsyncTask<Void, Void, List<Shot>> {

    private Context context;
    private String email;

    public GetShotsOfArcherTask(Context context, String email) {
        super();
        this.context = context;
        this.email = email;
    }

    @Override
    protected List<Shot> doInBackground(Void... voids) {

        List<Shot> shots = new LinkedList<>();


        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter msgConverter = new MappingJackson2HttpMessageConverter();
        restTemplate.getMessageConverters().add(msgConverter);

        HttpEntity<String> entity = new HttpEntity<String>(email);

        //Send POST
        String url = context.getString(R.string.back_end_url) + "/getShotsOfArcher";


        //@TODO: The date format ("Jan 1, 1970")cannot be converted to Date object. Should store those as long
        ResponseEntity<Shot[]> res = restTemplate.postForEntity(url, entity, Shot[].class);


        if(res == null) {
            System.out.println("Unable to fetch shots");
            return shots;
        }

        for(Shot s : res.getBody()) {
            shots.add(s);
        }

        return shots;




    }


    @Override
    protected void onPostExecute(List<Shot> shots) {
        if(shots == null || shots.isEmpty()) {
            System.out.println("Unable to fetch shots");
            return;
        }

        //Show shots in UI...

        for(Shot s : shots)
            System.out.println(s.toString());

    }

}
