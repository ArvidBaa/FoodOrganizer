package com.fannyarvid.foodorganizer.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by FannyArvid on 2015-04-24.
 */
public class FoodProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FoodDbHelper mOpenHelper;

    /*
    static final int BOX = ###;
    static final int INGREDIENT = ###;
    static final int FOOD = ###;
    static final int FOOD_WITH_BOX = ###;

    */

    private static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority = FoodContract.CONTENT_AUTHORITY;

        /*
        uriMatcher.addURI(authority, FoodContract.PATH_BOX, BOX );
        uriMatcher.addURI(authority, FoodContract.PATH_INGREDIENT, INGREDIENT);
        uriMatcher.addURI(authority, FoodContract.PATH_FOOD, FOOD);
        */

        return null;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
