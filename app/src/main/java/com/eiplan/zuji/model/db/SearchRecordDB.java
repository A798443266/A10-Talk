package com.eiplan.zuji.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class SearchRecordDB extends SQLiteOpenHelper {

    //创建数据库
    public SearchRecordDB(@Nullable Context context) {
        super(context, "searchrecord.db", null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //用户搜索记录的表
        //插入的时候默认值为当前时间
        db.execSQL("create table search_record (phone varchar,type int," +
                "content varchar,createtime timestamp NOT NULL DEFAULT  (datetime('now','localtime')));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
