package com.archery.tessa.homescreen.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.archery.tessa.homescreen.R;
import com.archery.tessa.homescreen.models.Archer;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Timo on 6.2.2018.
 */

/**
 * Task that retrieves all the registered archers from the database
 */
public class GetAllArchersTask extends AsyncTask<Void, Void, List<Archer>> {

    private Context context;

    /**
     * Construct the task
     * @param context       Context reference of the parent activity
     */
    public GetAllArchersTask(Context context) {
        super();
        this.context = context;
    }

    /**
     * Send the getAllArchers request to the server
     * @param params
     * @return  List of the saved Archer objects
     */
    @Override
    protected List<Archer> doInBackground(Void... params) {
        List<Archer> archers = new LinkedList<>();


        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter msgConverter = new MappingJackson2HttpMessageConverter();
        restTemplate.getMessageConverters().add(msgConverter);


        //Send POST
        String url = context.getString(R.string.back_end_url) + "/getAllArchers";
        ResponseEntity<Archer[]> res = restTemplate.postForEntity(url, null, Archer[].class);

        if(res == null) {
            System.out.println("Unable to fetch archers");
            return archers;
        }


        for(Archer a : res.getBody()) {
            archers.add(a);
        }

        return archers;
    }

    /**
     * Print out the received archers
     * @param archers
     */
    @Override
    protected void onPostExecute(List<Archer> archers) {
        if(archers == null || archers.isEmpty()){
            System.out.println("Unable to fetch archers");
            return;
        }

        //Send archers list to UI...

        for(Archer a : archers)
            System.out.println(a.toString());
    }
}
