package com.fannyarvid.foodorganizer.data;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
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
        mContext.getContentResolver() .delete(
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

        Cursor cursor = mContext.getContentResolver().query(
                FoodEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Food table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                BoxEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Box table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                IngredientEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Ingredient table during delete", 0, cursor.getCount());
        cursor.close();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecordsFromProvider();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                FoodProvider.class.getName());
        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            assertEquals("Error: FoodProvider registered with authority: " + providerInfo.authority +
            " instead of authority: " + FoodContract.CONTENT_AUTHORITY,
                    providerInfo.authority, FoodContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            assertTrue("Error: FoodProvider not registered at " + mContext.getPackageName(), false);
        }
    }

    public void testGetType() {
        String type = mContext.getContentResolver().getType(FoodEntry.CONTENT_URI);

        assertEquals("Error: the FoodEntry CONTENT_UTI should return FoodEntry.CONTENT_TYPE",
                FoodEntry.CONTENT_TYPE, type);

        long testBoxNo = 1;

        type = mContext.getContentResolver().getType(
                FoodEntry.buildFoodWithBox(testBoxNo));

        assertEquals("The FoodEntry CONTENT _URI with boxNo should return FoodEntry.CONTENT_TYPE",
                FoodEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(BoxEntry.CONTENT_URI);

        assertEquals("The BoxEntry CONTENT _URI should return BoxEntry.CONTENT_TYPE",
                BoxEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(IngredientEntry.CONTENT_URI);

        assertEquals("The BoxEntry CONTENT _URI should return IngredientEntry.CONTENT_TYPE",
                IngredientEntry.CONTENT_TYPE, type);
    }

    public void testBasicFoodQuery() {
        FoodDbHelper dbHelper = new FoodDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long boxRowId = TestUtilities.insertBoxValues(mContext);
        long ingredientRowId = TestUtilities.insertIngredientValues(mContext);

        ContentValues foodValues = TestUtilities.createFoodValues(boxRowId, ingredientRowId);

        long foodRowId = db.insert(FoodEntry.TABLE_NAME, null, foodValues);
        assertTrue("Unable to Insert FoodEntry into the Database", foodRowId != -1);

        db.close();

        Cursor foodCursor = mContext.getContentResolver().query(
                FoodEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testBasicFoodQuery", foodCursor, foodValues);
    }

    public void testBasicBoxQueries() {
        FoodDbHelper dbHelper = new FoodDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createBoxValues();
        long boxRowId = TestUtilities.insertBoxValues(mContext);

        Cursor boxCursor = mContext.getContentResolver().query(
                BoxEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testBasicBoxQueries, box query", boxCursor, testValues);

        if (Build.VERSION.SDK_INT >= 19) {
            assertEquals("Error: Box Query did not properly set NotificationUri",
                    boxCursor.getNotificationUri(), BoxEntry.CONTENT_URI);
        }
    }

    public void testBasicIngredientQueries() {
        FoodDbHelper dbHelper = new FoodDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createIngredientValues();
        long ingredientRowId = TestUtilities.insertIngredientValues(mContext);

        Cursor ingredientCursor = mContext.getContentResolver().query(
                IngredientEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testBasicIngredientQueries, ingredient query", ingredientCursor, testValues);

        if (Build.VERSION.SDK_INT >= 19) {
            assertEquals("Error: Ingredient Query did not properly set NotificationUri",
                    ingredientCursor.getNotificationUri(), IngredientEntry.CONTENT_URI);
        }
    }
}
