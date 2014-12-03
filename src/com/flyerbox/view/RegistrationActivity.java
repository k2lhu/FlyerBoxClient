package com.flyerbox.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
 * FlyerBoxClient
 * Created by Roman on 9/24/14.
 * 11:18 AM
 */

public class RegistrationActivity extends Activity {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText country;
    private EditText city;
    private EditText pass;
    private ProgressDialog progressDialog;
    private String response;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        progressDialog = new ProgressDialog(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    public void createUserButtonClick(View v) {
        if (fieldValidation()) {
            new RegPostReq().execute();
        }
    }

    private boolean fieldValidation() {
        firstName = (EditText) findViewById(R.id.firstNameFieldReg);
        lastName = (EditText) findViewById(R.id.lastNameFieldReg);
        email = (EditText) findViewById(R.id.emailFieldReg);
        country = (EditText) findViewById(R.id.countryFieldReg);
        city = (EditText) findViewById(R.id.cityFieldReg);
        pass = (EditText) findViewById(R.id.passFieldReg);
        EditText passMatch = (EditText) findViewById(R.id.passMatchFieldReg);

        firstName.setError(null);
        lastName.setError(null);
        email.setError(null);
        country.setError(null);
        city.setError(null);
        pass.setError(null);
        passMatch.setError(null);

        int checkFields = 7;

        if (!CheckData.checkName(firstName.getText().toString())) {
            firstName.setError("First name is incorrect");
            checkFields--;
        }
        if (!CheckData.checkName(lastName.getText().toString())) {
            lastName.setError("Last name is incorrect");
            checkFields--;
        }
        if (!CheckData.checkEmail(email.getText().toString())) {
            email.setError("Email is incorrect");
            checkFields--;
        }
        if (!CheckData.checkCountryOrCity(country.getText().toString())) {
            country.setError("Country is incorrect");
            checkFields--;
        }
        if (!CheckData.checkCountryOrCity(city.getText().toString())) {
            city.setError("City is incorrect");
            checkFields--;
        }
        if (!CheckData.checkPass(pass.getText().toString())) {
            pass.setError("Password is incorrect. It must be greater than 8 characters");
            checkFields--;
        }
        if (!isPassesMatch(pass.getText().toString(), passMatch.getText().toString())) {
            passMatch.setError("Password does not match");
            checkFields--;
        }
        if (checkFields < 7)
            return false;
        return true;
    }

    private boolean isPassesMatch(String firstPass, String secondPass) {
        return firstPass.equals(secondPass);
    }

    private class RegPostReq extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            postData();
//            loadUserData();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("Http Post Response:", response);
            progressDialog.dismiss();
            if (response == null) {
                Toast.makeText(getApplicationContext(), "No Internet connection", Toast.LENGTH_LONG).show();
            } else if (response.contains("success")) {
                Toast.makeText(getApplicationContext(), "You've been registered!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            } else if (response.contains("email")) {
                Toast.makeText(getApplicationContext(), "Your email already registered.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Server is busy. Try later.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        public void postData() {
            try {
                DefaultHttpClient hc = new DefaultHttpClient();
                ResponseHandler<String> res = new BasicResponseHandler();

                HttpPost postMethod = new HttpPost("http://flyerbox.herokuapp.com/registration");

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
                nameValuePairs.add(new BasicNameValuePair("firstName", String.valueOf(firstName.getText())));
                nameValuePairs.add(new BasicNameValuePair("lastName", String.valueOf(lastName.getText())));
                nameValuePairs.add(new BasicNameValuePair("email", String.valueOf(email.getText())));
                nameValuePairs.add(new BasicNameValuePair("password", String.valueOf(pass.getText())));
                nameValuePairs.add(new BasicNameValuePair("country", String.valueOf(country.getText())));
                nameValuePairs.add(new BasicNameValuePair("city", String.valueOf(city.getText())));

                postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                response = hc.execute(postMethod, res);
                Log.d("Http Post Response:", response);
            } catch (Exception e) {
                System.out.println("Exp=" + e + " \n Response from server = " + response);
            }
        }
    }

}