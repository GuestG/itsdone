package edu.tacoma.uw.itsdone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import edu.tacoma.uw.itsdone.model.Job;

/**
 * a fragment for adding jobs
 * @author max malyshev
 * @since 5/10/2020
 */
public class JobAddFragment extends Fragment {

    private AddListener mAddListener;

    public interface AddListener {
        public void addJob(Job job);
    }

    public JobAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddListener = (AddListener) getActivity();
    }

    /**
     * creates the view and also sets up the on click for the add button
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return returns the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_job_add, container
                , false);
        getActivity().setTitle("Create a job");
        final EditText jobTitleEditText = v.findViewById(R.id.add_job_title);
        final EditText jobShortDescEditText = v.findViewById(R.id.add_job_short_desc);
        final EditText jobLongDescEditText = v.findViewById(R.id.add_job_long_desc);
        final EditText jobLocationEditText = v.findViewById(R.id.add_job_location);
        final EditText jobPriceEditText = v.findViewById(R.id.add_job_price);
        Button addButton = v.findViewById(R.id.btn_add_job);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jobTitle = jobTitleEditText.getText().toString();
                String jobShortDesc = jobShortDescEditText.getText().toString();
                String jobLongDesc = jobLongDescEditText.getText().toString();
                String jobLocation = jobLocationEditText.getText().toString();
                String jobPrice = jobPriceEditText.getText().toString();
                Job job = new Job("id","creatorId", jobTitle, jobShortDesc, jobLongDesc, jobLocation, jobPrice);
                if (mAddListener != null) {
                    mAddListener.addJob(job);
                }
            }
        });
        return v;
    }
}
