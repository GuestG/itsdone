package edu.tacoma.uw.itsdone;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.tacoma.uw.itsdone.model.Job;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link JobListActivity}
 * in two-pane mode (on tablets) or a {@link JobDetailActivity}
 * on handsets.
 */
public class JobDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Job mJob;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public JobDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mJob = (Job) getArguments().getSerializable(ARG_ITEM_ID);

        }
    }

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

        return rootView;
    }

}
