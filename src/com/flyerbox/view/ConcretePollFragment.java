package com.flyerbox.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;
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
    private ProgressDialog progressDialog;
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
        // Set Progress Spinner
        progressDialog = new ProgressDialog(getActivity());
        // Load Shared Preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = sharedPreferences.getInt("Token", 0);
        // Load data from the server
        new GetAnswersPostReq().execute();
        // Load view
        View rootView = inflater.inflate(R.layout.fragment_concrete_poll, container, false);


        // Response to answer clicks
        rootView.findViewById(R.id.answerItem1).setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(final View v) {
                activateAnswer(v, getActivity().findViewById(R.id.answerCircle1), (TextView) getActivity().findViewById(R.id.answerOne));

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        deActivateAnswer(v, getActivity().findViewById(R.id.answerCircle1), (TextView) getActivity().findViewById(R.id.answerOne));

                        selectedAnswerID = 0;
                        currentQuestion = count - 1;
                        putAnswerToJSON("question_" + currentQuestion, answersArray[selectedAnswerID]);

                        getNext();
                    }
                }, 500);
            }
        });
        rootView.findViewById(R.id.answerItem2).setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(final View v) {
                activateAnswer(v, getActivity().findViewById(R.id.answerCircle2), (TextView) getActivity().findViewById(R.id.answerTwo));

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        deActivateAnswer(v, getActivity().findViewById(R.id.answerCircle2), (TextView) getActivity().findViewById(R.id.answerTwo));

                        selectedAnswerID = 1;
                        currentQuestion = count - 1;
                        putAnswerToJSON("question_" + currentQuestion, answersArray[selectedAnswerID]);

                        getNext();
                    }
                }, 500);
            }
        });
        rootView.findViewById(R.id.answerItem3).setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(final View v) {
                activateAnswer(v, getActivity().findViewById(R.id.answerCircle3), (TextView) getActivity().findViewById(R.id.answerThree));

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        deActivateAnswer(v, getActivity().findViewById(R.id.answerCircle3), (TextView) getActivity().findViewById(R.id.answerThree));

                        selectedAnswerID = 2;
                        currentQuestion = count - 1;
                        putAnswerToJSON("question_" + currentQuestion, answersArray[selectedAnswerID]);

                        getNext();
                    }
                }, 500);
            }
        });
        rootView.findViewById(R.id.answerItem4).setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(final View v) {
                activateAnswer(v, getActivity().findViewById(R.id.answerCircle4), (TextView) getActivity().findViewById(R.id.answerFour));

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        deActivateAnswer(v, getActivity().findViewById(R.id.answerCircle4), (TextView) getActivity().findViewById(R.id.answerFour));

                        selectedAnswerID = 3;
                        currentQuestion = count - 1;
                        putAnswerToJSON("question_" + currentQuestion, answersArray[selectedAnswerID]);

                        getNext();
                    }
                }, 500);
            }
        });

        return rootView;
    }

    private void activateAnswer(final View itemView, final View circleView, final TextView mainText) {
        getActivity().findViewById(R.id.answerCircle1).setSelected(true);
        getActivity().findViewById(R.id.answerCircle2).setSelected(true);
        getActivity().findViewById(R.id.answerCircle3).setSelected(true);
        getActivity().findViewById(R.id.answerCircle4).setSelected(true);
        circleView.setSelected(false);
        circleView.setActivated(true);

        itemView.setActivated(true);

        ((TextView) getActivity().findViewById(R.id.answerOne)).setTextColor(Color.LTGRAY);
        ((TextView) getActivity().findViewById(R.id.answerTwo)).setTextColor(Color.LTGRAY);
        ((TextView) getActivity().findViewById(R.id.answerThree)).setTextColor(Color.LTGRAY);
        ((TextView) getActivity().findViewById(R.id.answerFour)).setTextColor(Color.LTGRAY);
        mainText.setTextColor(Color.BLACK);
    }

    private void deActivateAnswer(final View itemView, final View circleView, final TextView mainText) {
        getActivity().findViewById(R.id.answerCircle1).setSelected(false);
        getActivity().findViewById(R.id.answerCircle2).setSelected(false);
        getActivity().findViewById(R.id.answerCircle3).setSelected(false);
        getActivity().findViewById(R.id.answerCircle4).setSelected(false);
        circleView.setActivated(false);

        itemView.setActivated(false);

        ((TextView) getActivity().findViewById(R.id.answerOne)).setTextColor(Color.BLACK);
        ((TextView) getActivity().findViewById(R.id.answerTwo)).setTextColor(Color.BLACK);
        ((TextView) getActivity().findViewById(R.id.answerThree)).setTextColor(Color.BLACK);
        ((TextView) getActivity().findViewById(R.id.answerFour)).setTextColor(Color.BLACK);
    }

    void getNext() {
        try {
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
            } else {
                System.out.println("Пытаемся отправить на сервак нахуй");
                new PostAnswersPostReq().execute();
                Dialog completeDialog = new PollDialog(getActivity(), 0, getActivity().getFragmentManager());
                completeDialog.show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public int getCount() {
        return count;
    }

    private void putAnswerToJSON(String question, int answer) {
        JSONObject concreteQuestionDetail = new JSONObject();
        try {
            concreteQuestionDetail.put("question_id", "" + selectedQuestionID);
            concreteQuestionDetail.put("answer_id", "" + answer);

            completedAnswers.put(question, concreteQuestionDetail);
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
            progressDialog.dismiss();
            getNext();
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
                HttpPost postMethod = new HttpPost("http://flyerbox.herokuapp.com/getSurvey");
                // передаём параметры в списке
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("survey", String.valueOf(pollID)));

                nameValuePairs.add(new BasicNameValuePair("token", String.valueOf(token)));

                System.out.println("Concrete poll Token2: " + String.valueOf(token));

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
                nameValuePairs.add(new BasicNameValuePair("answers", completedAnswers.toString()));

                //собераем их вместе и посылаем на сервер
                postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //получаем ответ от сервера
                response = hc.execute(postMethod, res);
                Log.d("Http send Post Response:", response);
            } catch (Exception e) {
                System.out.println("Exp=" + e + " \n send Poll Response from server = " + response);
            }
        }
    }

}

