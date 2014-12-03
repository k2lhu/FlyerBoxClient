package com.flyerbox.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.flyerbox.R;
import com.flyerbox.logic.Poll;
import com.flyerbox.logic.PollAdapter;

import java.util.ArrayList;

/**
 * Created by tmrafael on 13.11.2014.
 */
public class PollsFragment extends Fragment {
    ListView pollsList;
    ArrayList<Poll> polls = new ArrayList<Poll>();
    int selectedPollID;

    public PollsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_polls, container, false);

        loadPolls();

        pollsList = (ListView) rootView.findViewById(R.id.pollsList);
        pollsList.setAdapter(new PollAdapter(getActivity(), polls));

        pollsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPollID = ((Poll)parent.getItemAtPosition(position)).getId();
                Log.d("Polls Fragment", "Selected poll id: " + selectedPollID);

                ConcretePollFragment questionFragment = new ConcretePollFragment();
                questionFragment.setPollID(selectedPollID);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, questionFragment).commit();

            }
        });

        return rootView;
    }

    private void loadPolls() {
        //TODO: parse JSON object and fill 'polls' in WHILE cycle. Below just an example. !!!!!!
        polls.add(new Poll("M'c Donalds", "", 300));
        polls.add(new Poll("Dafi", "", 12));
        polls.add(new Poll("Comfy", "", 1004));
        polls.add(new Poll("Eldorado", "", 463));
    }


}
