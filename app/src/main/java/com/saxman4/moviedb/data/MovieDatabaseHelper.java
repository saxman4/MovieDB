package com.saxman4.moviedb.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.saxman4.moviedb.data.DatabaseDescription.Movies;

class MovieDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Movie.db";
    private static final int DATABASE_VERSION = 1;

    // constructor
    public MovieDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // creates the movie table when the database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL for creating the movie table
        final String CREATE_MOVIE_TABLE =
                "CREATE TABLE " + Movies.TABLE_NAME + "(" +
                        Movies._ID + " integer primary key, " +
                        Movies.COLUMN_TITLE + " TEXT, " +
                        Movies.COLUMN_YEAR + " TEXT, " +
                        Movies.COLUMN_ACTORS + " TEXT, " +
                        Movies.COLUMN_ACTRESSES + " TEXT, " +
                        Movies.COLUMN_SUMMARY + " TEXT, " +
                        Movies.COLUMN_DIRECTOR + " TEXT, " +
                        Movies.COLUMN_AWARDS + " TEXT, " +
                        Movies.COLUMN_SONGS + " TEXT);";
        db.execSQL(CREATE_MOVIE_TABLE); // create the movie table
    }

    // normally defines how to upgrade the database when the schema changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) { }
}
