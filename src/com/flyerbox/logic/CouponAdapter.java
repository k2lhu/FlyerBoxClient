package com.flyerbox.logic;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.flyerbox.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by tmrafael on 03.12.2014.
 */
public class CouponAdapter extends BaseAdapter {
    ArrayList<Coupon> data = new ArrayList<Coupon>();
    Context context;

    public CouponAdapter(Context context, ArrayList<Coupon> data) {
        this.data = data;
        this.context = context;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Coupon getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.coupons_list_item, parent, false);
        }

        LinearLayout item = (LinearLayout) convertView.findViewById(R.id.coupons_list_item);
        TextView title = (TextView) convertView.findViewById(R.id.couponItemTitle);
        TextView discount = (TextView) convertView.findViewById(R.id.couponItemDiscount);
        LinearLayout status = (LinearLayout) convertView.findViewById(R.id.couponItemStatus);
        TextView expire = (TextView) convertView.findViewById(R.id.couponItemExpire);

        item.setTag(getItem(position).getId());
        title.setText(getItem(position).getTitle());
        discount.setText("" + getItem(position).getDiscount());
        expire.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(getItem(position).getExpire()));
//        if (getItem(position).isUsed()){
//            title.setTextColor(Color.DKGRAY);
//        } else {
//            title.setTextColor(Color.BLACK);
//        }

        status.setEnabled(!getItem(position).isUsed());

        return convertView;
    }
}
