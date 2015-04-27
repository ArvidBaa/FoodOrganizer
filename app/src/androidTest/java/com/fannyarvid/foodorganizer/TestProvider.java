package com.fannyarvid.foodorganizer;

import android.test.AndroidTestCase;

import com.fannyarvid.foodorganizer.data.FoodContract.BoxEntry;
import com.fannyarvid.foodorganizer.data.FoodContract.FoodEntry;
import com.fannyarvid.foodorganizer.data.FoodContract.IngredientEntry;


/**
 * Created by FannyArvid on 2015-04-27.
 */
public class TestProvider extends AndroidTestCase {
    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                FoodEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                BoxEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                IngredientEntry.CONTENT_URI,
                null,
                null
        );
    }

}
