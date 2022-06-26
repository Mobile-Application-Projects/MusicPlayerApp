package com.example.soc_macmini_15.musicplayer.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.soc_macmini_15.musicplayer.Model.EbooksList;

import java.util.ArrayList;

public class FavoritesOperations
{

    public static final String TAG = "Favorites2 Database";

    SQLiteOpenHelper dbHandler;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            FavoritesDBHandler.COLUMN_ID,
            FavoritesDBHandler.COLUMN_TITLE,
            FavoritesDBHandler.COLUMN_SUBTITLE,
            FavoritesDBHandler.COLUMN_PATH
    };

    public FavoritesOperations(Context context) {
        dbHandler = new FavoritesDBHandler(context);
    }

    public void open() {
        Log.i(TAG, " Database Opened");
        database = dbHandler.getWritableDatabase();
    }

    public void close() {
        Log.i(TAG, "Database Closed");
        dbHandler.close();
    }

    public void addEbookFav(EbooksList ebooksList) {
        open();
        ContentValues values = new ContentValues();
        values.put(FavoritesDBHandler.COLUMN_TITLE, ebooksList.getTitle());
        values.put(FavoritesDBHandler.COLUMN_SUBTITLE, ebooksList.getSubTitle());
        values.put(FavoritesDBHandler.COLUMN_PATH, ebooksList.getPath());

        database.insertWithOnConflict(FavoritesDBHandler.TABLE_EBOOKS, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        close();
    }

    public ArrayList<EbooksList> getAllFavorites() {
        open();
        Cursor cursor = database.query(FavoritesDBHandler.TABLE_EBOOKS, allColumns,
                null, null, null, null, null);
        ArrayList<EbooksList> favEbooks = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                EbooksList ebooksList = new EbooksList(cursor.getString(cursor.getColumnIndex(FavoritesDBHandler.COLUMN_TITLE))
                        , cursor.getString(cursor.getColumnIndex(FavoritesDBHandler.COLUMN_SUBTITLE))
                        , cursor.getString(cursor.getColumnIndex(FavoritesDBHandler.COLUMN_PATH)));
                favEbooks.add(ebooksList);
            }
        }
        close();
        return favEbooks;
    }

    public void removeEbook(String ebookPath)
    {
        open();
        String whereClause = FavoritesDBHandler.COLUMN_PATH + "=?";
        String[] whereArgs = new String[]{ebookPath};

        database.delete(FavoritesDBHandler.TABLE_EBOOKS, whereClause, whereArgs);
        close();
    }

}






















