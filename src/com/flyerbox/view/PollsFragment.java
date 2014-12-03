package com.flyerbox.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.flyerbox.R;
import com.flyerbox.logic.Poll;
import com.flyerbox.logic.PollAdapter;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tmrafael on 13.11.2014.
 */
public class PollsFragment extends Fragment {
    ListView pollsList;
    ArrayList<Poll> polls = new ArrayList<Poll>();
    int selectedPollID;
    private SharedPreferences sharedPreferences;
    private String response;
    private int pollsCount = 1;
    int token = 0;

    public PollsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Create new thread ASYNC
        new getSurveyList().execute();

        View rootView = inflater.inflate(R.layout.fragment_polls, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = sharedPreferences.getInt("Token", 0);
        pollsList = (ListView) rootView.findViewById(R.id.pollsList);

        return rootView;
    }


    void runLoadPolls() {
        loadPolls();

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
    }

    private class getSurveyList extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            postData();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            runLoadPolls();
        }

        @Override
        protected void onPreExecute() {

        }

        public void postData() {
            try {
                // создаем запрос на сервер
                DefaultHttpClient hc = new DefaultHttpClient();
                ResponseHandler<String> res = new BasicResponseHandler();
                // post запрос
                HttpPost postMethod = new HttpPost("http://flyerbox.herokuapp.com/getSurveyList");
                // передаём параметры в списке
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("token", String.valueOf(token)));

                //собераем их вместе и посылаем на сервер
                postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //получаем ответ от сервера
                response = hc.execute(postMethod, res);

                Log.d("Http Polls Post Response:", response);
            } catch (Exception e) {
                System.out.println("Exp=" + e + " \n Polls Response from server = " + response);
            }
        }
    }

    void loadPolls() {
        try {
            JSONObject resp = new JSONObject(response);
            while(resp.toString().contains("survey_" + pollsCount)) {
                JSONObject concreteSurvey = resp.getJSONObject("survey_" + pollsCount);

                String surveyID = concreteSurvey.getString("id");
                String surveyDescription = concreteSurvey.getString("description");

                // Create new item in List
                polls.add(new Poll(surveyDescription, "", Integer.parseInt(surveyID)));

                System.out.println("ANSWER!!!   -->" + surveyDescription + Integer.parseInt(surveyID));
                System.out.println(pollsCount);
                System.out.println("survey_" + pollsCount);

                ++pollsCount;
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("pollsCount", pollsCount - 1);
            editor.apply();

            String pollsCountString = String.valueOf(sharedPreferences.getInt("pollsCount", 0));
            TextView pollsC = (TextView) getActivity().findViewById(R.id.profileNewPolls);
            pollsC.setText(pollsCountString);

        } catch (JSONException e) {
            System.out.println("Can't create polls object");
        }
    }

}
