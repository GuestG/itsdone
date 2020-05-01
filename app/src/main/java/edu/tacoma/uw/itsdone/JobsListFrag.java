package edu.tacoma.uw.itsdone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;


/**
 * A simple {@link Fragment} subclass.
 */
public class JobsListFrag extends Fragment {
    ListView lv;
    SearchView searchView;
    ArrayAdapter adapter;
    String[] data = {"1" , "2", "3", "4", "1" , "2", "3", "4", "1" , "2", "3", "4",
                     "1" , "2", "3", "4", "1" , "2", "3", "4", "1" , "2", "3", "4"};
    public JobsListFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.jobs_list_frag, container, false);
        lv = (ListView) view.findViewById(R.id.idListView);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, data);
        lv.setAdapter(adapter);
        // Inflate the layout for this fragment
        return view;
    }
}
