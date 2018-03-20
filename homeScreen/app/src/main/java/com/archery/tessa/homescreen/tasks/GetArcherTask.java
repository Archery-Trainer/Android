
package com.archery.tessa.homescreen.tasks;

        import android.content.Context;
        import android.os.AsyncTask;

        import com.archery.tessa.homescreen.R;
        import com.archery.tessa.homescreen.models.Archer;

        import org.springframework.http.HttpEntity;
        import org.springframework.http.ResponseEntity;
        import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
        import org.springframework.web.client.RestTemplate;

        import java.util.LinkedList;
        import java.util.List;

/**
 * Created by Timo on 6.2.2018.
 */

/**
 * Task that gets a single archer object
 */
public class GetArcherTask extends AsyncTask<Void, Void, Archer> {

    private Context context;
    private String email;

    /**
     * Construct task
     *
     * @param context   Context of the parent activity
     * @param email     Email of the archer to get
     */
    public GetArcherTask(Context context, String email) {
        super();
        this.context = context;
        this.email = email;
    }

    /**
     * Send the request to the server
     * @param params
     * @return  The received archer object
     */
    @Override
    protected Archer doInBackground(Void... params) {

        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter msgConverter = new MappingJackson2HttpMessageConverter();
        restTemplate.getMessageConverters().add(msgConverter);


        HttpEntity<String> entity = new HttpEntity<String>(email);


        //Send POST
        String url = context.getString(R.string.back_end_url) + "/getArcher";
        ResponseEntity<Archer> res = restTemplate.postForEntity(url, entity, Archer.class);

        if(res == null) {
            System.out.println("Unable to fetch archer");
        }

        Archer a = res.getBody();

        return a;
    }

    /**
     * Print out the received archer
     * @param a
     */
    @Override
    protected void onPostExecute(Archer a) {
        if(a == null){
            System.out.println("Unable to fetch archer");

            return;
        }

        //Show archer in UI and save to some session state?


        System.out.println(a.toString());
    }
}
