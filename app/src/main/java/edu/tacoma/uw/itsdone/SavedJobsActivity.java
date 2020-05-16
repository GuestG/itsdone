package edu.tacoma.uw.itsdone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import edu.tacoma.uw.itsdone.model.Job;

/**
 * shows all of the jobs the user saved.
 *
 * @author Trevor Peters, Max Malyshev
 * @version 1.0
 * @since 5/15/2020
 */
public class SavedJobsActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private List<Job> mJobList;
    private JSONObject mMemberOutJSON;
    private String mSavedJobs = "SAVEDJOBS";
    private RecyclerView mRecyclerView;

    /**
     * Initializes the activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_jobs);
        SharedPreferences sharedPref =getApplicationContext().getApplicationContext().
                getSharedPreferences("userInfo", 0);

        mMemberOutJSON = new JSONObject();
        try{
            mMemberOutJSON.put("memberID", sharedPref.getInt(getString(R.string.memberID), 0));
        } catch (JSONException e){
            Toast.makeText(this,"Error with JSON creation in SavedJobsActivity: " +
                            e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        mRecyclerView = findViewById(R.id.item_list);
        assert mRecyclerView != null;
        setupRecyclerView((RecyclerView) mRecyclerView);
    }

    /**
     * resumes the activity. Calls the execute method of the private job tasks class.
     */
    @Override
    protected void onResume(){
        super.onResume();
        if (mJobList == null){
            new SavedJobsActivity.JobTask().execute(getString(R.string.my_saved_jobs));
        }
    }

    /**
     * sets up the recycler view.
     * @param recyclerView the recycler view to setup.
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if (mJobList != null) {
            recyclerView.setAdapter(new SavedJobsActivity.SimpleItemRecyclerViewAdapter(
                    this, mJobList, mTwoPane));
        }

    }

    /**
     * A sub class for storing the recycler view data.
     */
    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SavedJobsActivity.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final SavedJobsActivity mParentActivity;
        private final List<Job> mValues;
        private final boolean mTwoPane;

        /**
         * Sets the action for when a user clicks a job
         */
        private final View.OnClickListener mOnClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Job item = (Job) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putSerializable(JobDetailFragment.ARG_ITEM_ID, item);
                    JobDetailFragment fragment = new JobDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, JobDetailActivity.class);
                    intent.putExtra(JobDetailFragment.ARG_ITEM_ID, item);

                    context.startActivity(intent);
                }
            }
        };

        /**
         * Creates  a view adapter item
         * @param parent
         * @param items
         * @param twoPane
         */
        SimpleItemRecyclerViewAdapter(SavedJobsActivity parent,
                                      List<Job> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        /**
         * Returns a view older object after it is created.
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public SavedJobsActivity.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.job_list_content, parent, false);
            return new SavedJobsActivity.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        /**
         * Initializes and binds view and view holder
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(final SavedJobsActivity.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).getJobId());
            holder.mContentView.setText(mValues.get(position).getTitle());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        /**
         * gets the number of jobs in the activity
         * @return the number of jobs in the activity
         */
        @Override
        public int getItemCount() {
            return mValues.size();
        }

        /**
         * subclass for storing the view objects
         */
        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }

    /**
     * Private sub class used for getting jobs from the database
     */
    private class JobTask extends AsyncTask<String, Void, String> {
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
                    Log.i(mSavedJobs, mMemberOutJSON.toString());
                    wr.write(mMemberOutJSON.toString());
                    wr.flush();
                    wr.close();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to download the list of jobs, Reason: "
                            + e.getMessage();
                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * Helper method for finishing the JSON parsing of jobs.
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), "Unable to download" + s, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getBoolean("success")) {
                    mJobList = Job.parseJobJson(
                            jsonObject.getString("names"));
                    if (!mJobList.isEmpty()) {
                        setupRecyclerView((RecyclerView) mRecyclerView);
                    }
                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

}

