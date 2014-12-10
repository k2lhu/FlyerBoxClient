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
import android.widget.Toast;
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
    private int couponsCountNotUsed = 0;
    private ProgressDialog progressDialog;
    int token = 0;

    public CouponsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Create Progress Spinner
        progressDialog = new ProgressDialog(getActivity());

        // Get Shared Preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = sharedPreferences.getInt("Token", 0);

        // Load coupons from the server
        new getCouponsList().execute();

        View rootView = inflater.inflate(R.layout.fragment_coupons, container, false);

        // Load coupons ot View
        couponsList = (ListView) rootView.findViewById(R.id.couponsList);

        return rootView;
    }

    void runLoadCoupons() {
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

            if (response == null) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "No Internet connection", Toast.LENGTH_LONG).show();
            } else {
                runLoadCoupons();
                progressDialog.dismiss();
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
                HttpPost postMethod = new HttpPost("http://flyerbox.herokuapp.com/getCouponList");
                // передаём параметры в списке
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("token", String.valueOf(token)));

                //собераем их вместе и посылаем на сервер
                postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //получаем ответ от сервера
                response = hc.execute(postMethod, res);

                Log.d("Http get coupons list Post Response:", response);
            } catch (Exception e) {
                System.out.println("Exp=" + e + " \n get coupons list Response from server = " + response);
            }
        }
    }

    private void loadCoupons() {
        try {
            JSONObject resp = new JSONObject(response);
            JSONObject couponObj = resp.getJSONObject("coupons");
            while (couponObj.toString().contains("coupon_" + couponsCount)) {
                JSONObject concreteSurvey = couponObj.getJSONObject("coupon_" + couponsCount);

                // Get data from response
                String couponID = concreteSurvey.getString("id");
                String couponDescription = concreteSurvey.getString("description");
                boolean isUsed = Boolean.parseBoolean(concreteSurvey.getString("used"));
                String discount = concreteSurvey.getString("discount");
                String dateOfExpiry = concreteSurvey.getString("expiryDate");

                // Create new item in List
                coupons.add(new Coupon(Integer.parseInt(couponID), couponDescription, isUsed, Integer.parseInt(discount), dateOfExpiry));

                ++couponsCount;
                couponsCountNotUsed += (isUsed) ? 0 : 1;
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("couponsCount", couponsCountNotUsed);
            editor.apply();

            String couponsCountString = String.valueOf(sharedPreferences.getInt("couponsCount", 0));
            TextView couponsC = (TextView) getActivity().findViewById(R.id.profileNewCoupons);
            couponsC.setText(couponsCountString);

        } catch (JSONException e) {
            System.out.println("Can't create coupons object = " + e);
        }
    }

}
