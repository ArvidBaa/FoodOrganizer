package com.fannyarvid.foodorganizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.text.format.Time;

/**
 * Created by FannyArvid on 2015-05-11.
 */
public class AddBoxDialogFragment extends DialogFragment {

    private static final String LOG_TAG = AddBoxDialogFragment.class.getSimpleName();

    public interface AddBoxDialogListener {
        public void onAddBoxDialogPositiveClick(
                String boxName, int date, int storageType, int hasBeenInFreezer, int[] ingredients);
    }

    AddBoxDialogListener mListener;
    private EditText mBoxName;

    // Dummy data for ContentValues
    private int mStorageType = 0;
    private int mHasBeenInFreezer = 0;
    private int [] mIngredients = {0};


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (AddBoxDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
            + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_box, null);
        mBoxName = (EditText) dialogView.findViewById(R.id.edittext_add_box_name);

        builder.setView(dialogView)
                .setTitle(R.string.add_box)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String boxName = mBoxName.getText().toString();

                        Time dateTime = new Time();
                        dateTime.setToNow();
                        int julianDay = Time.getJulianDay(System.currentTimeMillis(), dateTime.gmtoff);
                        // TODO: change from dummy data to real data here
                        int storageType = mStorageType; // 0 = fridge, 1 = freezer
                        int hasBeenInFreezer = mHasBeenInFreezer; // 0 = false, 1 = true. true IF storageType has been 1 at any time.

                        int[] ingredients = mIngredients;

                        mListener.onAddBoxDialogPositiveClick(
                                boxName,
                                julianDay,
                                storageType,
                                hasBeenInFreezer,
                                ingredients);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddBoxDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
