package com.flyerbox.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.flyerbox.R;
import com.flyerbox.logic.Coupon;
import com.flyerbox.logic.CouponAdapter;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tmrafael on 13.11.2014.
 */
public class CouponsFragment extends Fragment {
    private ListView pollsList;
    private ArrayList<Coupon> coupons = new ArrayList<Coupon>();
    private int selectedCouponID;

    public CouponsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_coupons, container, false);

        loadCoupons(); //Example

        pollsList = (ListView) rootView.findViewById(R.id.couponsList);

        pollsList.setAdapter(new CouponAdapter(getActivity(), coupons));
        pollsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCouponID = ((Coupon)parent.getItemAtPosition(position)).getId();
                Log.d("Polls Fragment", "Selected coupon id: " + selectedCouponID);

                //TODO: Open Dialog with Coupon Info and Buttons "Use Coupon" and "Close"
            }
        });

        return rootView;
    }

    void runLoadCoupons(){
        loadCoupons();

        pollsList = (ListView) getView().findViewById(R.id.couponsList);
        pollsList.setAdapter(new CouponAdapter(getActivity(), coupons));
        pollsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCouponID = ((Coupon)parent.getItemAtPosition(position)).getId();
                Log.d("Polls Fragment", "Selected coupon id: " + selectedCouponID);

                //TODO: Open Dialog with Coupon Info and Buttons "Use Coupon" and "Close"
            }
        });
    }

    private void loadCoupons() {
        coupons.add(new Coupon(156, "Mc'Donalds", false, 18, new Date()));
        coupons.add(new Coupon(12, "Comfy", false, 7, new Date()));
        coupons.add(new Coupon(120, "Eldorado", true, 35, new Date()));
    }
}
