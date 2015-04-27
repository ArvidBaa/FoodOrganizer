package com.fannyarvid.foodorganizer;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.fannyarvid.foodorganizer.data.FoodContract;

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
        boxValues.put(FoodContract.BoxEntry.COLUMN_FOOD_NAME, "Bröd");
        boxValues.put(FoodContract.BoxEntry.COLUMN_STORAGE_TYPE, 0);
        boxValues.put(FoodContract.BoxEntry.COLUMN_HAS_BEEN_IN_FREEZER, 0);
        return boxValues;
    }

    static ContentValues createIngredientValues() {
        ContentValues ingredientValues = new ContentValues();
        ingredientValues.put(FoodContract.IngredientEntry.COLUMN_INGREDIENT_NAME, "Mjöl");
        ingredientValues.put(FoodContract.IngredientEntry.COLUMN_STORAGE_TIME_FREEZER, 10);
        ingredientValues.put(FoodContract.IngredientEntry.COLUMN_STORAGE_TIME_FRIDGE, 5);
        return ingredientValues;
    }
}
