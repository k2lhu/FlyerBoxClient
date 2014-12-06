package com.flyerbox.view;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.flyerbox.R;
import com.flyerbox.logic.Coupon;

/**
 * Created by tmrafael on 04.12.2014.
 */
public class CouponDialog extends Dialog {
    public CouponDialog(Context context, int theme, Coupon coupon, FragmentManager fragmentManager) {
        super(context, theme);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_coupon);

        setCanceledOnTouchOutside(false);

        View returnButton = findViewById(R.id.coupon_dialog_return_button);
        View useCouponButton = findViewById(R.id.coupon_dialog_usecoupon_button);

        TextView title = (TextView) findViewById(R.id.coupon_dialog_title);
        TextView id = (TextView) findViewById(R.id.coupon_dialog_id);
        TextView discount = (TextView) findViewById(R.id.coupon_dialog_discount);
        TextView daysLeft = (TextView) findViewById(R.id.coupon_dialog_daysleft);
        TextView buttonUseCoupon = (TextView) findViewById(R.id.coupon_dialog_usecoupon_button);

        title.setText(coupon.getTitle());
        id.setText("" + coupon.getId());
        discount.setText("" + coupon.getDiscount() + "%");
        daysLeft.setText(coupon.getCoolTime());

        if(coupon.isUsed()){
            buttonUseCoupon.setEnabled(false);
            buttonUseCoupon.setClickable(false);
            buttonUseCoupon.setTextColor(Color.argb(255, 109, 109, 109));
            title.setEnabled(false);
        } else {
            buttonUseCoupon.setEnabled(true);
            buttonUseCoupon.setClickable(true);
            buttonUseCoupon.setTextColor(Color.argb(255, 0, 0, 0));
            title.setEnabled(true);
        }


        final FragmentManager fManager = fragmentManager;

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CouponsFragment();
                fManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

                dismiss();
            }
        });

        useCouponButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useCoupon();

                Fragment fragment = new CouponsFragment();
                fManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

                dismiss();
            }
        });
    }

    private void useCoupon() {
        //TODO: Do something to set that coupon used and give this information to server
    }
}
