package com.android.ronakdoongarwal.moviesandshows;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FavoriteShowDBHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "favorite_show_db";
    private static final String TABLE_NAME = "show_table";
    //column names
    private static final String ID = "id";
    private static final String SHOW_NAME = "show_name";
    public static final String USER_RATING = "user_rating";
    public static final String RELEASE_DATE = "release_date";
    public static final String VOTE_COUNT = "vote_count";
    public static final String POSTER_PATH = "poster_path";
    public static final String BACKDROP_PATH = "backdrop_path";
    public static final String OVERVIEW = "overview";

    public FavoriteShowDBHelper(Context context) {
        super(context, DATABASE_NAME, null  , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOVIE_TABLE = "CREATE TABLE " +
                TABLE_NAME+
                "("+ID+" INTEGER PRIMARY KEY,"+
                SHOW_NAME+" TEXT,"+
                USER_RATING+" DOUBLE,"+
                RELEASE_DATE+" TEXT,"+
                VOTE_COUNT+" TEXT,"+
                POSTER_PATH+" TEXT,"+
                BACKDROP_PATH+" TEXT,"+
                OVERVIEW+" TEXT)";
        Log.d("tablestatement", CREATE_MOVIE_TABLE);
        db.execSQL(CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public void addShowToFavorite(TVShowParcel parcel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valuePairs = new ContentValues();
        valuePairs.put(ID,parcel.getShowId());
        valuePairs.put(SHOW_NAME,parcel.getTVShowTitle());
        valuePairs.put(USER_RATING,parcel.getUserRating());
        valuePairs.put(RELEASE_DATE, parcel.getReleaseDate());
        valuePairs.put(VOTE_COUNT, parcel.getVoteCount());
        valuePairs.put(POSTER_PATH, parcel.getImagePosterURL());
        valuePairs.put(BACKDROP_PATH, parcel.getBackdropURL());
        valuePairs.put(OVERVIEW, parcel.getOverview());
        db.insert(TABLE_NAME,null, valuePairs);
        db.close();
    }

    public List<TVShowParcel> getFavoriteShowList() {
        List<TVShowParcel> list =new ArrayList<>();
        String SELECT_TABLE_DATA = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_TABLE_DATA, null);
        if(cursor.moveToFirst()){
            do{
                TVShowParcel showParcel = new TVShowParcel(cursor.getString(1),
                        cursor.getString(7),
                        cursor.getString(3),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getDouble(2),
                        cursor.getString(4),
                        cursor.getInt(0));
                Log.d("favorit", cursor.getString(1));
                list.add(showParcel);
            }while (cursor.moveToNext());
        }
    return list;
    }
}
