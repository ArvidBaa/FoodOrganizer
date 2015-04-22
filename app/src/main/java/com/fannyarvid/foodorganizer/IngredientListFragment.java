package com.fannyarvid.foodorganizer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by FannyArvid on 2015-04-22.
 */
public class IngredientListFragment extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.fragment_ingredient_list, container, false);
        ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                "Test ingredient");
        return rootView;
    }
}
