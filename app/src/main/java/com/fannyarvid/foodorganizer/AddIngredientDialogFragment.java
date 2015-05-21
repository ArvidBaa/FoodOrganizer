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

/**
 * Created by FannyArvid on 2015-05-08.
 */
public class AddIngredientDialogFragment extends DialogFragment {

    private static final String LOG_TAG = AddIngredientDialogFragment.class.getSimpleName();

    public interface AddIngredientDialogListener {
        public void onAddIngredientDialogPositiveClick(
                String ingredientName, int fridgeTime, int freezerTime, int isInitialIngredient);
    }

    AddIngredientDialogListener mListener;
    private EditText mIngredientName;
    private EditText mFridgeTime;
    private EditText mFreezerTime;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (AddIngredientDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AddIngredientDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_ingredient, null);
        mIngredientName = (EditText) dialogView.findViewById(R.id.edittext_add_ingredient_name);
        mFridgeTime = (EditText) dialogView.findViewById(R.id.edittext_add_ingredient_fridge_time);
        mFreezerTime = (EditText) dialogView.findViewById(R.id.edittext_add_ingredient_freezer_time);

        builder.setView(dialogView)
                .setTitle(R.string.add_ingredient)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // TODO: Fix error if any of the times are ""
                        String ingredientName = mIngredientName.getText().toString();
                        int fridgeTime = Integer.valueOf(mFridgeTime.getText().toString());
                        int freezerTime = Integer.valueOf(mFreezerTime.getText().toString());
                        int isInitialIngredient = 0; // 0 --> false.

                        mListener.onAddIngredientDialogPositiveClick(
                                ingredientName,
                                fridgeTime,
                                freezerTime,
                                isInitialIngredient
                        );
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddIngredientDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
