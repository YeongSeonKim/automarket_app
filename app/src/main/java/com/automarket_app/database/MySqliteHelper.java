package com.automarket_app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySqliteHelper extends SQLiteOpenHelper {
    // SQLiteOpenHelper를 상속받아서 사용 => 오버라이딩(재정의)해야함

    // 생성자도 다시 작성해 줘야한다!! 기본생성자가 아닌 인자를 가지는 생성자여만한다.
    public MySqliteHelper(Context context){
        // 상위 클래스의 생성자 호출(인자 4개짜리 생성자 호출)
        super(context,"Cart.db",null,1); // 인자 4개 생성자호출
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 일반적으로 초기 데이터베이스 세팅코드가 들어간다.
        // 테이블을 생성하고 초기데이터를 insert하는 작업.
        // execSQL() : resultset을 가져오지 않는 SQL구문을 실행.
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS cart (prodId TEXT,prodCnt INTEGER, prodName Text, prodPrice INTEGER,imgpath TEXT);");

        Log.i("DatabaseExam", "Helper의 onCreate() 호출!!");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
