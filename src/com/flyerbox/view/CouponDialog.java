package com.flyerbox.view;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.flyerbox.R;
import com.flyerbox.logic.Coupon;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tmrafael on 04.12.2014.
 */
public class CouponDialog extends Dialog {
    final Coupon coupon;
    private SharedPreferences sharedPreferences;
    int token = 0;
    private String response;



    public CouponDialog(Context context, int theme, final Coupon coupon, FragmentManager fragmentManager) {
        super(context, theme);
        this.coupon = coupon;

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
                new useCouponPostReq().execute();
                Fragment fragment = new CouponsFragment();
                fManager.beginTransaction().replace(R.id.frame_container, fragment).commit();


            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // Get Shared Preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        token = sharedPreferences.getInt("Token", 0);

        // barcode data
        String barcode_data = ""+coupon.getId();

        // barcode image
        Bitmap bitmap = null;
        ImageView barcodeView = (ImageView) findViewById(R.id.coupon_dialog_barcode);

        try {
            bitmap = encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, barcodeView.getWidth(), barcodeView.getHeight());
            barcodeView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void useCoupon() {
        //TODO: Do something to set that coupon used and give this information to server
    }

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    private class useCouponPostReq extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            postData();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (response == null) {
                dismiss();
                Toast.makeText(getContext(), "No Internet connection", Toast.LENGTH_LONG).show();
            } else {
                dismiss();
            }

        }

        @Override
        protected void onPreExecute() {

        }

        public void postData() {
            try {
                // создаем запрос на сервер
                DefaultHttpClient hc = new DefaultHttpClient();
                ResponseHandler<String> res = new BasicResponseHandler();
                // post запрос
                HttpPost postMethod = new HttpPost("http://flyerbox.herokuapp.com/updateCoupon");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("token", String.valueOf(token)));
                nameValuePairs.add(new BasicNameValuePair("couponId", Integer.toString(coupon.getId())));

                //собераем их вместе и посылаем на сервер
                postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //получаем ответ от сервера
                response = hc.execute(postMethod, res);
                Log.d("Http send used coupon Response:", response);
            } catch (Exception e) {
                System.out.println("Exp=" + e + " \n used coupon Response from server = " + response);
            }
        }
    }

}
