package com.flyerbox.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.flyerbox.R;

/**
 * Created by Roman on 9/23/14.
 * 2:59 PM
 */

public class MainActivity extends Activity {
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        LinearLayout listItem = (LinearLayout) findViewById(R.id.polls);
        listItem.setEnabled(false);

        findViewById(R.id.polls).setOnClickListener(new MenuItemClickListener(R.id.polls));
        findViewById(R.id.coupons).setOnClickListener(new MenuItemClickListener(R.id.coupons));
        findViewById(R.id.profile).setOnClickListener(new MenuItemClickListener(R.id.profile));

        findViewById(R.id.logout).setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        displayView(R.id.polls);


        // Set email in SideDrawer
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String emailKey = sharedPreferences.getString("userFirstName", "") + " " + sharedPreferences.getString("userLastName", "");

        TextView emailText = (TextView) findViewById(R.id.profileEmail);
        emailText.setText(emailKey);

        // Set survey count
        String pollsCountString = String.valueOf(sharedPreferences.getInt("pollsCount", 0));
        TextView pollsC = (TextView) findViewById(R.id.profileNewPolls);
        pollsC.setText(pollsCountString);
        // Set coupons count
        String couponsCountString = String.valueOf(sharedPreferences.getInt("couponsCount", 0));
        TextView couponsC = (TextView) findViewById(R.id.profileNewCoupons);
        couponsC.setText(couponsCountString);

    }

    private class MenuItemClickListener implements LinearLayout.OnClickListener{
        private int item;

        MenuItemClickListener(int itemId) {
            this.item = itemId;
        }

        @Override
        public void onClick(View view) {
            // display view for selected nav drawer item
            displayView(item);
            LinearLayout listItem = (LinearLayout) findViewById(item);
            LinearLayout other = (LinearLayout) findViewById(R.id.polls);
            other.setEnabled(true);
            other = (LinearLayout) findViewById(R.id.coupons);
            other.setEnabled(true);
            other = (LinearLayout) findViewById(R.id.profile);
            other.setEnabled(true);

            listItem.setEnabled(false);
        }
    }

    private void displayView(int itemId) {
        Fragment fragment = null;
        if(itemId == R.id.polls)
        {
            fragment = new PollsFragment();
        } else if(itemId == R.id.coupons)
        {
            fragment = new CouponsFragment();
        } else if(itemId == R.id.profile)
        {
            fragment = new ProfileFragment();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            mDrawerLayout.closeDrawers();
        } else {
            // error in creating fragment
        }
    }

    private  void  logOut(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Editor editor = sharedPreferences.edit();

        editor.putString("Email", "");
        editor.putInt("Token", 0);
        editor.putString("userFirstName", "");
        editor.putString("userLastName", "");
        editor.putString("userCountry", "");
        editor.putString("userCity", "");
        editor.apply();

        Intent logOutIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(logOutIntent);
    }

    @Override
    public void onBackPressed() {
        // Disable "Back" button
    }

}
