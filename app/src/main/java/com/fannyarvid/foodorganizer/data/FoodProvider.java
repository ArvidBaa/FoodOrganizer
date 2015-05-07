package com.fannyarvid.foodorganizer.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by FannyArvid on 2015-04-24.
 */
public class FoodProvider extends ContentProvider {

    public static final String LOG_TAG = FoodProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FoodDbHelper mOpenHelper;


    static final int FOOD = 100;
    static final int FOOD_WITH_BOX = 101;
    static final int BOX = 200;
    static final int INGREDIENT = 300;

    private static final SQLiteQueryBuilder sFoodByBoxQueryBuilder;

    static{
        sFoodByBoxQueryBuilder = new SQLiteQueryBuilder();

        sFoodByBoxQueryBuilder.setTables(
                FoodContract.FoodEntry.TABLE_NAME + " INNER JOIN " +
                        FoodContract.BoxEntry.TABLE_NAME +
                        " ON " + FoodContract.FoodEntry.TABLE_NAME +
                        "." + FoodContract.FoodEntry.COLUMN_BOX_KEY +
                        " = " + FoodContract.BoxEntry.TABLE_NAME +
                        "." + FoodContract.BoxEntry._ID + " INNER JOIN " +
                        FoodContract.IngredientEntry.TABLE_NAME +
                        " ON " + FoodContract.FoodEntry.TABLE_NAME +
                        "." + FoodContract.FoodEntry.COLUMN_INGREDIENT_KEY +
                        " = " + FoodContract.IngredientEntry.TABLE_NAME +
                        "." + FoodContract.IngredientEntry._ID);
    }

    private static final String sBoxSelection =
            FoodContract.BoxEntry.TABLE_NAME +
            "." + FoodContract.BoxEntry._ID + " = ? ";

    private Cursor getFoodByBox(Uri uri, String[] projection, String sortOrder) {
        long boxNo = FoodContract.FoodEntry.getBoxNoFromUri(uri);

        return sFoodByBoxQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sBoxSelection,
                new String[]{Long.toString(boxNo)},
                null,
                null,
                sortOrder
        );
    }

    private static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority = FoodContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, FoodContract.PATH_FOOD, FOOD);
        uriMatcher.addURI(authority, FoodContract.PATH_FOOD + "/*", FOOD_WITH_BOX );
        uriMatcher.addURI(authority, FoodContract.PATH_BOX, BOX );
        uriMatcher.addURI(authority, FoodContract.PATH_INGREDIENT, INGREDIENT);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new FoodDbHelper(getContext());
        Log.d(LOG_TAG, "directory:" + getContext().getApplicationInfo().dataDir);

        return true;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match){
            case FOOD_WITH_BOX:
                return FoodContract.FoodEntry.CONTENT_TYPE;
            case FOOD:
                return FoodContract.FoodEntry.CONTENT_TYPE;
            case BOX:
                return FoodContract.BoxEntry.CONTENT_TYPE;
            case INGREDIENT:
                return FoodContract.IngredientEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case FOOD_WITH_BOX: {
                Log.i(LOG_TAG, "query switch: FOOD_WITH_BOX");
                retCursor = getFoodByBox(uri, projection, sortOrder);
                break;
            }
            case FOOD: {
                Log.i(LOG_TAG, "query switch: FOOD");
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FoodContract.FoodEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case BOX: {
                Log.i(LOG_TAG, "query switch: BOX");
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FoodContract.BoxEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case INGREDIENT: {
                Log.i(LOG_TAG, "query switch: INGREDIENT");
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FoodContract.IngredientEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FOOD: {
                normalizeDate(values);
                long _id = db.insert(FoodContract.FoodEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FoodContract.FoodEntry.buildFoodUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }
            case BOX: {
                normalizeDate(values);
                long _id = db.insert(FoodContract.BoxEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FoodContract.BoxEntry.buildBoxUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }
            case INGREDIENT: {
                normalizeDate(values);
                long _id = db.insert(FoodContract.IngredientEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FoodContract.IngredientEntry.buildIngredientUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    private void normalizeDate(ContentValues values) {
        if (values.containsKey(FoodContract.BoxEntry.COLUMN_DATE)) {
            long dateValue = values.getAsLong(FoodContract.BoxEntry.COLUMN_DATE);
            values.put(FoodContract.BoxEntry.COLUMN_DATE, FoodContract.normalizeDate(dateValue));
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted = 0;

        final int match = sUriMatcher.match(uri);

        if (null == selection ) selection = "1";
        switch (match) {
            case FOOD: {
                rowsDeleted = db.delete(
                        FoodContract.FoodEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case BOX: {
                rowsDeleted = db.delete(
                        FoodContract.BoxEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case INGREDIENT: {
                rowsDeleted = db.delete(
                        FoodContract.IngredientEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdated = 0;

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FOOD: {
                normalizeDate(values);
                rowsUpdated = db.update(
                        FoodContract.FoodEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case BOX: {
                normalizeDate(values);
                rowsUpdated = db.update(
                        FoodContract.BoxEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case INGREDIENT: {
                normalizeDate(values);
                rowsUpdated = db.update(
                        FoodContract.IngredientEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
        }

        if (rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
