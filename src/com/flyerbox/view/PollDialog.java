package com.flyerbox.view;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import com.flyerbox.R;

/**
 * Created by tmrafael on 04.12.2014.
 */
public class PollDialog extends Dialog {
    public PollDialog(Context context, int theme, FragmentManager fragmentManager) {
        super(context, theme);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_complete);

        setCanceledOnTouchOutside(false);

        View returnButton = findViewById(R.id.poll_dialog_return_button);

        final FragmentManager fManager = fragmentManager;

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PollsFragment();
                fManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

                dismiss();
            }
        });
    }
}
