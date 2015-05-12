package com.fannyarvid.foodorganizer;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fannyarvid.foodorganizer.data.FoodContract;

import java.util.Locale;


public class MainActivity extends ActionBarActivity
        implements AddIngredientDialogFragment.AddIngredientDialogListener, AddBoxDialogFragment.AddBoxDialogListener {

    public static final int PAGE_BOX_LIST = 0;
    public static final int PAGE_INGREDIENT_LIST = 1;
    public static final int NUM_PAGES = 2;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddIngredientDialogPositiveClick(
            String ingredientName, int fridgeTime, int freezerTime, int isInitialIngredient) {
        // TODO: Make sure the listView updates after adding a new ingredient
        ContentValues values = new ContentValues();
        values.put(FoodContract.IngredientEntry.COLUMN_INGREDIENT_NAME, ingredientName);
        values.put(FoodContract.IngredientEntry.COLUMN_STORAGE_TIME_FRIDGE, fridgeTime);
        values.put(FoodContract.IngredientEntry.COLUMN_STORAGE_TIME_FREEZER, freezerTime);
        values.put(FoodContract.IngredientEntry.COLUMN_IS_INITIAL_INGREDIENT, isInitialIngredient);

        getContentResolver().insert(FoodContract.IngredientEntry.buildAllIngredientUri(), values);
    }

    @Override
    public void onAddBoxDialogPositiveClick(
            String boxName, int julianDay, int storageType, int hasBeenInFreezer, long[] ingredientsId) {
        ContentValues boxValues = new ContentValues();
        boxValues.put(FoodContract.BoxEntry.COLUMN_BOX_NAME, boxName);
        boxValues.put(FoodContract.BoxEntry.COLUMN_DATE, julianDay);
        boxValues.put(FoodContract.BoxEntry.COLUMN_STORAGE_TYPE, storageType);
        boxValues.put(FoodContract.BoxEntry.COLUMN_HAS_BEEN_IN_FREEZER, hasBeenInFreezer);

        Uri boxUri = getContentResolver().insert(FoodContract.BoxEntry.buildAllBoxUri(), boxValues);

        int boxId = FoodContract.BoxEntry.getIdFromUri(boxUri);
        ContentValues foodValues = new ContentValues();
        for (int i = 0; i < ingredientsId.length; i++) {
            foodValues.put(FoodContract.FoodEntry.COLUMN_BOX_KEY, boxId);
            foodValues.put(FoodContract.FoodEntry.COLUMN_INGREDIENT_KEY, ingredientsId[i]);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position) {
                case PAGE_BOX_LIST: return BoxFragment.newInstance(position);
                case PAGE_INGREDIENT_LIST: return IngredientFragment.newInstance(position);
                default: return BoxFragment.newInstance(position);
            }
        }

        @Override
        public int getCount() {
            // Show NUM_PAGES total pages.
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
