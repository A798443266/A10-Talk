package com.eiplan.zuji.model.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eiplan.zuji.bean.RecordInfo;
import com.eiplan.zuji.model.db.SearchRecordDB;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户搜索记录表操作
 */

public class SearchRecordDao {
    private final SearchRecordDB mHelper;

    public SearchRecordDao(Context context) {
        mHelper = new SearchRecordDB(context);//创建数据库
    }

    //添加搜索记录到数据库

    public void addRecord(String phone,int type,String content){//手机号、类型（文档记录还是专家记录）、内容
        // 获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();

        db.execSQL("insert into search_record(phone,type,content) values (?,?,?)",new Object[]{phone,type,content});
        db.close();
    }

    //按照手机号和搜索类型查询记录
    public List<RecordInfo> queryByphone(String phone, int type){
        List<RecordInfo> records = new ArrayList<>();
        // 获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select * from search_record where phone =? and type =?";
        Cursor cursor = db.rawQuery(sql, new String[]{phone,type+""});

        while(cursor.moveToNext()){
            RecordInfo record = new RecordInfo();
            record.setContent(cursor.getString(cursor.getColumnIndex("content")));

            record.setTime(cursor.getString(cursor.getColumnIndex("createtime")));
            records.add(record);
        }

        // 关闭资源
        cursor.close();
        db.close();
        return  records;
    }

    public void deleteOne(String phone, int type, String content){
        // 获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "delete from search_record where phone =? and type =? and content =?";
        db.execSQL(sql,new String[]{phone,type+"",content});
        db.close();
    }


    public void deleteAll(String phone, int type){
        // 获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "delete from search_record where phone =? and type =?";
        db.execSQL(sql,new String[]{phone,type+""});
        db.close();
    }

}
