package com.eiplan.zuji.utils;

/**
 * 常量类
 */

public class Constant {

    //备用ip  172.20.10.3
//    192.168.0.146

    public static String BASE = "http://luo.easy.echosite.cn/A13/";

    public static String REGISTER = BASE + "add";//用户注册url
    public static String REGISTER2 = BASE + "add2";//第三方注册url

    public static String DOCSEARCH = BASE + "filesearch";//文档搜索url
    public static String SEARCH_EXPERT = BASE + "expsearch";//专家搜索url
    public static String recommendFile = BASE + "recommendfile";//文档推荐url
    public static String recommendExperts = BASE + "recommendExp";//专家推荐url
    public static String getAllExperts = BASE + "getexps";//获取全部专家url

    public static String LOGIN = BASE + "login";//登录
    public static String expByEva = BASE + "expByEva";//服务评价排行
    public static String expByLook = BASE + "expByLook";//浏览次数排行
    public static String startLive = BASE + "live";//直播
    public static String seeLive = BASE + "seelive";//搜索直播
    public static String getVideos = BASE + "getvedios";//获取视频信息
    public static String getVideoCommnet = BASE + "vedioeva";//获取视频评论
    public static String updateVideo = BASE + "vupload";//上传视频
    public static String updatePrombleWithoutPic = BASE + "upForum";//发布问题（无图片）
    public static String updatePromble = BASE + "upForumPic";//发布问题（有图片）
    public static String Prombles = BASE + "forums";//获取全部问题
    public static String my_release_Prombles = BASE + "getForumByPhone";//获取我发布的问题
    public static String my_answer_Prombles = BASE + "getForumevaByPhone";//获取我回答的问题
    public static String getPrombleComments = BASE + "getForumById";//获取问题的评论
    public static String gotoAnswer = BASE + "getForumByMajor";//去回答问题（返回的json是根据行业推荐的）
    public static String upFile = BASE + "upFile";//上传文档
    public static String myReleaseDocs = BASE + "myfiles";//我上传的文档
    public static String myCollectfiles = BASE + "myCollectfiles";//我收藏的文档
    public static String myBuyfiles = BASE + "myBuyfiles";//我购买的文档
    public static String getMyUpVedios = BASE + "getMyVedios";//我上传的视频
    public static String becomeExpert = BASE + "uprz";//认证专家
    public static String getExpertComments = BASE + "getEva";//获取专家评价信息
    public static String getOrdering = BASE + "ordering";//正在进行的专家接口
    public static String getOrderWaitEva = BASE + "orderWaitEva";//等待评价的专家接口
    public static String getOrderEnd = BASE + "orderEnd";//已完成的专家接口
    public static String payAndwancheng = BASE + "orderCaozuo"; //支付和完成操作接口
    public static String orderEva = BASE + "orderEva"; //评价操作接口
    public static String customerYY = BASE + "orderYY"; //专家端客户预约模块
    public static String customerFS = BASE + "orderFS"; //专家端发送订单模块
    public static String customerZF = BASE + "orderZF"; //专家端支付状态模块

    public static String Agree = BASE + "orderAction"; //专家同意的操作
    public static String sendDingdan = BASE + "uporder"; //专家同意的操作
    public static String getCollectExpers = BASE + "myCollectExps"; //我收藏的专家
    public static String cancelCollectExpers = BASE + "cancelCollectExp"; //取消收藏专家
    public static String CollectExp = BASE + "CollectExp"; //收藏专家
    public static String cancelCollectfile = BASE + "cancelCollectfile"; //取消收藏文档
    public static String Collectfile = BASE + "Collectfile"; //收藏文档
    public static String buyfile = BASE + "buyfile"; //购买文档

