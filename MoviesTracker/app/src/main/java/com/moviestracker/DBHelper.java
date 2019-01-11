package com.moviestracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "Login3.db", null, 2);
    }

    private static final String USER_TABLE = "users_table";
    private static final String USER_ID = "user_id";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_NAME = "user_name";
    private static final String USER_PASSWORD = "user_password";

    private static final String MOVIE_TABLE = "movies_table";
    private static final String MOVIE_ID = "movie_id";
    private static final String MOVIE_TITLE = "movie_title";
    private static final String MOVIE_ADDED_AT = "movie_added_at";
    private static final String MOVIE_USER = "movie_user";

    private static final String MOVIE_TABLE_CREATE = "create table "
            + MOVIE_TABLE + " ("
            + MOVIE_ID + " integer primary key autoincrement, "
            + MOVIE_TITLE + " text not null, "
            + MOVIE_ADDED_AT + " DATETIME, "
            + MOVIE_USER + " text not null,"
            + " FOREIGN KEY (" + MOVIE_USER + ") REFERENCES " + USER_TABLE + "(" + USER_EMAIL + "));";

    private static final String USER_TABLE_CREATE = "create table "
            + USER_TABLE + " ("
            + USER_ID + " integer primary key autoincrement, "
            + USER_EMAIL + " text not null, "
            + USER_NAME + " text not null, "
            + USER_PASSWORD + " text not null" + ");";

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("create table user(email text primary key, name text, password text)");
        db.execSQL(MOVIE_TABLE_CREATE);
        db.execSQL(USER_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + USER_TABLE);
        db.execSQL("drop table if exists " + MOVIE_TABLE);

        //create new tables
        onCreate(db);
    }

    public int getMoviesSeen(String user_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + MOVIE_TABLE + " WHERE " + MOVIE_USER + " = '" + user_name + "'";
        Cursor data = db.rawQuery(query, null);

        return data.getCount();
    }

    //inserting in db
    public boolean insert(String email, String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_EMAIL, email);
        contentValues.put(USER_NAME, name);
        contentValues.put(USER_PASSWORD, password);

        long ins = db.insert(USER_TABLE, null, contentValues);
        if (ins == -1) return false;
        else return true;
    }

    //checking if mail exists
    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + USER_TABLE + " where " + USER_EMAIL + "=?", new String[]{email});
        if (cursor.getCount() > 0) return false;
        else return true;
    }

    //checking the email and password
    public boolean checkEmailPAssword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + USER_TABLE + " where " + USER_EMAIL + "=? and  " + USER_PASSWORD + "=?", new String[]{email, password});
        if (cursor.getCount() > 0) return true;
        else return false;
    }

    public boolean addMovie(String title, String user_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MOVIE_TITLE, title);
        contentValues.put(MOVIE_ADDED_AT, getDateTime());
        contentValues.put(MOVIE_USER, user_name);
        long ins = db.insert(MOVIE_TABLE, null, contentValues);
        if (ins == -1) return false;
        else return true;
    }

    public Cursor getItemID(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + MOVIE_ID + " FROM " + MOVIE_TABLE +
                " WHERE " + MOVIE_TITLE + " = '" + title + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getData(String user_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + MOVIE_TABLE
                + " WHERE " + MOVIE_USER + " = '" + user_name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void updateMovie(String oldTitle, String newTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + MOVIE_TABLE + " SET " + MOVIE_TITLE +
                " = '" + newTitle + "' WHERE " + MOVIE_TITLE + " = '" + oldTitle + "'";
        db.execSQL(query);
    }

    public void deleteMovie(String title, String user_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + MOVIE_TABLE + " WHERE "
                + MOVIE_USER + " = '" + user_name + "'" +
                " AND " + MOVIE_TITLE + " = '" + title + "'";
        db.execSQL(query);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
