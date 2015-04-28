package com.fannyarvid.foodorganizer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by FannyArvid on 2015-04-28.
 */
public class IngredientFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_box, container, false);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static IngredientFragment newInstance(int sectionNumber) {
        IngredientFragment fragment = new IngredientFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
}
