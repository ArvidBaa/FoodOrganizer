package com.fannyarvid.foodorganizer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fannyarvid.foodorganizer.data.FoodContract;

/**
 * Created by FannyArvid on 2015-04-28.
 */
public class IngredientFragment extends Fragment {

    private IngredientAdapter mIngredientAdapter;
    private ListView mListView;

    private static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Uri allIngredientUri = FoodContract.IngredientEntry.buildAllIngredientUri();
        Cursor cur = getActivity().getContentResolver().query(
                allIngredientUri,
                null,
                null,
                null,
                null
        );
        mIngredientAdapter = new IngredientAdapter(getActivity(), cur, 0);

        View view = inflater.inflate(R.layout.fragment_ingredient, container, false);

        mListView = (ListView) view.findViewById(R.id.listview_ingredient);
        mListView.setAdapter(mIngredientAdapter);

        return view;
    }

    public static IngredientFragment newInstance(int sectionNumber) {
        IngredientFragment fragment = new IngredientFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public static class IngredientAdapter extends CursorAdapter {

        private static final String LOG_TAG = IngredientAdapter.class.getSimpleName();

        public IngredientAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.list_item_ingredient, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            // TODO: Use the viewHolder class?
            //IngredientViewHolder viewHolder = (IngredientViewHolder) view.getTag();
            //viewHolder.nameView.setText("Test ingredient");

            TextView textView = (TextView) view.findViewById(R.id.ingredient_list_item_name);
            int idx_ingredient_name = cursor.getColumnIndex(FoodContract.IngredientEntry.COLUMN_INGREDIENT_NAME);
            String ingredientNameStr = cursor.getString(idx_ingredient_name);
            textView.setText(ingredientNameStr);
        }

        public static class IngredientViewHolder {
            public final TextView nameView;

            public IngredientViewHolder(View view) {
                nameView = (TextView) view.findViewById(R.id.ingredient_list_item_name);
            }
        }
    }
}
