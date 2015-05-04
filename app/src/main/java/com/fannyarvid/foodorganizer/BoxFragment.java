package com.fannyarvid.foodorganizer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
public class BoxFragment extends Fragment {



    private ArrayAdapter<String> mBoxAdapter;
    // TODO: Use this code when leaving the dummy data
    // private BoxAdapter mBoxAdapter;
    private ListView mListView;

    private static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Dummy data for the ListView
        String[] data = {
                "Spaghetti",
                "Svamspoppa",
                "Pannkakor",
                "Potatisgratäng",
                "Tortellini",
                "Lasagne",
                "Tomatsoppa",
                "Carbonara",
                "Plättar",
                "Tacos",
                "Korvstroganoff",
                "Lax",
                "Kycklingwrap",
                "Biffar",
                "Köttfärssås",
                "Risgrynsgröt"
        };
        List<String> boxNames = new ArrayList<String>(Arrays.asList(data));
        mBoxAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.list_item_box,
                        R.id.list_item_name,
                        boxNames
                );

        // Use the following code when leaving the dummy data
        //mBoxAdapter = new BoxAdapter(getActivity(), null, 0);

        View view = inflater.inflate(R.layout.fragment_box, container, false);

        mListView = (ListView) view.findViewById(R.id.listview_box);
        mListView.setAdapter(mBoxAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String box = mBoxAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), FoodActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, box);
                startActivity(intent);
            }
        });

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
            BoxListViewHolder viewHolder = (BoxListViewHolder) view.getTag();
            viewHolder.nameView.setText("Test box");
        }

        public static class BoxListViewHolder {
            public final TextView nameView;

            public BoxListViewHolder(View view) {
                nameView = (TextView) view.findViewById(R.id.list_item_name);
            }
        }
    }
}
