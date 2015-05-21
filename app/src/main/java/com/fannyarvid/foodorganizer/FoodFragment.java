package com.fannyarvid.foodorganizer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.fannyarvid.foodorganizer.data.FoodContract.IngredientEntry;

/**
 * Created by FannyArvid on 2015-04-28.
 */
public class FoodFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String DETAIL_URI = "URI";

    private Uri mUri;
    private String[] mSelection;

    private static final int FOOD_LOADER = 0;

    private static final String[] INGREDIENT_COLUMNS = {
            IngredientEntry.TABLE_NAME + "." + IngredientEntry._ID,
            IngredientEntry.COLUMN_INGREDIENT_NAME,
            IngredientEntry.COLUMN_STORAGE_TIME_FRIDGE,
            IngredientEntry.COLUMN_STORAGE_TIME_FREEZER,
            IngredientEntry.COLUMN_IS_INITIAL_INGREDIENT
    };

    static final int COL_INGREDIENT_ID = 0;
    static final int COL_INGREDIENT_NAME = 1;
    static final int COL_STORAGE_TIME_FRIDGE = 2;
    static final int COL_STORAGE_TIME_FREEZER = 3;
    static final int COL_IS_INITIAL_INGREDIENT = 4;

    private FoodAdapter mFoodAdapter;
    // TODO: Add all views for this layout

    public FoodFragment() { setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(FoodFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_food, container, false);

        /*
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String boxStr = intent.getStringExtra(Intent.EXTRA_TEXT);
            ((TextView) rootView.findViewById(R.id.food_text))
                    .setText(boxStr);
        }
        */
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(FOOD_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mUri != null ) {
            // TODO: get boxId and use it to get corresponding ingredientsId from FoodTable and use that in turn as selection arguments below
            // long boxId =
            // String[] ingredientsId =
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    INGREDIENT_COLUMNS,
                    null, // Ingredient_id = ?
                    null, // String[] ingredientsId
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mFoodAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFoodAdapter.swapCursor(null);
    }

    public static class FoodAdapter extends CursorAdapter {

        private static final String LOG_TAG = FoodAdapter.class.getSimpleName();

        public FoodAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.list_item_ingredient, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // TODO: Set data to listview
            // TODO: Use viewHolder class?
        }
    }
}