    public static String contactSearch = BASE + "getyhByPhone"; //按手机号搜索添加好友
    public static String contactSearchByMajor = BASE + "getYhByMajor"; //按关键词搜索添加好友
    public static String getAllContact = BASE + "getyhs"; //获取所有用户头像名称信息
    public static String yuyue = BASE + "yuyue";//预约
    public static String getPoints = BASE + "getPoints";//获取积分明细
    public static String adoptAnswer = BASE + "adoptAnswer";//采纳回答
    public static String recommends = BASE + "recommends";//推荐讯息
    public static String getSystemNews = BASE + "getNews";//系统消息
    public static String getGroupById = BASE + "getGroupById";//根据群id搜索群
    public static String getGroupByMajor = BASE + "getGroupByMh";//根据关键词搜索群
    public static String getDemands = BASE + "demandsWithFind";//获取全部需求(寻找中)
    public static String getDemandsFinish = BASE + "demandsWithFinish";//获取全部需求(已完成)
    public static String upDemand = BASE + "updemand";//发布需求
    public static String getMyDemands1 = BASE + "getDByPhoneWithFind"; //获得我发布的需求（进行中）
    public static String getMyDemands2 = BASE + "getDByPhoneWithFinish"; //获得我发布的需求（完成）
    public static String getlives = BASE + "getlives"; //获取直播间
    public static String getstudys = BASE + "getstudys"; //获取课程
    public static String getTradeByPhone = BASE + "getTradeByPhone"; //获取交易记录
    public static String getStudyCommends = BASE + "getVedioEvas"; //获取课程评论
    public static String upAnswer = BASE + "upAnswer"; //发布问题评论


    public static String SP_NAME = "SP_EIPLAN";//sp名字
    public static String Recommend_experts = "Recommend_experts";  //推荐专家存入的json数据
    public static String Recommend_docs = "recommend_docs";//推荐文档存入的json数据
    public static String Rank_Eva = "Rank_Eva";//专家评分排行存入的json数据
    public static String Rank_Look = "Rank_Look";//专家浏览次数排行存入的json数据
    public static String Videos = "Videoos";//全部视频json
    public static final String All_Experts = "All_Experts";//全部专家json
    public static String save_problems = "save_problems";//所有问题信息存入的json数据
    public static String recommendForme_problems = "recommendForme_problems";//回答问题信息存入的json数据
    public static String my_save_problems = "my_save_problems";//我发布的问题存入的json数据
    public static String my_save_answer_problems = "my_save_answer_problems";//我回答的问题存入的json数据
    public static String my_buy_docs = "my_buy_docs"; //我购买的文档存入的json数据
    public static String my_collect_docs = "my_collect_docs";//我收藏的文档存入的json数据
    public static String my_release_docs = "my_release_docs";//我发布的文档存入的json数据
    public static String my_up_videos = "my_up_videos"; //我上传的视频存入的json数据

    public static String ordering = "ordering";//正在进行中专家的json数据
    public static String orderEnd = "orderEnd";//已经完成专家的json数据
    public static String orderWaitEva = "orderWaitEva";//待评价专家的json数据
    public static String save_collect_experts = "save_collect_experts";//我收藏专家的json数据
    public static String save_my_points = "save_my_points";//我的积分的json数据
    public static String save_recommend_msgs = "recommend_msgs";//推荐讯息的json数据
    public static String save_system_news = "save_system_news";//系统消息的json数据
    public static String save_demans = "save_demans"; //需求进行中的json
    public static String save_demans1 = "save_demans1";//需求完成的json
    public static String save_my_demand1 = "save_my_demand1";//获得我发布的需求json（进行中）
    public static String save_my_demand2 = "save_my_demand2";//获得我发布的需求json（已完成）
    public static String save_study = "save_study";//获得全部课程信息json


    public static String current_phone = "current_phone";   //当前登录的手机号
    public static String current_isexpert = "current_isexpert"; //当前登录的是不是专家
    public static String current_pic = "current_pic";  //当前登录用户的头像地址，用于聊天显示
    public static String current_major = "current_major";//如果是专家，记录行业
    public static String current_industry = "current_industry"; //如果是用户，记录行业
    public static String current_job = "current_job";  //如果是专家，记录职业
    public static String current_point = "current_point";//当前登录的用户的积分
    public static String current_workyear = "current_workyear"; //如果是专家，记录工作经验
    public static String current_name = "current_name"; //当前登录的名称
    public static String current_company = "current_company";


    public static String GUIDE = "guide";//判断是否要进入引导页面（如果第一次进入就为1，否则为0）
    public static String INDUSTRY = "industy";


    public static final String GROUP_ID = "group_id";// 群id
    public static final String EXIT_GROUP = "exit_group";// 退群广播
    public static final String CONTACT_CHANGED = "contact_changed";// 发送联系人变化的广播
    public static final String CONTACT_INVITE_CHANGED = "contact_invite_changed";// 联系人邀请信息变化的广播
    public static final String GROUP_INVITE_CHANGED = "group_invite_changed";// 群邀请信息变化的广播
    public static final String IS_NEW_INVITE = "is_new_invite";// 新的邀请标记

    public static String expertPublishFiles = BASE + "myPublishFiles";//专家页面已发布的文档
}
