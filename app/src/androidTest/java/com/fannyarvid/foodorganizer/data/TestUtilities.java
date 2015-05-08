package com.fannyarvid.foodorganizer.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.fannyarvid.foodorganizer.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/**
 * Created by FannyArvid on 2015-04-27.
 */
public class TestUtilities extends AndroidTestCase {
    static final long TEST_DATE = 1419033600L;  // December 20th, 2014

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + valueCursor.getString(idx) +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createFoodValues(long boxRowId, long ingredientRowId) {
        ContentValues foodValues = new ContentValues();
        foodValues.put(FoodContract.FoodEntry.COLUMN_BOX_KEY, boxRowId);
        foodValues.put(FoodContract.FoodEntry.COLUMN_INGREDIENT_KEY, ingredientRowId);
        return foodValues;
    }

    static ContentValues createBoxValues() {
        ContentValues boxValues = new ContentValues();
        boxValues.put(FoodContract.BoxEntry.COLUMN_DATE, TEST_DATE);
        boxValues.put(FoodContract.BoxEntry.COLUMN_BOX_NAME, "Bröd");
        boxValues.put(FoodContract.BoxEntry.COLUMN_STORAGE_TYPE, 0);
        boxValues.put(FoodContract.BoxEntry.COLUMN_HAS_BEEN_IN_FREEZER, 0);
        return boxValues;
    }

    static ContentValues createIngredientValues() {
        ContentValues ingredientValues = new ContentValues();
        ingredientValues.put(FoodContract.IngredientEntry.COLUMN_INGREDIENT_NAME, "Mjöl");
        ingredientValues.put(FoodContract.IngredientEntry.COLUMN_STORAGE_TIME_FREEZER, 10);
        ingredientValues.put(FoodContract.IngredientEntry.COLUMN_STORAGE_TIME_FRIDGE, 5);
        ingredientValues.put(FoodContract.IngredientEntry.COLUMN_IS_INITIAL_INGREDIENT, 1);
        return ingredientValues;
    }

    static long insertBoxValues(Context context) {
        FoodDbHelper dbHelper = new FoodDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createBoxValues();

        long boxRowId = db.insert(FoodContract.BoxEntry.TABLE_NAME, null, testValues);

        assertTrue("Error: Failure to insert Box test values", boxRowId != -1);

        return boxRowId;
    }

    static long insertIngredientValues(Context context) {
        FoodDbHelper dbHelper = new FoodDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createIngredientValues();

        long ingredientRowId = db.insert(FoodContract.IngredientEntry.TABLE_NAME, null, testValues);

        assertTrue("Error: Failure to insert Ingredient test values", ingredientRowId != -1);

        return ingredientRowId;
    }

    /*
        Students: The functions we provide inside of TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
        CTS tests.

        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
