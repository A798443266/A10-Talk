package com.eiplan.zuji.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eiplan.zuji.bean.UserInfo;
import com.eiplan.zuji.model.db.UserAccountDB;


// 用户账号数据库的操作类
public class UserAccountDao {
    private final UserAccountDB mHelper;

    public UserAccountDao(Context context) {
        mHelper = new UserAccountDB(context);//创建数据库
    }

    // 添加用户到数据库
    public void addAccount(UserInfo user) {
        // 获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();
        // 执行添加操作
        ContentValues values = new ContentValues();
        values.put(UserAccountTable.COL_PHONE, user.getPhone());
        values.put(UserAccountTable.COL_NAME, user.getName());
        values.put(UserAccountTable.COL_PIC, user.getPic());
        values.put(UserAccountTable.COL_COMPANY, user.getCompany());
        values.put(UserAccountTable.COL_INDUSTRY, user.getIndustry());
        values.put(UserAccountTable.COL_BALANCE, user.getBalance());
        values.put(UserAccountTable.COL_BANKCARD, user.getBankcard());
        values.put(UserAccountTable.COL_POINT, user.getPoint());
        values.put(UserAccountTable.COL_ISEXPERT, user.getIsExpert());

        db.replace(UserAccountTable.TAB_NAME, null, values);//用替代如果之前有用户名就不用再插入了，替换就可以
    }

    // 根据手机号获取所有用户信息
    public UserInfo getAccountByPhone(String phone) {
        // 获取数据库对象
        SQLiteDatabase db = mHelper.getReadableDatabase();
        // 执行查询语句
        String sql = "select * from " + UserAccountTable.TAB_NAME + " where " + UserAccountTable.COL_PHONE + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{phone});
        UserInfo userInfo = null;
        if(cursor.moveToNext()) {
            userInfo = new UserInfo();
            // 封装对象
            userInfo.setPhone(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_PHONE)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_NAME)));
            userInfo.setPic(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_PIC)));
            userInfo.setCompany(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_COMPANY)));
            userInfo.setCompany(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_INDUSTRY)));
            userInfo.setBalance(cursor.getInt(cursor.getColumnIndex(UserAccountTable.COL_BALANCE)));
            userInfo.setBankcard(cursor.getInt(cursor.getColumnIndex(UserAccountTable.COL_BANKCARD)));
            userInfo.setPoint(cursor.getInt(cursor.getColumnIndex(UserAccountTable.COL_POINT)));
            userInfo.setIsExpert(cursor.getInt(cursor.getColumnIndex(UserAccountTable.COL_ISEXPERT)));
        }
        // 关闭资源
        cursor.close();
        db.close();
        // 返回数据
        return  userInfo;
    }
}
