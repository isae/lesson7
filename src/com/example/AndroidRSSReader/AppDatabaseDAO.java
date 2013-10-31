package com.example.AndroidRSSReader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Xottab
 * Date: 31.10.13
 * Time: 12:15
 * To change this template use File | Settings | File Templates.
 */
public class AppDatabaseDAO {


    private static final String KEY_TITLE = "title";
    private static final String KEY_BODY = "body";
    private static final String KEY_CHANNEL_ID = "_channelId";
    private static final String KEY_URL = "url";
    private static final String KEY_ID = "_id";

    private static final String TAG = "NotesDbAdapter";
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    private static final String FEEDS_TABLE_CREATE =
            "create table if not exists " +
                    "feeds (_id integer primary key autoincrement, " +
                    "_channelId integer, "
                    + "title text not null, " +
                    "body text not null);";
    private static final String CHANNELS_TABLE_CREATE = "create table if not exists channels " +
            "(_channelId integer primary key autoincrement, " +
            "title text not null, " +
            "url text not null);";

    private static final String DATABASE_NAME = "mydb";
    private static final String TABLE_CHANNELS = "channels";
    private static final String TABLE_FEEDS = "feeds";
    private static final int DATABASE_VERSION = 2;

    private final Context context;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(FEEDS_TABLE_CREATE);
            db.execSQL(CHANNELS_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS feeds");
            db.execSQL("DROP TABLE IF EXISTS channels");
            onCreate(db);
        }
    }


    public AppDatabaseDAO(Context context) {
        this.context = context;
    }


    private AppDatabaseDAO open() throws SQLException {
        helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
        return this;
    }

    private void close() {
        helper.close();
    }


    public long addChannel(Channel channel) throws SQLException {
        try {
            this.open();
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_TITLE, channel.title);
            initialValues.put(KEY_URL, channel.url);
            long result = db.insert(TABLE_CHANNELS, null, initialValues);
            this.close();
            return result;
        } finally {
            this.close();
        }

    }

    public boolean deleteChannel(int channelId) throws SQLException {
        try {
            this.open();
            boolean result = db.delete(TABLE_CHANNELS, KEY_CHANNEL_ID + "=" + channelId, null) > 0;

            this.close();
            return result;
        } finally {
            this.close();
        }
    }

    public int editChannel(Channel channel) throws SQLException {
        try {
            this.open();
            ContentValues values = new ContentValues();
            values.put(KEY_TITLE, channel.title);
            values.put(KEY_URL, channel.url);
            return db.update(TABLE_CHANNELS, values, KEY_CHANNEL_ID + "=" + channel.dbId, null);
        } finally {
            this.close();
        }
    }


    private Cursor fetchAllChannels() {
        return db.query(TABLE_CHANNELS, new String[]{KEY_CHANNEL_ID, KEY_TITLE,
                KEY_URL}, null, null, null, null, null);
    }

    private Cursor fetchAllFeeds(Integer channelId) {
        return db.query(TABLE_FEEDS, new String[]{KEY_TITLE,
                KEY_BODY}, KEY_CHANNEL_ID + "=" + channelId, null, null, null, null);
    }

    public ArrayList<Channel> getAllChannels() throws SQLException {
        this.open();
        Cursor cursor = fetchAllChannels();
        ArrayList<Channel> result = new ArrayList<>();
        int i1 = cursor.getColumnIndex(KEY_CHANNEL_ID);
        int i2 = cursor.getColumnIndex(KEY_TITLE);
        int i3 = cursor.getColumnIndex(KEY_URL);
        while (cursor.moveToNext()) {
            result.add(new Channel(cursor.getInt(i1), cursor.getString(i2), cursor.getString(i3)));
        }
        this.close();
        return result;
    }

    public ArrayList<Feed> getAllFeeds(Integer channelId) throws SQLException {
        ArrayList<Feed> result = new ArrayList<>();
        this.open();
        Cursor cursor = fetchAllFeeds(channelId);
        int i1 = cursor.getColumnIndex(KEY_TITLE);
        int i2 = cursor.getColumnIndex(KEY_BODY);
        while (cursor.moveToNext()) {
            result.add(new Feed(cursor.getString(i1), cursor.getString(i2)));
        }
        this.close();
        return result;
    }

    public boolean deleteAllFeeds(Integer channelId) throws SQLException {
        this.open();
        boolean result = db.delete(TABLE_FEEDS, KEY_CHANNEL_ID + "=" + channelId, null) > 0;
        this.close();
        return result;
    }


    private Cursor fetchNote(long rowId) throws SQLException {

        Cursor mCursor =

                db.query(true, TABLE_FEEDS, new String[]{KEY_ID,
                        KEY_TITLE, KEY_BODY}, KEY_ID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    private boolean updateNote(long rowId, String title, String body) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_BODY, body);

        return db.update(TABLE_FEEDS, args, KEY_ID + "=" + rowId, null) > 0;
    }
}