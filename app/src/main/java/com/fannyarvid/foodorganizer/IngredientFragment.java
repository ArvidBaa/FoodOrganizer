package com.fannyarvid.foodorganizer;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by FannyArvid on 2015-04-28.
 */
public class IngredientFragment extends Fragment {

    private ArrayAdapter<String> mIngredientAdapter;
    // TODO: Use this code when leaving the dummy data
    // private IngredientAdapter mIngredientAdapter;
    private ListView mListView;

    private static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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

        private static final int VIEW_TYPE_ADD_INGREDIENT = 0;
        private static final int VIEW_TYPE_INGREDIENT = 1;
        private static final int VIEW_TYPE_COUNT = 2;

        public IngredientAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
        }

        @Override
        public int getItemViewType(int position) {
            return (position == 0) ? VIEW_TYPE_ADD_INGREDIENT : VIEW_TYPE_INGREDIENT;
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE_COUNT;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            int viewType = getItemViewType(cursor.getPosition());
            int layoutId = -1;
            switch (viewType) {
                case VIEW_TYPE_ADD_INGREDIENT: {
                    // TODO: Add code to find the layour id for the "add ingredient" button layout


                    // Following layout is temporary until above is finished
                    layoutId = R.layout.list_item_ingredient;

                    break;
                }
                case VIEW_TYPE_INGREDIENT: {
                    layoutId = R.layout.list_item_ingredient;
                    break;
                }
            }
            return  LayoutInflater.from(context).inflate(layoutId, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            IngredientViewHolder viewHolder = (IngredientViewHolder) view.getTag();
            int viewType = getItemViewType(cursor.getPosition());
            switch (viewType) {
                case VIEW_TYPE_ADD_INGREDIENT: {
                    // TODO: Add code to build the "add ingredient button layout
                    viewHolder
                            .nameView
                            .setText("Test add ingredient"
                            );
                    break;
                }
                case VIEW_TYPE_INGREDIENT: {
                    // TODO: Add code to get ingredient name from cursor after implementing database
                    // String name = cursor.getString()
                    viewHolder
                            .nameView
                            .setText("Test ingredient"
                            );
                    break;
                }
            }
        }

        public static class IngredientViewHolder {
            public final TextView nameView;

            public IngredientViewHolder(View view) {
                nameView = (TextView) view.findViewById(R.id.list_item_name);
            }
        }
    }
}
