package edu.tacoma.uw.itsdone;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import edu.tacoma.uw.itsdone.model.Job;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link JobDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 *
 * @author Trevor Peters, Max Malyshev
 * @version 1.1
 * @since 5/27/2020
 */
public class JobListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private List<Job> mJobList;
    private RecyclerView mRecyclerView;

    /**
     * initializes and creates the activity.
     * Overrides the AppCompatActivity on create method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        Button createJobBtn = findViewById(R.id.AddJobButton);
        createJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchJobAddFragment();
            }
        });

        Button showSavedJobsBtn = findViewById(R.id.FindJobButton);
        showSavedJobsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSavedJobActivity();
            }
        });


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
     * launches and displays the showJob activity.
     */
    private void launchSavedJobActivity() {
        Intent intent = new Intent(this, SavedJobsActivity.class);
        startActivity(intent);
    }

    /**
     * Launches and displays the jobAdd activity.
     */
    private void launchJobAddFragment() {
        JobAddFragment jobAddFragment = new JobAddFragment();
        if (mTwoPane){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, jobAddFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, JobDetailActivity.class);
            intent.putExtra(JobDetailActivity.ADD_JOB, true);
            startActivity(intent);
        }
    }

    /**
     * resumes the activity. Calls the execute method of the private job tasks class.
     */
    @Override
    protected void onResume(){
        super.onResume();
        if (mJobList == null){
            new JobTask().execute(getString(R.string.get_jobs));
        }
    }

    /**
     * sets up the recycler view.
     * @param recyclerView the recycler view to setup.
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if (mJobList != null) {
            recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(
                    this, mJobList, mTwoPane));
        }

    }

    /**
     * A sub class for storing the recycler view data.
     */
    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final JobListActivity mParentActivity;
        private final List<Job> mValues;
        private final boolean mTwoPane;
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
        SimpleItemRecyclerViewAdapter(JobListActivity parent,
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
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_item, parent, false);
            return new ViewHolder(view);
        }

        /**
         * Initializes and binds view and view holder
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            // array of photos. Should probably be stored somewhere else but this works for now
            int [] photos = {
                    R.drawable.a,
                    R.drawable.b,
                    R.drawable.d,
                    R.drawable.e,
                    R.drawable.f,
                    R.drawable.g,
                    R.drawable.h,
                    R.drawable.i,
                    R.drawable.j
            };
            holder.mIdView.setText("$"+mValues.get(position).getPrice());
            holder.mContentView.setText(mValues.get(position).getTitle());
            holder.mImageView.setImageResource(photos[mValues.get(position).getPicture()]);
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
            final ImageView mImageView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.card_uname);
                mContentView = (TextView) view.findViewById(R.id.card_title);
                mImageView = (ImageView) view.findViewById(R.id.card_background);
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
                Toast.makeText(getApplicationContext(), "Unable to download" + s, Toast.LENGTH_LONG)
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
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    public void profile(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
