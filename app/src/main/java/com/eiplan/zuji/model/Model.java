package com.eiplan.zuji.model;

import android.content.Context;

import com.eiplan.zuji.bean.UserInfo;
import com.eiplan.zuji.model.dao.SearchRecordDao;
import com.eiplan.zuji.model.dao.UserAccountDao;
import com.eiplan.zuji.model.db.DBManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 数据模型层全局类
 */

public class Model {
    private Context mContext;
    private ExecutorService executors = Executors.newCachedThreadPool();//线程池
    private UserAccountDao userAccountDao;//用户数据库操作对象
    private SearchRecordDao searchRecordDao;//搜索记录操作数据库操作对象
    private DBManager dbManager;
    // 创建对象
    private static Model model = new Model();
    // 私有化构造
    private Model() {}

    // 获取单例对象
    public static Model getInstance(){
        return model;
    }

    // 初始化的方法
    public void init(Context context){
        mContext = context;
        userAccountDao = new UserAccountDao(mContext);
        searchRecordDao = new SearchRecordDao(mContext);

        // 开启全局监听
        EventListener eventListener = new EventListener(mContext);
    }

    // 获取全局线程池对象
    public ExecutorService getGlobalThreadPool(){
        return executors;
    }

    // 用户登录成功后的处理方法
    public void loginSuccess(UserInfo account) {
        // 校验
        if(account == null) {
            return;
        }
        if(dbManager != null) {
            dbManager.close();
        }
        dbManager = new DBManager(mContext, account.getPhone());
    }

    public DBManager getDbManager(){
        return dbManager;
    }

    public  UserAccountDao getUserAccountDao(){
        return userAccountDao;
    }
    public  SearchRecordDao getSearchRecordDao(){
        return searchRecordDao;
    }
}
