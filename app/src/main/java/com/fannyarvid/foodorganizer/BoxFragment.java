package com.fannyarvid.foodorganizer;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by FannyArvid on 2015-04-28.
 */
public class BoxFragment extends Fragment {

    private BoxAdapter mBoxAdapter;
    private ListView mListView;

    private static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mBoxAdapter = new BoxAdapter(getActivity(), null, 0);

        View view = inflater.inflate(R.layout.fragment_box, container, false);

        mListView = (ListView) view.findViewById(R.id.listview_box);
        mListView.setAdapter(mBoxAdapter);


        return view;
    }

    public static BoxFragment newInstance(int sectionNumber) {
        BoxFragment fragment = new BoxFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public static class BoxAdapter extends CursorAdapter {

        private static final int VIEW_TYPE_ADD_BOX = 0;
        private static final int VIEW_TYPE_BOX = 1;
        private static final int VIEW_TYPE_COUNT = 2;

        public BoxAdapter(Context context, Cursor cursor, int flags) { super(context, cursor, flags);}

        @Override
        public int getItemViewType(int position) {
            return (position == 0) ? VIEW_TYPE_ADD_BOX : VIEW_TYPE_BOX;
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
                case VIEW_TYPE_ADD_BOX: {
                    // TODO: Add code to find the layout id for the "add box" button layout

                    // Following layout is just for now
                    layoutId = R.layout.list_item_box;

                    break;
                }
                case VIEW_TYPE_BOX: {
                    layoutId = R.layout.list_item_box;
                    break;
                }
            }
            return LayoutInflater.from(context).inflate(layoutId, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            BoxListViewHolder viewHolder = (BoxListViewHolder) view.getTag();

            int viewType = getItemViewType(cursor.getPosition());
            switch (viewType) {
                case VIEW_TYPE_ADD_BOX: {
                    // TODO: Add code to build the "add box" button layout
                    viewHolder
                            .nameView
                            .setText("Test"
                            );
                    break;
                }
                case VIEW_TYPE_BOX: {
                    // TODO: Add code to get name from cursor after implementing database
                    // String name = cursor.getString()
                    viewHolder
                            .nameView
                            .setText("Test"
                            );
                    break;
                }
            }
        }

        public static class BoxListViewHolder {
            public final TextView nameView;

            public BoxListViewHolder(View view) {
                nameView = (TextView) view.findViewById(R.id.list_item_name);
            }
        }
    }
}
