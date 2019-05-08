package com.eiplan.zuji.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.eiplan.zuji.model.dao.UserAccountTable;

public class UserAccountDB extends SQLiteOpenHelper {
    //创建数据库
    public UserAccountDB(@Nullable Context context) {
        super(context, "account.db", null,1);
    }

    // 数据库创建的时候调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建数据库用户表的语句
        db.execSQL(UserAccountTable.CREATE_TAB);
    }

    // 数据库更新的时候调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
