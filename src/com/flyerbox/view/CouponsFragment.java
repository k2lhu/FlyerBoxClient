package com.flyerbox.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.flyerbox.R;

/**
 * Created by tmrafael on 13.11.2014.
 */
public class CouponsFragment extends Fragment {
    public CouponsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_coupons, container, false);

        return rootView;
    }
}
