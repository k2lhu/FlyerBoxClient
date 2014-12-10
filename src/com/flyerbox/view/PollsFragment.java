package com.flyerbox.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
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
    private ListView pollsList;
    private ArrayList<Poll> polls = new ArrayList<Poll>();
    private int selectedPollID;
    private SharedPreferences sharedPreferences;
    private String response;
    private int pollsCount = 1;
    private int pollsCountNotAnswered = 0;
    private ProgressDialog progressDialog;
    int token = 0;

    public PollsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Create Progress Spinner
        progressDialog = new ProgressDialog(getActivity());
        // Get Shared Preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = sharedPreferences.getInt("Token", 0);
        // Create new thread ASYNC
        new getSurveyList().execute();
        // Set View
        View rootView = inflater.inflate(R.layout.fragment_polls, container, false);

        //Load polls to View
        pollsList = (ListView) rootView.findViewById(R.id.pollsList);

        return rootView;
    }

    // Check if we have an Internet connection
    public boolean isOnline() {
        ConnectivityManager connectivity = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    // Load polls to View
    void runLoadPolls() {
        loadPolls();

        pollsList.setAdapter(new PollAdapter(getActivity(), polls));
        pollsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPollID = ((Poll) parent.getItemAtPosition(position)).getId();
                Log.d("Polls Fragment", "Selected poll id: " + selectedPollID);
                LinearLayout pollsBackground = (LinearLayout) getActivity().findViewById(R.id.pollsNoInternet);
                if (isOnline()) {
                    ConcretePollFragment questionFragment = new ConcretePollFragment();
                    questionFragment.setPollID(selectedPollID);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, questionFragment).commit();
                } else {
                    pollsList.setAdapter(new PollAdapter(getActivity(), new ArrayList<Poll>()));
                    Toast.makeText(getActivity(), "No Internet connection", Toast.LENGTH_LONG).show();
                    pollsBackground.setEnabled(false);
                }
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
            progressDialog.dismiss();
            LinearLayout pollsBackground = (LinearLayout) getActivity().findViewById(R.id.pollsNoInternet);
            if (response == null) {
                Toast.makeText(getActivity(), "No Internet connection", Toast.LENGTH_LONG).show();
                pollsBackground.setEnabled(false);
            } else {
                runLoadPolls();
                pollsBackground.setEnabled(true);
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
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

    // Parse response
    void loadPolls() {
        try {
            JSONObject resp = new JSONObject(response);
            while (resp.toString().contains("survey_" + pollsCount)) {
                JSONObject concreteSurvey = resp.getJSONObject("survey_" + pollsCount);

                // Get data from response
                String surveyID = concreteSurvey.getString("id");
                String surveyDescription = concreteSurvey.getString("description");
                boolean isAnswered = Boolean.parseBoolean(concreteSurvey.getString("answered"));

                // Create new item in List
                polls.add(new Poll(surveyDescription, "", Integer.parseInt(surveyID), isAnswered));

                ++pollsCount;
                pollsCountNotAnswered += (isAnswered) ? 0 : 1;

            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("pollsCount", pollsCountNotAnswered);
            editor.apply();

            String pollsCountString = String.valueOf(sharedPreferences.getInt("pollsCount", 0));
            TextView pollsC = (TextView) getActivity().findViewById(R.id.profileNewPolls);
            pollsC.setText(pollsCountString);

        } catch (JSONException e) {
            System.out.println("Can't create polls object");
        }
    }

}
