package com.flyerbox.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.flyerbox.R;
import com.flyerbox.logic.CheckData;
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
 * Created by Roman on 9/23/14.
 * 3:00 PM
 */

public class LoginActivity extends Activity {

    private EditText emailField;
    private EditText passField;
    private ProgressDialog progressDialog;
    private String response;
    private String responseInfo;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Set Progress Spinner
        progressDialog = new ProgressDialog(this);
        // Load Shared Preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // Autologin
        if (!sharedPreferences.getString("Email", "").equals(""))
        {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void loginClick(View v) {
        if (checkFields()) {
            new LoginPostReq().execute();
        }
    }

    public void registrationClick(View v) {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // Disable "Back" button
    }

    private boolean checkFields() {
        emailField = (EditText) findViewById(R.id.loginField);
        passField = (EditText) findViewById(R.id.passField);

        if (emailField.getText().toString().length() != 0 && passField.getText().toString().length() != 0 && CheckData.checkEmail(emailField.getText().toString())) {
            return true;
        } else if (!CheckData.checkEmail(emailField.getText().toString())) {
            emailField.setError("Incorrect email.");
            return false;
        } else if (emailField.getText().toString().length() == 0 && passField.getText().toString().length() == 0) {
            emailField.setError("Field required");
            passField.setError("Field required");
            return false;
        } else if (emailField.getText().toString().length() == 0) {
            emailField.setError("Field required");
            return false;
        } else if (passField.getText().toString().length() == 0)
            passField.setError("Field required");
        return false;
    }

    private class LoginPostReq extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            postData();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            doLogin(response);
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
                HttpPost postMethod = new HttpPost("http://flyerbox.herokuapp.com/authorization");
                HttpPost postMethodInfo = new HttpPost("http://flyerbox.herokuapp.com/info");
                // передаём параметры в списке
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("email", String.valueOf(emailField.getText())));
                nameValuePairs.add(new BasicNameValuePair("password", String.valueOf(passField.getText())));

                //собераем их вместе и посылаем на сервер
                postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                postMethodInfo.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //получаем ответ от сервера
                response = hc.execute(postMethod, res);
                responseInfo = hc.execute(postMethodInfo, res);

                Log.d("Http Login Post Response:", response);
            } catch (Exception e) {
                System.out.println("Exp=" + e + " \n Login Response from server = " + response);
            }
        }

        private void doLogin(String response) {
            if (response == null) {
                Toast.makeText(getApplicationContext(), "No Internet connection", Toast.LENGTH_LONG).show();
            } else if (response.contains("success")) {
                Toast.makeText(getApplicationContext(), "You've been successfully signed in!", Toast.LENGTH_LONG).show();
                loginSuccess();
            } else if (response.contains("null") || response.contains("password")) {
                Toast.makeText(getApplicationContext(), "Email and password do not match.", Toast.LENGTH_LONG).show();
            } else if (response.contains("Email")) {
                Toast.makeText(getApplicationContext(), "Email doesn't exist.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Server is busy. Try later.", Toast.LENGTH_LONG).show();
            }
        }

        private void loginSuccess() {
            saveSharedPreferences();
            saveUserData();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        private void saveSharedPreferences() {
            Session session = new Session();
            session.createSessionFrom(response);
            Editor editor = sharedPreferences.edit();

            editor.putInt("Token", session.getSessionID());
            editor.putString("Email", emailField.getText().toString());
            editor.putInt("pollsCount", 0);
            editor.apply();
        }

        private void saveUserData() {
            Session session = new Session();
            session.loadUserDataToApp(responseInfo);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userFirstName", session.getFirstName());
            editor.putString("userLastName", session.getLastName());
            editor.putString("userCountry", session.getCountry());
            editor.putString("userCity", session.getCity());
            editor.putString("Email", session.getEmail());
            editor.apply();
        }
    }

}
