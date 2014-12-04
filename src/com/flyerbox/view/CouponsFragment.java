package com.flyerbox.view;

import android.app.Dialog;
import android.app.Fragment;
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
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by tmrafael on 13.11.2014.
 */
public class CouponsFragment extends Fragment {
    private ListView couponsList;
    private ArrayList<Coupon> coupons = new ArrayList<Coupon>();
    private Coupon selectedCoupon;

    public CouponsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_coupons, container, false);

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

    private void loadCoupons() {
        coupons.add(new Coupon(156, "Mc'Donalds", false, 18, new Date(2014,12,20,0,0,0)));
        coupons.add(new Coupon(12, "Comfy", false, 7, new Date(2014,12,10,1,2,2)));
        coupons.add(new Coupon(120, "Eldorado", true, 35, new Date()));
    }
}
