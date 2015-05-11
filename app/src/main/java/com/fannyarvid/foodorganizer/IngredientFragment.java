package com.fannyarvid.foodorganizer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fannyarvid.foodorganizer.data.FoodContract;

/**
 * Created by FannyArvid on 2015-04-28.
 */
public class IngredientFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int INGREDIENT_LOADER = 0;

    private static final String[] INGREDIENT_COLUMNS = {
            FoodContract.IngredientEntry.TABLE_NAME + "." + FoodContract.IngredientEntry._ID,
            FoodContract.IngredientEntry.COLUMN_INGREDIENT_NAME,
            FoodContract.IngredientEntry.COLUMN_STORAGE_TIME_FRIDGE,
            FoodContract.IngredientEntry.COLUMN_STORAGE_TIME_FREEZER,
            FoodContract.IngredientEntry.COLUMN_IS_INITIAL_INGREDIENT
    };

    static final int COL_INGREDIENT_ID = 0;
    static final int COL_INGREDIENT_NAME = 1;
    static final int COL_STORAGE_TIME_FRIDGE = 2;
    static final int COL_STORAGE_TIME_FREEZER = 3;
    static final int COL_IS_INITIAL_INGREDIENT = 4;

    private IngredientAdapter mIngredientAdapter;
    private ListView mListView;
    private Button mButton;

    private static final String ARG_SECTION_NUMBER = "section_number";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mIngredientAdapter = new IngredientAdapter(getActivity(), null, 0);

        View view = inflater.inflate(R.layout.fragment_ingredient, container, false);

        mListView = (ListView) view.findViewById(R.id.listview_ingredient);
        mListView.setAdapter(mIngredientAdapter);

        mButton = (Button) view.findViewById(R.id.button_add_ingredient);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddIngredientDialogFragment dialog = new AddIngredientDialogFragment();
                dialog.show(getActivity().getFragmentManager(), "AddIngredientDialogFragment");
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(INGREDIENT_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public static IngredientFragment newInstance(int sectionNumber) {
        IngredientFragment fragment = new IngredientFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri allIngredientUri = FoodContract.IngredientEntry.buildAllIngredientUri();
        return new CursorLoader(getActivity(),
                allIngredientUri,
                INGREDIENT_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mIngredientAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mIngredientAdapter.swapCursor(null);
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
            String ingredientNameStr = cursor.getString(COL_INGREDIENT_NAME);
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
