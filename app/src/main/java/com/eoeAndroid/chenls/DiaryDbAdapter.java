package com.eoeAndroid.chenls;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DiaryDbAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_ROOM = "room";
    public static final String KEY_NAME = "name";
    public static final String KEY_DATE = "date";
    public static final String KEY_DAY = "day";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_RENT = "rent";
    public static final String KEY_MONEY = "money";
    public static final String KEY_WATER = "water";
    public static final String KEY_ELECTRIC = "electric";
    public static final String KEY_MARK = "mark";

    private static final String TAG = "DiaryDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_CREATE = "create table diary (_id integer primary key autoincrement" +
            ", room varchar(20) not null" +
            ", name varchar(20) not null" +
            ", date Date DEFAULT NULL" +
            ", day int(2) DEFAULT NULL" +
            ", phone int(11) DEFAULT NULL" +
            ", rent int(11) DEFAULT NULL" +
            ", money int(11) DEFAULT NULL" +
            ", water varchar(70) DEFAULT NULL" +
            ", electric varchar(70) DEFAULT NULL" +
            ", mark text DEFAULT NULL);";

    private static final String DATABASE_NAME = "database";
    private static final String DATABASE_TABLE = "diary";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        //创建表
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        //先删除表  然后创建表
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS diary");
            onCreate(db);
        }
    }

    public DiaryDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public DiaryDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    /**
     * 插入新的数据
     *
     * @param room
     * @param name
     * @return
     */

    public long createDiary(String room, String name, String date, String phone, String money,
                            String rent, String water, String electric, String mark) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROOM, room);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_DATE, date);
        String[] day_array=date.split("-");
        String day = day_array[2];
        initialValues.put(KEY_DAY, day);
        initialValues.put(KEY_PHONE, phone);
        initialValues.put(KEY_MONEY, money);
        initialValues.put(KEY_RENT, rent);
        initialValues.put(KEY_WATER, water);
        initialValues.put(KEY_ELECTRIC, electric);
        initialValues.put(KEY_MARK, mark);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * 通过id删除记录
     */
    public boolean deleteDiary(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * 获取所有的记录
     */
    public Cursor getAllNotes(String data) {
        if (data.equals("room")) {
            return mDb.rawQuery("select * from diary order by room ASC", null);
        } else {
            return mDb.rawQuery("SELECT * FROM `diary` ORDER BY (case when day<strftime('%d', 'now','+8 hour') " +
                    "then day+40 else day end);", null);
        }
    }

    public Cursor getDiary(String search_context) throws SQLException {
        String sql = "select * from " +
                DATABASE_TABLE + " where " +
                KEY_ROWID + " like ? or " +
                KEY_ROOM + " like ? or " +
                KEY_NAME + " like ? or " +
                KEY_DATE + " like ?";
        String[] selectionArgs = new String[]{"%" + search_context + "%",
                "%" + search_context + "%",
                "%" + search_context + "%",
                "%" + search_context + "%"};
        Cursor mCursor = mDb.rawQuery(sql, selectionArgs);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateDiary(String rowId, String title, String date) {
        ContentValues args = new ContentValues();
        args.put(title, date);
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
