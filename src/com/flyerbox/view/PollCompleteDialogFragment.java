package com.flyerbox.view;

import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;
import com.flyerbox.R;

import java.io.FileDescriptor;
import java.io.PrintWriter;

/**
 * Created by tmrafael on 03.12.2014.
 */
public class PollCompleteDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle("Congratulations!");
        dialogBuilder.setMessage("You have successfully completed the survey.\r\nThank you.");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Return to the polls", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Fragment fragment = new PollsFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
            }
        });

        return dialogBuilder.create();
    }
}
