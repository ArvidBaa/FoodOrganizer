package com.fannyarvid.foodorganizer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fannyarvid.foodorganizer.data.FoodContract.BoxEntry;
import com.fannyarvid.foodorganizer.data.FoodContract.IngredientEntry;
import com.fannyarvid.foodorganizer.data.FoodContract.LinkEntry;

/**
 * Created by FannyArvid on 2015-04-24.
 */
public class FoodDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "food.db";

    public FoodDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_BOX_TABLE = "CREATE TABLE " +
                BoxEntry.TABLE_NAME + " (" +
                BoxEntry._ID + " INTEGER PRIMARY KEY, " +
                BoxEntry.COLUMN_FOOD_NAME + " TEXT NOT NULL, " +
                BoxEntry.COLUMN_DATE + " INTEGER NOT NULL AUTOINCREMENT, " +
                BoxEntry.COLUMN_STORAGE_TYPE + " INTEGER NOT NULL, " +
                BoxEntry.COLUMN_HAS_BEEN_IN_FREEZER + " INTEGER NOT NULL " +
                " );";

        final String SQL_CREATE_INGREDIENT_TABLE = "CREATE TABLE " +
                IngredientEntry.TABLE_NAME + " (" +
                IngredientEntry._ID + " INTEGER PRIMARY KEY, " +
                IngredientEntry.COLUMN_INGREDIENT_NAME + " TEXT NOT NULL, " +
                IngredientEntry.COLUMN_STORAGE_TIME_FREEZER + " INTEGER NOT NULL, " +
                IngredientEntry.COLUMN_STORAGE_TIME_FRIDGE + " INTEGER NOT NULL " +
                " );";

        final String SQL_CREATE_LINK_TABLE = "CREATE TABLE " +
                LinkEntry.TABLE_NAME + " (" +
                LinkEntry._ID + " INTEGER PRIMARY KEY, " +
                LinkEntry.COLUMN_BOX_KEY + " INTEGER NOT NULL, " +
                LinkEntry.COLUMN_INGREDIENT_KEY + " INTEGER NOT NULL, " +

                " FOREIGN KEY (" + LinkEntry.COLUMN_BOX_KEY + ") REFERENCES " +
                BoxEntry.TABLE_NAME + " (" + BoxEntry._ID + "), " +

                " FOREIGN KEY (" + LinkEntry.COLUMN_INGREDIENT_KEY + ") REFERENCES " +
                IngredientEntry.TABLE_NAME + " (" + IngredientEntry._ID + ");";

        db.execSQL(SQL_CREATE_BOX_TABLE);
        db.execSQL(SQL_CREATE_INGREDIENT_TABLE);
        db.execSQL(SQL_CREATE_LINK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + BoxEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + IngredientEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LinkEntry.TABLE_NAME);
        onCreate(db);

    }
}
