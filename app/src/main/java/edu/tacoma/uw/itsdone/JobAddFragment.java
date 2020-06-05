package edu.tacoma.uw.itsdone;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.tacoma.uw.itsdone.model.Job;

/**
 * Class for controlling the job add activity
 * @author Max Malyshev
 * @version 1.0
 * @since 2020-05-13
 */
public class JobAddFragment extends Fragment {

    private AddListener mAddListener;
    private int photoIndex;

    /**
     * interface for connecting the add job button to the add job fragment
     */
    public interface AddListener {
        public void addJob(Job job);
    }

    /**
     * Required empty public constructor.
     */
    public JobAddFragment() {}

    /**
     * Creates the Activity instance.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddListener = (AddListener) getActivity();
    }

    /**
     * Adds jobs to the list display, and adds a button listener to the add job button.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the view of the activity
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
        int[] ids = {R.id.photo0,R.id.photo1,R.id.photo2,R.id.photo3,R.id.photo4,R.id.photo5,
                R.id.photo6,R.id.photo7,R.id.photo8,R.id.photo9};
        final int[] integers = {0,1,2,3,4,5,6,7,8,9}; //this is so dumb
        for (int i = 0; i < 10; i++){
            Button PhotoButton = v.findViewById(ids[i]);
            final int index = i;
            PhotoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photoIndex = index;
                    Toast.makeText(getContext(), "picture button clicked. id=" + index,
                            Toast.LENGTH_LONG).show();
                }
            });
        }

        Button addButton = v.findViewById(R.id.btn_add_photo);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "adding job. photo id =" + photoIndex,
                        Toast.LENGTH_LONG).show();
                if ((jobPriceEditText.getText().toString().matches("[0-9]+"))) {
                    String jobTitle = jobTitleEditText.getText().toString();
                    String jobShortDesc = jobShortDescEditText.getText().toString();
                    String jobLongDesc = jobLongDescEditText.getText().toString();
                    String jobLocation = jobLocationEditText.getText().toString();
                    String jobPrice = jobPriceEditText.getText().toString();
                    Job job = new Job("id","creatorId", jobTitle, jobShortDesc, jobLongDesc, jobLocation, jobPrice);
                    job.setPicture(photoIndex);
                    if (mAddListener != null) {
                        mAddListener.addJob(job);
                    }
                } else {
                    Toast.makeText(getContext(), "please enter valid  price (ie 10) ",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        return v;
    }
}
