package com.flyerbox.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.flyerbox.R;
import android.preference.PreferenceManager;

/**
 * FlyerBox
 * Created by roman on 9/23/14.
 * 4:50 PM
 */
public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(2000);
                } catch (Exception e) {
                    Log.d("Error", "Splash screen");
                } finally {
                    doLogin();
                }
            }
        };
        welcomeThread.start();
    }

    private void doLogin() {
        if (isSignedIn()) {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    private boolean isSignedIn() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String emailKey = sharedPreferences.getString("Email", "");
        int tokenKey = sharedPreferences.getInt("Token", 0);

        return tokenKey != 0 && emailKey != null;
    }

}
