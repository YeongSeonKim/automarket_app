package com.automarket_app.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class MyContentProvider extends ContentProvider {

    // 필드지정
    private SQLiteDatabase db;

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        MySqliteHelper helper = new MySqliteHelper(getContext());
        db = helper.getWritableDatabase();
        Log.i("DatabaseExam", "CP(ContentProvider)에 onCreate() 호출!!");
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // select 계열의 SQL문이 실행되고 그결과를 Cursor를 다른 App에 제공.
        Log.i("DatabaseExam", "CP(ContentProvider)에 query() 호출 되었어요!!");
        // 인자로 들어오는 값을 이용하여 사용할 SQL을 구성해야 한다.!!
        String sql = "SELECT * FROM cart";
        return db.rawQuery(sql,null);

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
