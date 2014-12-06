package com.flyerbox.view;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.flyerbox.R;
import com.flyerbox.logic.Coupon;
import com.flyerbox.logic.CouponAdapter;
import com.flyerbox.logic.Poll;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by tmrafael on 13.11.2014.
 */
public class CouponsFragment extends Fragment {
    private ListView couponsList;
    private ArrayList<Coupon> coupons = new ArrayList<Coupon>();
    private Coupon selectedCoupon;
    private SharedPreferences sharedPreferences;
    private String response;
    private int couponsCount = 1;
    private ProgressDialog progressDialog;
    int token = 0;

    public CouponsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_coupons, container, false);
        // Load copons from the server
        // new getCouponsList().execute(); //TODO Load coupons from the server

        // Create Progress Spinner
        progressDialog = new ProgressDialog(getActivity());
        // Get Shared Preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = sharedPreferences.getInt("Token", 0);

        // Load coupons ot View
        couponsList = (ListView) rootView.findViewById(R.id.couponsList);

        runLoadCoupons();

        return rootView;
    }

    void runLoadCoupons(){
        loadCoupons();

        couponsList.setAdapter(new CouponAdapter(getActivity(), coupons));
        couponsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCoupon = (Coupon) parent.getItemAtPosition(position);
                Log.d("Polls Fragment", "Selected coupon id: " + selectedCoupon.getId());

                Dialog couponDialog = new CouponDialog(getActivity(), 0, selectedCoupon, getActivity().getFragmentManager());
                couponDialog.show();
            }
        });
    }

    private class getCouponsList extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            postData();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            loadCoupons();
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

    void loadCoupons1() {
        try {
            JSONObject resp = new JSONObject(response);
            while (resp.toString().contains("survey_" + couponsCount)) {
                JSONObject concreteSurvey = resp.getJSONObject("survey_" + couponsCount);

                String surveyID = concreteSurvey.getString("id");
                String surveyDescription = concreteSurvey.getString("description");

                // Create new item in List
                coupons.add(new Coupon(156, "Mc'Donalds", false, 18, new Date(2014,12,20,0,0,0)));

                ++couponsCount;
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("couponsCount", couponsCount - 1);
            editor.apply();

            String pollsCountString = String.valueOf(sharedPreferences.getInt("pollsCount", 0));
            TextView pollsC = (TextView) getActivity().findViewById(R.id.profileNewPolls);
            pollsC.setText(pollsCountString);

        } catch (JSONException e) {
            System.out.println("Can't create polls object");
        }
    }

    private void loadCoupons() {
        coupons.add(new Coupon(156, "Mc'Donalds", false, 18, new Date(2014,12,20,0,0,0)));
        coupons.add(new Coupon(12, "Comfy", false, 7, new Date(2014,12,10,1,2,2)));
        coupons.add(new Coupon(120, "Eldorado", true, 35, new Date()));
    }
}
