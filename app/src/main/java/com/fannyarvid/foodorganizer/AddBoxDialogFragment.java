package com.fannyarvid.foodorganizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.fannyarvid.foodorganizer.data.FoodContract;

/**
 * Created by FannyArvid on 2015-05-11.
 */
public class AddBoxDialogFragment extends DialogFragment {

    private static final String LOG_TAG = AddBoxDialogFragment.class.getSimpleName();

    public interface AddBoxDialogListener {
        public void onAddBoxDialogPositiveClick(
                String boxName, int date, int storageType, int hasBeenInFreezer, long[] ingredientsId);
    }

    AddBoxDialogListener mListener;
    private IngredientFragment.IngredientAdapter mIngredientAdapter;
    private EditText mBoxName;
    private RadioGroup mStorageButtonGroup;
    private ListView mIngredientsList;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (AddBoxDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
            + " must implement AddBoxDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_box, null);
        mBoxName = (EditText) dialogView.findViewById(R.id.edittext_add_box_name);
        mStorageButtonGroup = (RadioGroup) dialogView.findViewById(R.id.radiogroup_storage_type);
        mIngredientsList = (ListView) dialogView.findViewById(R.id.listview_ingredient_dialog);

        Cursor cur = getActivity().getContentResolver().query(
                FoodContract.IngredientEntry.buildAllIngredientUri(),
                null,
                null,
                null,
                null
        );

        mIngredientAdapter = new IngredientFragment.IngredientAdapter(getActivity(), cur, 0);
        mIngredientsList.setAdapter(mIngredientAdapter);

        builder.setView(dialogView)
                .setTitle(R.string.add_box)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String boxName = mBoxName.getText().toString();

                        Time dateTime = new Time();
                        dateTime.setToNow();
                        int julianDay = Time.getJulianDay(System.currentTimeMillis(), dateTime.gmtoff);

                        int storageType = mStorageButtonGroup.getCheckedRadioButtonId(); // 0 = fridge, 1 = freezer
                        int hasBeenInFreezer = storageType; // 0 = false, 1 = true. true IF storageType has been 1 at any time.

                        // TODO: change from dummy data to real data here

                       long[] ingredientsId = mIngredientsList.getCheckedItemIds();
                        for(int i = 0; i < ingredientsId.length; i++) {
                            Log.d(LOG_TAG, "ingredientsId " + i + ": " + ingredientsId[i]);
                        }

                        mListener.onAddBoxDialogPositiveClick(
                                boxName,
                                julianDay,
                                storageType,
                                hasBeenInFreezer,
                                ingredientsId);
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
