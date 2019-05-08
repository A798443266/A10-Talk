package com.eiplan.zuji.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.eiplan.zuji.bean.ContactInfo;
import com.eiplan.zuji.bean.UserInfo;
import com.eiplan.zuji.model.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

// 联系人表的操作类
public class ContactTableDao {
    private DBHelper mHelper;

    public ContactTableDao(DBHelper helper) {
        mHelper = helper;
    }

    // 获取所有联系人
    public List<ContactInfo> getContacts() {
        // 获取数据库链接
        SQLiteDatabase db = mHelper.getReadableDatabase();
        // 执行查询语句
        String sql = "select * from " + ContactTable.TAB_NAME + " where " + ContactTable.COL_IS_CONTACT + "=1";
        Cursor cursor = db.rawQuery(sql, null);
        List<ContactInfo> contactInfos = new ArrayList<>();
        while (cursor.moveToNext()) {
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setPhone(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
            contactInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
            contactInfo.setPic(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));
            contactInfos.add(contactInfo);
        }
        // 关闭资源
        cursor.close();
        // 返回数据
        return contactInfos;
    }

    // 通过环信id获取联系人单个信息
    public ContactInfo getContactByHx(String hxId) {
        if (hxId == null) {
            return null;
        }
        // 获取数据库链接
        SQLiteDatabase db = mHelper.getReadableDatabase();
        // 执行查询语句
        String sql = "select * from " + ContactTable.TAB_NAME + " where " + ContactTable.COL_HXID + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{hxId});
        ContactInfo userInfo = null;
        if (cursor.moveToNext()) {
            userInfo = new ContactInfo();

            userInfo.setPhone(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
            userInfo.setPic(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));
        }
        // 关闭资源
        cursor.close();
        // 返回数据
        return userInfo;
    }

    // 通过环信id获取用户联系人信息
    public List<ContactInfo> getContactsByHx(List<String> hxIds) {
        if (hxIds == null || hxIds.size() <= 0) {
            return null;
        }
        List<ContactInfo> contacts = new ArrayList<>();
        // 遍历hxIds，来查找
        for (String hxid : hxIds) {
            ContactInfo contact = getContactByHx(hxid);
            contacts.add(contact);
        }
        // 返回查询的数据
        return contacts;
    }

    // 保存单个联系人
    public void saveContact(ContactInfo user, boolean isMyContact) {
        if (user == null) {
            return;
        }
        // 获取数据库链接
        SQLiteDatabase db = mHelper.getReadableDatabase();
        // 执行保存语句
        ContentValues values = new ContentValues();
        values.put(ContactTable.COL_HXID, user.getPhone());
        values.put(ContactTable.COL_NAME, user.getName());
        values.put(ContactTable.COL_PHOTO, user.getPic());
        values.put(ContactTable.COL_IS_CONTACT, isMyContact ? 1 : 0);
        ContactInfo contactInfo;

        contactInfo = getContactByHx(user.getPhone());
        Log.e("TAG", user.getPhone());
        /*if (contactInfo != null) {//如果数据库中不存在才插入
            db.insert(ContactTable.TAB_NAME, null, values);
            Log.e("TAG", "插入成功");
        } else {
            Log.e("TAG", "插入失败");
        }*/

        db.replace(ContactTable.TAB_NAME, null, values);
    }


    // 保存联系人信息
    public void saveContacts(List<ContactInfo> contacts, boolean isMyContact) {
        if (contacts == null || contacts.size() <= 0) {
            return;
        }
        for (ContactInfo contact : contacts) {
            saveContact(contact, isMyContact);
        }
    }

    // 删除联系人信息
    public void deleteContactByHxId(String hxId) {
        if (hxId == null) {
            return;
        }

        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.delete(ContactTable.TAB_NAME, ContactTable.COL_HXID + "=?", new String[]{hxId});
    }
}
