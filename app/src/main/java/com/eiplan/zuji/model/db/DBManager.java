package com.eiplan.zuji.model.db;

import android.content.Context;

import com.eiplan.zuji.model.dao.ContactTableDao;
import com.eiplan.zuji.model.dao.InviteTableDao;


// 联系人和邀请信息表的操作类的管理类
public class DBManager {

    private final DBHelper dbHelper;
    private final ContactTableDao contactTableDao;
    private final InviteTableDao inviteTableDao;

    public DBManager(Context context, String phone) {
        // 创建数据库
        dbHelper = new DBHelper(context, phone);

        // 创建该数据库中两张表的操作类
        contactTableDao = new ContactTableDao(dbHelper);
        inviteTableDao = new InviteTableDao(dbHelper);
    }

    // 获取联系人表的操作类对象
    public  ContactTableDao getContactTableDao(){
        return contactTableDao;
    }
    // 获取邀请信息表的操作类对象
    public InviteTableDao getInviteTableDao(){
        return inviteTableDao;
    }

    // 关闭数据库的方法
    public void close() {
        dbHelper.close();
    }
}
