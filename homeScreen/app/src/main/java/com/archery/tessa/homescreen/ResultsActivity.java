package com.archery.tessa.homescreen;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.archery.tessa.homescreen.models.Shot;
import com.archery.tessa.homescreen.tasks.GetShotsOfArcherTask;
import com.archery.tessa.homescreen.tasks.OnTaskCompleted;

import java.util.LinkedList;
import java.util.List;


public class ResultsActivity extends AppCompatActivity implements OnTaskCompleted  {

    private ListView recordingList;
    private List<Shot> shots;

    /**
     * Send the shots received from the server to the list view
     * @param o Type of the result
     */
    @Override
    public void onTaskCompleted(Object o) {
        shots = (List<Shot>) o;

        if(shots == null || shots.isEmpty())
            Toast.makeText(this, R.string.getShotsError, Toast.LENGTH_SHORT).show();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, shots);

        if(recordingList == null)
            recordingList = findViewById(R.id.recordingList);

        recordingList.setAdapter(adapter);
    }


    /**
     * Start viewing a recording
     * @param shotIx    Index of the shot to view
     */
    private void startSavedRecordingActivity(int shotIx) {
        Shot selectedShot = (Shot)recordingList.getItemAtPosition(shotIx);

        if(selectedShot == null) {
            System.out.println("Selected shot was NULL!");
            return;
        }

        Intent i = new Intent(this, SavedRecordingActivity.class);

        i.putExtra("SHOT", selectedShot);
        startActivity(i);
    }


    /**
     * Initialize the shot list by fetching shots from the server
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);

        recordingList = findViewById(R.id.recordingList);
        List<Shot> shots = new LinkedList<>();

        //Get recordings of this archer
        String archerEmail =  getIntent().getExtras().getString("ARCHER_EMAIL");
        GetShotsOfArcherTask getShotsTask = new GetShotsOfArcherTask(this, archerEmail, this);
        getShotsTask.execute();

        recordingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startSavedRecordingActivity(i);
            }
        });


        //Set action bar text
        ActionBar actionBar = getSupportActionBar();
        String header = getString(R.string.chooseRecording);

        actionBar.setTitle(header);



    }



}
