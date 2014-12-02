package com.flyerbox.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.flyerbox.R;

/**
 * Created by tmrafael on 13.11.2014.
 */
public class PollsFragment extends Fragment {
    public PollsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_polls, container, false);


        rootView.findViewById(R.id.questionTemp).setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadQuestions();
            }
        });



        return rootView;
    }

    private void loadQuestions() {
        //TODO: do something to load questions for this poll
        Fragment questionFragment = new ConcretePollFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, questionFragment).commit();
    }


}
