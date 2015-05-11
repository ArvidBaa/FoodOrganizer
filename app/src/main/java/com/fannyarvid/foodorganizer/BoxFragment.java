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
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fannyarvid.foodorganizer.data.FoodContract;

/**
 * Created by FannyArvid on 2015-04-28.
 */
public class BoxFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int BOX_LOADER = 0;

    private static final String[] BOX_COLUMNS = {
            FoodContract.BoxEntry.TABLE_NAME + "." + FoodContract.BoxEntry._ID,
            FoodContract.BoxEntry.COLUMN_BOX_NAME,
            FoodContract.BoxEntry.COLUMN_DATE,
            FoodContract.BoxEntry.COLUMN_STORAGE_TYPE,
            FoodContract.BoxEntry.COLUMN_HAS_BEEN_IN_FREEZER
    };

    static final int COL_BOX_ID = 0;
    static final int COL_BOX_NAME = 1;
    static final int COL_DATE = 2;
    static final int COL_STORAGE_TYPE = 3;
    static final int COL_HAS_BEEN_IN_FREEZER = 4;

    private BoxAdapter mBoxAdapter;
    private ListView mListView;

    private static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mBoxAdapter = new BoxAdapter(getActivity(), null, 0);

        View view = inflater.inflate(R.layout.fragment_box, container, false);

        mListView = (ListView) view.findViewById(R.id.listview_box);
        mListView.setAdapter(mBoxAdapter);
        // TODO: Code below should make a detail view of a "box" appear when clicking on a box item in the listView
        /*
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String box = mBoxAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), FoodActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, box);
                startActivity(intent);
            }
        });
        */

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(BOX_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public static BoxFragment newInstance(int sectionNumber) {
        BoxFragment fragment = new BoxFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri allBoxUri = FoodContract.BoxEntry.buildAllBoxUri();
        return new CursorLoader(getActivity(),
                allBoxUri,
                BOX_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mBoxAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mBoxAdapter.swapCursor(null);
    }

    public static class BoxAdapter extends CursorAdapter {

        private static final String LOG_TAG = BoxAdapter.class.getSimpleName();

        public BoxAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.list_item_box, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            // TODO: Add data from cursor instead of dummy data
            // BoxListViewHolder viewHolder = (BoxListViewHolder) view.getTag();
            // viewHolder.nameView.setText("Test box");

            TextView textView = (TextView) view.findViewById(R.id.box_list_item_name);
            String boxNameStr = cursor.getString(COL_BOX_NAME);
            textView.setText(boxNameStr);
        }

        public static class BoxListViewHolder {
            public final TextView nameView;

            public BoxListViewHolder(View view) {
                nameView = (TextView) view.findViewById(R.id.box_list_item_name);
            }
        }
    }
}
