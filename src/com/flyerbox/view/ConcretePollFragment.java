package com.flyerbox.view;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.flyerbox.R;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by tmrafael on 27.11.2014.
 */
public class ConcretePollFragment extends Fragment {
    private int selectedQuestionID;
    private int selectedAnswerID;
    private int currentQuestion;
    private String response;
    private SharedPreferences sharedPreferences;
    private int count = 1;
    private int[] answersArray = new int[4];

    JSONObject fullCompletedAnswers;
    JSONObject completedAnswers = new JSONObject();

    int pollID;
    int token = 0;


    public ConcretePollFragment() {
    }

    public void setPollID(int pollID) {
        this.pollID = pollID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        new GetAnswersPostReq().execute();

        View rootView = inflater.inflate(R.layout.fragment_concrete_poll, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = sharedPreferences.getInt("Token", 0);
        rootView.findViewById(R.id.answerOne).setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedAnswerID = 0;
                currentQuestion = count - 1;
                putAnswerToJSON("question_" + currentQuestion, answersArray[selectedAnswerID]);
                getNext();
            }
        });
        rootView.findViewById(R.id.answerTwo).setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedAnswerID = 1;
                currentQuestion = count - 1;
                putAnswerToJSON("question_" + currentQuestion, answersArray[selectedAnswerID]);
                getNext();
            }
        });
        rootView.findViewById(R.id.answerThree).setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedAnswerID = 2;
                currentQuestion = count - 1;
                putAnswerToJSON("question_" + currentQuestion, answersArray[selectedAnswerID]);
                getNext();
            }
        });
        rootView.findViewById(R.id.answerFour).setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedAnswerID = 3;
                currentQuestion = count - 1;
                putAnswerToJSON("question_" + currentQuestion, answersArray[selectedAnswerID]);
                getNext();
            }
        });

        return rootView;
    }

    void getNext() {
        try {
            System.out.println("RESPONSE!!!!!!  " + response);
            JSONObject obj = new JSONObject(response);
            String surveyDescription = obj.getString("survey_description");
            TextView pollName = (TextView) getActivity().findViewById(R.id.pollTitle);
            pollName.setText(surveyDescription);

            JSONObject questions = obj.getJSONObject("questions");
            Iterator question = questions.keys();

            // Get question #(count)
            if (questions.toString().contains("question_" + count)) {
                String currentQuestionKey = "question_" + count;
                JSONObject currentQuestionValue = questions.getJSONObject(currentQuestionKey);

                // Add quiestion ID to array
                selectedQuestionID = currentQuestionValue.getInt("question_id");

                // Set text to field in View
                String questionText = currentQuestionValue.getString("question_text");
                TextView questionTextField = (TextView) getActivity().findViewById(R.id.questionText);
                questionTextField.setText(questionText);

                // Set question ID in View
                TextView questionIDView = (TextView) getActivity().findViewById(R.id.questionID);
                questionIDView.setText("" + count);


                JSONObject answers = currentQuestionValue.getJSONObject("answers");
                Iterator answerKeys = answers.keys();

                // Fill answer field
                while (answerKeys.hasNext()) {

                    String currentAnswerKey = (String) answerKeys.next();
                    JSONObject currentAnswerValue = answers.getJSONObject(currentAnswerKey);

                    String answerText = currentAnswerValue.getString("text");

                    if (currentAnswerKey.contains("answer_1")) {
                        TextView answer = (TextView) getActivity().findViewById(R.id.answerOne);
                        answer.setText(answerText);
                        answersArray[0] = currentAnswerValue.getInt("id");
                    } else if (currentAnswerKey.contains("answer_2")) {
                        TextView answer = (TextView) getActivity().findViewById(R.id.answerTwo);
                        answer.setText(answerText);
                        answersArray[1] = currentAnswerValue.getInt("id");
                    } else if (currentAnswerKey.contains("answer_3")) {
                        TextView answer = (TextView) getActivity().findViewById(R.id.answerThree);
                        answer.setText(answerText);
                        answersArray[2] = currentAnswerValue.getInt("id");
                    } else if (currentAnswerKey.contains("answer_4")) {
                        TextView answer = (TextView) getActivity().findViewById(R.id.answerFour);
                        answer.setText(answerText);
                        answersArray[3] = currentAnswerValue.getInt("id");
                    }

                }
                count++;
                System.out.println("Count: " + count);
            } else {
                DialogFragment newFragment = new PollCompleteDialogFragment();
                newFragment.show(getFragmentManager(), "complete");

                new PostAnswersPostReq().execute();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public int getCount() {
        return count;
    }

    private void putAnswerToJSON(String question, int answer) {
        // TODO впихнуть в JSON и отправить нахуй на сервер
        JSONObject concreteQuestionDetail = new JSONObject();
        try {
            concreteQuestionDetail.put("question_id", "" + selectedQuestionID);
            concreteQuestionDetail.put("answer_id", "" + answer);
            System.out.println(concreteQuestionDetail);

            completedAnswers.put(question, concreteQuestionDetail);
            System.out.println(completedAnswers);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class GetAnswersPostReq extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            postData();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            getNext();
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
                HttpPost postMethod = new HttpPost("http://flyerbox.herokuapp.com/getSurvey");
                // передаём параметры в списке
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("survey", String.valueOf(pollID)));

                nameValuePairs.add(new BasicNameValuePair("token", String.valueOf(token)));

                System.out.println("Poll Token2: " + String.valueOf(token));

                //собераем их вместе и посылаем на сервер
                postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //получаем ответ от сервера
                response = hc.execute(postMethod, res);

                Log.d("Http GetSurvey Post Response:", response);
            } catch (Exception e) {
                System.out.println("Exp=" + e + " \n GetSurvey Response from server = " + response);
            }
        }
    }

    private class PostAnswersPostReq extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            postData();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

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
                HttpPost postMethod = new HttpPost("http://flyerbox.herokuapp.com/saveSurvey");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("survey_id", String.valueOf(pollID)));

                nameValuePairs.add(new BasicNameValuePair("token", String.valueOf(token)));

                nameValuePairs.add(new BasicNameValuePair("answers", fullCompletedAnswers.toString()));

                //собераем их вместе и посылаем на сервер
                postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //получаем ответ от сервера
                response = hc.execute(postMethod, res);

                Log.d("Http GetSurvey Post Response:", response);
            } catch (Exception e) {
                System.out.println("Exp=" + e + " \n GetSurvey Response from server = " + response);
            }
        }
    }

}

