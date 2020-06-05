package edu.tacoma.uw.itsdone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.tacoma.uw.itsdone.model.Job;

/**
 * This fragment displays all info on the job, through the use of loading
 * data from the database.
 *
 * @authors Gehry Guest, Max Malyshev
 * @version 1.1
 * @since 2020-05-15
 */
public class JobDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The job this fragment is representing
     */
    private Job mJob;
    private SaveListener mSaveListener;
    private CancelListener mCancelListener;
    private CompleteListener mCompleteListener;
    private String mFromActivity; //activity that started the JobDetailFragment

    public interface SaveListener { void saveJob(Job job); }
    public interface CancelListener { void cancelJob(Job job); }
    public interface CompleteListener { void completeJob(Job job); }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public JobDetailFragment() {
    }

    /**
     * Initializes the fragment.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        mSaveListener = (SaveListener) getActivity();
        mCancelListener = (CancelListener) getActivity();
        mCompleteListener = (CompleteListener) getActivity();
        super.onCreate(savedInstanceState);
        mFromActivity = getArguments().getString("fromID");
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mJob = (Job) getArguments().getSerializable(ARG_ITEM_ID);

        }
    }

    /**
     * creates and initializes the fields in the view.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.job_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mJob != null) {
            ((TextView) rootView.findViewById(R.id.item_detail_short_desc)).setText(
                    mJob.getShortDesc());
            ((TextView) rootView.findViewById(R.id.item_detail_long_desc)).setText(
                    mJob.getLongDesc());
            ((TextView) rootView.findViewById(R.id.item_detail_location)).setText(
                    "Location: " + mJob.getLocation());
            ((TextView) rootView.findViewById(R.id.item_detail_price)).setText(
                    "Price: " + mJob.getPrice());
        }

        createButtons(rootView);

        return rootView;
    }

    /**
     * creates the correct buttons for the job. if a user clicks on their own job,
     * they can cancel that job. if the user is in their myJobs list. the option to
     * complete the job will appear.
     * @param rootView
     */
    private void createButtons(View rootView){
        SharedPreferences sharedPref = getContext().getApplicationContext().
                getSharedPreferences("userInfo", 0);

        Button saveButton = rootView.findViewById(R.id.save_job_button);

        //check where which activity the user came from
        if("JobListActivity".equals(mFromActivity)) {
            /** adds a save job listener to the save job button */
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSaveListener.saveJob(mJob);
                }
            });
        } else {
            saveButton.setText("Complete Job");
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCompleteListener.completeJob(mJob);
                }
            });
        }

        Button profileButton = rootView.findViewById(R.id.view_profile_button);
        //check if the user is looking at their own job
        if (mJob.getCreatorUsername().equals(sharedPref.getString(getString(R.string.username), null))) {
            profileButton.setText("cancel Job");
            profileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCancelListener.cancelJob(mJob);
                }
            });
        } else {
            profileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    intent.putExtra("username", mJob.getCreatorUsername());
                    startActivity(intent);
                }
            });
        }
    }

}
