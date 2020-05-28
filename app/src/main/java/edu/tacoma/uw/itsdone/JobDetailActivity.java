package edu.tacoma.uw.itsdone;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import edu.tacoma.uw.itsdone.model.Job;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link JobListActivity}.
 *
 * @author Trevor Peters, Max Malsyev
 * @version 1.2
 * @since 5/27/2020
 */
public class JobDetailActivity extends AppCompatActivity implements JobAddFragment.AddListener, JobDetailFragment.SaveListener{

    private int mUserId;
    public static final String ADD_JOB = "ADD_JOB";
    private JSONObject mJobJSON;

    /**
     * initializes the activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            if (getIntent().getSerializableExtra(JobDetailFragment.ARG_ITEM_ID) != null) {
                arguments.putSerializable(JobDetailFragment.ARG_ITEM_ID,
                        getIntent().getSerializableExtra(JobDetailFragment.ARG_ITEM_ID));
                JobDetailFragment fragment = new JobDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.item_detail_container, fragment)
                        .commit();
            } else if (getIntent().getBooleanExtra(JobDetailActivity.ADD_JOB, false)) {
                JobAddFragment fragment = new JobAddFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.item_detail_container, fragment)
                        .commit();
            }
        }
    }

    /**
     * method for saving a job to the users saved jobs.
     * these are the jobs that the user accepts and is
     * willing to complete.
     * @param job the job to accept.
     */
    @Override
    public void saveJob(Job job){
        StringBuilder url = new StringBuilder(getString(R.string.save_job));

        mJobJSON = new JSONObject();
        try{
            int memberID = getApplicationContext().getSharedPreferences("userInfo", 0)
                    .getInt(getString(R.string.memberID), 0);
            mJobJSON.put("memberID", memberID);
            mJobJSON.put("jobID", job.getJobId());
            new AddJobAsyncTask().execute(url.toString());

        } catch (JSONException e){
            Toast.makeText(this,"Error with JSON creation on adding a job: " +
                            e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * method for allowing the user to create a new job.
     * This job gets put to the general job database and will
     * be visible to all other users. Anyone who is willing
     * can accept this job.
     * @param job The job to create.
     */
    @Override
    public void addJob(Job job) {
        StringBuilder url = new StringBuilder(getString(R.string.add_job));

        mJobJSON = new JSONObject();
        try{
            String username = getApplicationContext().getSharedPreferences("userInfo", 0)
                    .getString(getString(R.string.username), null);
            if (username == null){
                throw new NullPointerException();
            }
            mJobJSON.put("creatorUsername", username);
            mJobJSON.put("title", job.getTitle());
            mJobJSON.put("shortDesc", job.getShortDesc());
            mJobJSON.put("longDesc", job.getLongDesc());
            mJobJSON.put("place", job.getLocation());
            mJobJSON.put("price", job.getPrice());
            new AddJobAsyncTask().execute(url.toString());

        } catch (JSONException e){
            Toast.makeText(this,"Error with JSON creation on adding a job: " +
                            e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }  catch (NullPointerException e ){
        Toast.makeText(this,"Error: please log out and log back in before creating a job",
                Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Private sub class for getting json files from the database.
     */
    private class AddJobAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter wr =
                            new OutputStreamWriter(urlConnection.getOutputStream());

                    // For Debugging
                    Log.i(ADD_JOB, mJobJSON.toString());
                    wr.write(mJobJSON.toString());
                    wr.flush();
                    wr.close();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to add the new Job, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * Helper method for getting json files from database.
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to add the new Job")) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("success")) {
                    Toast.makeText(getApplicationContext(), "successful"
                            , Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Job couldn't be added: "
                                    + jsonObject.getString("error")
                            , Toast.LENGTH_LONG).show();
                    Log.e(ADD_JOB, jsonObject.getString("error"));
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Parsing error on Adding Job"
                                + e.getMessage()
                        , Toast.LENGTH_LONG).show();
                Log.e(ADD_JOB, e.getMessage());
            }
        }
    }

    /**
     * Weather or not the item is selected
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            navigateUpTo(new Intent(this, JobListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




}
