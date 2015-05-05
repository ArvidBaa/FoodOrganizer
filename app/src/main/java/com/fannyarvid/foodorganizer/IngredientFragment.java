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

    // private ArrayAdapter<String> mIngredientAdapter;
    // TODO: Use this code when leaving the dummy data
    private IngredientAdapter mIngredientAdapter;
    private ListView mListView;

    private static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /*
        // Dummy data for the ListView
        String[] data = {
                "Mjöl",
                "Smör",
                "Mjölk",
                "Kyckling - rå",
                "Kyckling - stekt",
                "Kyckling - kokt",
                "Grädde",
                "Ärtor",
                "Svamp",
                "Korv",
                "Ägg - rå",
                "Ägg - stekt",
                "Ägg - kokt",
                "Broccoli"
        };
        List<String> ingredientNames = new ArrayList<String>(Arrays.asList(data));
        mIngredientAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.list_item_ingredient,
                        R.id.list_item_name,
                        ingredientNames
                );
        */

        Uri allIngredientUri = FoodContract.IngredientEntry.buildAllIngredientUri();
        Cursor cur = getActivity().getContentResolver().query(
                allIngredientUri,
                null,
                null,
                null,
                null
        );;
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

        public IngredientAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.list_item_ingredient, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            // TODO: Add data from cursor instead of dummy data
            //IngredientViewHolder viewHolder = (IngredientViewHolder) view.getTag();
            //viewHolder.nameView.setText("Test ingredient");

            TextView textView = (TextView) view.findViewById(R.id.list_item_name);
            int idx_ingredient_name = cursor.getColumnIndex(FoodContract.IngredientEntry.COLUMN_INGREDIENT_NAME);
            String ingredientNameStr = cursor.getString(idx_ingredient_name);
            textView.setText(ingredientNameStr);
        }

        public static class IngredientViewHolder {
            public final TextView nameView;

            public IngredientViewHolder(View view) {
                nameView = (TextView) view.findViewById(R.id.list_item_name);
            }
        }
    }
}
