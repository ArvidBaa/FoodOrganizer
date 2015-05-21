package com.fannyarvid.foodorganizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.Time;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fannyarvid.foodorganizer.data.FoodContract;

import java.util.ArrayList;

/**
 * Created by FannyArvid on 2015-05-11.
 */
public class AddBoxDialogFragment extends DialogFragment {

    private static final String LOG_TAG = AddBoxDialogFragment.class.getSimpleName();

    public interface AddBoxDialogListener {
        public void onAddBoxDialogPositiveClick(
                String boxName, int date, int storageType, int hasBeenInFreezer, ArrayList<Integer> ingredientsId);
    }

    AddBoxDialogListener mListener;
    private IngredientCheckboxAdapter mIngredientAdapter;
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

        mIngredientAdapter = new IngredientCheckboxAdapter(getActivity(), cur, 0);
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

                        ArrayList<Integer> ingredientsId = mIngredientAdapter.getCheckedId();

                        mListener.onAddBoxDialogPositiveClick(
                                boxName,
                                julianDay,
                                storageType,
                                hasBeenInFreezer,
                                ingredientsId
                        );
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

    public static class IngredientCheckboxAdapter extends CursorAdapter implements CompoundButton.OnCheckedChangeListener {

        private static final String LOG_TAG = IngredientCheckboxAdapter.class.getSimpleName();

        SparseBooleanArray mCheckStates = new SparseBooleanArray();

        public IngredientCheckboxAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.list_item_ingredient_checkbox, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            // TODO: Use the viewHolder class?
            //IngredientViewHolder viewHolder = (IngredientViewHolder) view.getTag();
            //viewHolder.nameView.setText("Test ingredient");

            CheckBox checkBox = (CheckBox) view.findViewById(R.id.ingredient_list_item_name_checkbox);
            String ingredientNameStr = cursor.getString(IngredientFragment.COL_INGREDIENT_NAME);
            checkBox.setOnCheckedChangeListener(this);
            checkBox.setTag(cursor.getPosition());
            checkBox.setText(ingredientNameStr);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mCheckStates.put((Integer) buttonView.getTag(),isChecked);
        }

        public boolean isChecked(int position) {
            return mCheckStates.get(position, false);
        }

        public void setChecked(int position, boolean isChecked) {
            mCheckStates.put(position, isChecked);
        }

        public void toggle(int position) {
            setChecked(position, !isChecked(position));
        }

        public ArrayList<Integer> getCheckedId() {
            ArrayList<Integer> returnId = new ArrayList<Integer>(mCheckStates.size());
            for (int i = 0; i < mCheckStates.size(); i++) {
                if(mCheckStates.valueAt(i)) {
                    returnId.add(mCheckStates.keyAt(i));
                }
            }
            return returnId;
        }

        public static class IngredientViewHolder {
            public final TextView nameView;

            public IngredientViewHolder(View view) {
                nameView = (TextView) view.findViewById(R.id.ingredient_list_item_name);
            }
        }
    }
}
