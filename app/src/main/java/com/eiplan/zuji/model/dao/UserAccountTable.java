package com.eiplan.zuji.model.dao;

//用户表的字段和建表sql语句
public class UserAccountTable {
    public static final String TAB_NAME = "tab_account"; //用户表名
    public static final String COL_PHONE = "phone";
    public static final String COL_NAME = "name";
    public static final String COL_PIC = "pic";
    public static final String COL_COMPANY = "company";
    public static final String COL_INDUSTRY = "industry";
    public static final String COL_BALANCE = "balance";
    public static final String COL_BANKCARD = "bankcard";
    public static final String COL_POINT = "point";
    public static final String COL_ISEXPERT = "isExpert"; //是否是专家，1是，0不是

    public static final String CREATE_TAB = "create table "
            + TAB_NAME + " ("
            + COL_PHONE + " varchar primary key,"
            + COL_NAME + " varchar,"
            + COL_PIC + " varchar,"
            + COL_COMPANY + " varchar,"
            + COL_INDUSTRY + " varchar,"
            + COL_BALANCE + " int,"
            + COL_BANKCARD + " int,"
            + COL_POINT + " int,"
            + COL_ISEXPERT + " int);";


//    create table ( hxid text primary key, name text, nick text, photo text);
}
