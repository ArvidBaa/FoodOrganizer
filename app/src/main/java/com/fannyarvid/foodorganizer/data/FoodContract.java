package com.fannyarvid.foodorganizer.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by FannyArvid on 2015-04-24.
 */
public class FoodContract {

    public static final String CONTENT_AUTHORITY = "com.fannyarvid.foodorganizer";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_BOX = "box";
    public static final String PATH_INGREDIENT = "ingredient";
    public static final String PATH_FOOD = "link";

    public static long normalizeDate(long startDate) {
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    public static final class FoodEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FOOD).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FOOD;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FOOD;

        public static final String TABLE_NAME = "food";

        public static final String COLUMN_BOX_KEY = "box_id";
        public static final String COLUMN_INGREDIENT_KEY = "ingredient_id";

        public static Uri buildFoodUri (long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getBoxNoFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(0));
        }

        public static Uri buildFoodWithBox(long boxNo) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(boxNo)).build();
        }
    }

    public static final class BoxEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOX).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOX;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOX;

        public static final String TABLE_NAME = "box";

        public static final String COLUMN_BOX_NAME = "food_name";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_STORAGE_TYPE = "storage_type";

        public static final String COLUMN_HAS_BEEN_IN_FREEZER = "has_been_in_freezer";

        public static Uri buildBoxUri (long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildAllBoxUri() {
            return CONTENT_URI;
        }
    }

    public static final class IngredientEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INGREDIENT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INGREDIENT;

        public static final String TABLE_NAME = "ingredient";

        public static final String COLUMN_INGREDIENT_NAME = "ingredient_name";

        public static final String COLUMN_STORAGE_TIME_FRIDGE = "storage_time_fridge";
        public static final String COLUMN_STORAGE_TIME_FREEZER = "storage_time_freezer";

        public static Uri buildIngredientUri (long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildAllIngredientUri() {
            return CONTENT_URI;
        }
    }
}
