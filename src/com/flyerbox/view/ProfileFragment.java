package com.flyerbox.view;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.flyerbox.R;
import com.flyerbox.logic.Session;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tmrafael on 29.10.2014.
 */

public class ProfileFragment extends Fragment {
    private String response;
    private SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //Loading data in new thread
        new LoginPostReq().execute();


        return rootView;
    }

    private void setData() {
        String emailKey = sharedPreferences.getString("Email", "");
        String firstNameKey = sharedPreferences.getString("userFirstName", "");
        String lastNameKey = sharedPreferences.getString("userLastName", "");
        String countryKey = sharedPreferences.getString("userCountry", "");
        String cityKey = sharedPreferences.getString("userCity", "");

        TextView emailText = (TextView) getActivity().findViewById(R.id.profileEmail);
        emailText.setText(emailKey);

        TextView firstNameText = (TextView) getActivity().findViewById(R.id.profileFirstName);
        firstNameText.setText(firstNameKey);

        TextView lastNameText = (TextView) getActivity().findViewById(R.id.profileLastName);
        lastNameText.setText(lastNameKey);

        TextView countryText = (TextView) getActivity().findViewById(R.id.profileCountry);
        countryText.setText(countryKey);

        TextView cityText = (TextView) getActivity().findViewById(R.id.profileCity);
        cityText.setText(cityKey);
    }

    private class LoginPostReq extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            setData();
        }

        @Override
        protected void onPreExecute() {

        }
//
//        public void postData() {
//            try {
//                // создаем запрос на сервер
//                DefaultHttpClient hc = new DefaultHttpClient();
//                ResponseHandler<String> res = new BasicResponseHandler();
//                // post запрос
//                HttpPost postMethod = new HttpPost("http://flyerbox.herokuapp.com/info");
//                // передаём параметры в списке
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//
//
//                String emailKey = sharedPreferences.getString("Email", "");
//                int tokenKey = sharedPreferences.getInt("Token", 0);
//
//                nameValuePairs.add(new BasicNameValuePair("email", emailKey));
//                nameValuePairs.add(new BasicNameValuePair("token", Integer.toString(tokenKey)));
//
//                //собераем их вместе и посылаем на сервер
//                postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//                //получаем ответ от сервера
//                response = hc.execute(postMethod, res);
//
//                Log.d("Http Profile Post Response:", response);
//            } catch (Exception e) {
//                System.out.println("Exp=" + e + " \n Response from server = " + response);
//            }
//        }
//
//        private void loadData(String response) {
//            Session session = new Session();
//            session.loadUserDataToApp(response);
//
//            Editor editor = sharedPreferences.edit();
//
//            editor.putString("userFirstName", session.getFirstName());
//            editor.putString("userLastName", session.getLastName());
//            editor.putString("userCountry", session.getCountry());
//            editor.putString("userCity", session.getCity());
//            editor.apply();
//
//
//        }
    }


}
