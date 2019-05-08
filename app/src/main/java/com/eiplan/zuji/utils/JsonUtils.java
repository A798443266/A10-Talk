package com.eiplan.zuji.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eiplan.zuji.activity.ReleaseDocActivity;
import com.eiplan.zuji.bean.CommentInfo;
import com.eiplan.zuji.bean.ContactInfo;
import com.eiplan.zuji.bean.DemandInfo;
import com.eiplan.zuji.bean.DocInfo;
import com.eiplan.zuji.bean.ExpertComment;
import com.eiplan.zuji.bean.ExpertInfo;
import com.eiplan.zuji.bean.LiveRoom;
import com.eiplan.zuji.bean.MyAnswerProblem;
import com.eiplan.zuji.bean.MyCustomer;
import com.eiplan.zuji.bean.MyExpertInfo;
import com.eiplan.zuji.bean.MyReleaseProblem;
import com.eiplan.zuji.bean.PointInfo;
import com.eiplan.zuji.bean.ProblemCommentInfo;
import com.eiplan.zuji.bean.ProblemInfo;
import com.eiplan.zuji.bean.ReLiveRoom;
import com.eiplan.zuji.bean.RecommendMSG;
import com.eiplan.zuji.bean.ReleaseDocInfo;
import com.eiplan.zuji.bean.SearchGroupInfo;
import com.eiplan.zuji.bean.StudyCommendInfo;
import com.eiplan.zuji.bean.StudyInfo;
import com.eiplan.zuji.bean.SystemNotificationInfo;
import com.eiplan.zuji.bean.TradeInfo;
import com.eiplan.zuji.bean.VideoInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析json数据类
 */

public class JsonUtils {

    //解析查询到专家的json数据(包括行业和名称查询)
    public static List<ExpertInfo> parseExperts(String json) {
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(json)
                .getJSONObject("extend");

        String names = jsonObject.getString("name");
        String majors = jsonObject.getString("major");

        List<ExpertInfo> experts1 = JSON.parseArray(names, ExpertInfo.class);//按名称搜索到的专家数组
        List<ExpertInfo> experts2 = JSON.parseArray(majors, ExpertInfo.class);//按行业搜索到的专家数组
        List<ExpertInfo> experts = new ArrayList<>();

        if (experts1 != null && experts1.size() > 0) {
            experts.addAll(experts1);
        }
        if (experts2 != null && experts2.size() > 0) {
            experts.addAll(experts2);
        }

        return experts;//返回搜索到的所有的专家
    }

    //解析全部专家信息
    public static List<ExpertInfo> parseAllExperts(String json) {
        JSONObject jsonObject = JSON.parseObject(json)
                .getJSONObject("extend");

        String s = jsonObject.getString("exps");
        return JSON.parseArray(s, ExpertInfo.class);
    }


    //解析查询到文档的json数据(包括行业和名称查询)
    public static List<DocInfo> parseDocs(String json) {
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(json)
                .getJSONObject("extend");

        String names = jsonObject.getString("name");
        String majors = jsonObject.getString("major");

        List<DocInfo> docs1 = JSON.parseArray(names, DocInfo.class);//按名称搜索到的文档数组
        List<DocInfo> docs2 = JSON.parseArray(majors, DocInfo.class);//按行业搜索到的文档数组
        List<DocInfo> docs = new ArrayList<>();

        if (docs1 != null && docs1.size() > 0) {
            docs.addAll(docs1);
        }
        if (docs2 != null && docs2.size() > 0) {
            docs.addAll(docs2);
        }

        return docs;//返回搜索到的所有的文档
    }

    //解析行业查询到专家的json数据（用于首页推荐专家）
    public static List<ExpertInfo> parseExpertsByMajor(String json) {
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(json)
                .getJSONObject("extend");

        String majors = jsonObject.getString("exps");

        List<ExpertInfo> experts = JSON.parseArray(majors, ExpertInfo.class);//按行业搜索到的专家数组
        return experts;//返回搜索到的所有的专家
    }

    //解析行业查询到文档的json数据（用于首页推荐文档）
    public static List<DocInfo> parseDocsByMajor(String json) {
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(json)
                .getJSONObject("extend");

        String majors = jsonObject.getString("files");

        List<DocInfo> docInfos = JSON.parseArray(majors, DocInfo.class);//按行业搜索到的专家数组

        return docInfos;//返回搜索到的所有的专家
    }

    //解析服务评价排行的专家数据
    public static List<ExpertInfo> parseExpertsByEva(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");

        String s = jsonObject.getString("expByEva");
        List<ExpertInfo> experts = JSON.parseArray(s, ExpertInfo.class);
        Log.e("TAG", experts.size() + "解析成功");
        return experts;
    }

    //解析浏览次数排行的专家数据
    public static List<ExpertInfo> parseExpertsByLook(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("expByLook");
        List<ExpertInfo> experts = JSON.parseArray(s, ExpertInfo.class);
        return experts;
    }


    //解析开启直播得到的json数据
    public static LiveRoom parseLive(String json) {
        LiveRoom room = new LiveRoom();
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend").getJSONObject("exp");

        String pic = jsonObject.getString("pic");
        String name = jsonObject.getString("name");

        JSONObject jsonObject1 = JSON.parseObject(json).getJSONObject("extend").getJSONObject("live");

        int id = jsonObject1.getInteger("id");
        String streamingurl = jsonObject1.getString("streamingurl");
        String playurl = jsonObject1.getString("playurl");
        String cover = jsonObject1.getString("cover");
        String title = jsonObject1.getString("title");
        String content = jsonObject1.getString("content");
        int see = jsonObject1.getInteger("see");
        String chatroom = jsonObject1.getString("chatroom");
        String major = jsonObject1.getString("major");

        room.setContent(content);
        room.setCover(cover);
        room.setId(id);
        room.setPic(pic);
        room.setPlayurl(playurl);
        room.setStreamingurl(streamingurl);
        room.setTitle(title);
        room.setName(name);
        room.setSee(see);
        room.setChatroom(chatroom);
        room.setMajor(major);

        return room;

    }

    //解析搜索直播间号码得到的json数据
    public static LiveRoom parseSearchLive(String json) {
        LiveRoom room = new LiveRoom();
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend").getJSONObject("user");

        String pic = jsonObject.getString("pic");
        String name = jsonObject.getString("name");

        JSONObject jsonObject1 = JSON.parseObject(json).getJSONObject("extend").getJSONObject("live");

        int id = jsonObject1.getInteger("id");
        String streamingurl = jsonObject1.getString("streamingurl");
        String playurl = jsonObject1.getString("playurl");
        String cover = jsonObject1.getString("cover");
        String title = jsonObject1.getString("title");
        String content = jsonObject1.getString("content");

        room.setContent(content);
        room.setCover(cover);
        room.setId(id);
        room.setPic(pic);
        room.setPlayurl(playurl);
        room.setStreamingurl(streamingurl);
        room.setTitle(title);
        room.setName(name);

        return room;
    }

    //解析所有视频信息
    public static List<VideoInfo> parseVideos(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("vedio");
        List<VideoInfo> videos = JSON.parseArray(s, VideoInfo.class);
        return videos;
    }

    //解析视频评论信息
    public static List<CommentInfo> parseVideoComments(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("vedioeva");
        List<CommentInfo> comments = JSON.parseArray(s, CommentInfo.class);
        return comments;
    }

    //解析所有问题信息
    public static List<ProblemInfo> parseProblems(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("forums");

        List<ProblemInfo> problems = JSON.parseArray(s, ProblemInfo.class);
        return problems;
    }

    //解析我发布的问题
    public static List<MyReleaseProblem> parseMyReleaseProblems(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        JSONObject jsonObject1 = JSON.parseObject(json).getJSONObject("extend").getJSONObject("answer");

        String s = jsonObject.getString("forums");

        List<ProblemInfo> problems = JSON.parseArray(s, ProblemInfo.class);

        List<MyReleaseProblem> myProblems = new ArrayList<>();
        if (problems != null && problems.size() > 0)
            for (int i = 0; i < problems.size(); i++) {
                MyReleaseProblem myProblem = new MyReleaseProblem();
                myProblem.setAnswer((String) jsonObject1.get(problems.get(i).getId() + ""));
                myProblem.setProblemInfo(problems.get(i));

                myProblems.add(myProblem);
            }

        return myProblems;
    }

    //解析问题的评论信息
    public static List<ProblemCommentInfo> parseProblemComment(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("eva");
        return JSON.parseArray(s, ProblemCommentInfo.class);
    }


    //解析我回答的问题
    public static List<MyAnswerProblem> parseMyAnswerProblems(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend").getJSONObject("question");
        JSONObject jsonObject1 = JSON.parseObject(json).getJSONObject("extend");

        List<ProblemCommentInfo> commentInfos = new ArrayList<>();
        List<MyAnswerProblem> myAnswerProblems = new ArrayList<>();
        String s = jsonObject1.getString("evas");

        commentInfos = JSON.parseArray(s, ProblemCommentInfo.class);

        if (commentInfos != null && commentInfos.size() > 0)
            for (int i = 0; i < commentInfos.size(); i++) {
                MyAnswerProblem myAnswerProblem = new MyAnswerProblem();
                myAnswerProblem.setProblemName(jsonObject.getString(commentInfos.get(i).getForumId() + ""));
                myAnswerProblem.setProblemPoint(jsonObject.getInteger("point" + commentInfos.get(i).getForumId()));
                myAnswerProblem.setProblemCommentInfo(commentInfos.get(i));
                myAnswerProblems.add(myAnswerProblem);
            }
        return myAnswerProblems;
    }

    //解析我发布的文档
    public static List<ReleaseDocInfo> parseMyReleaseDocs(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");

        String s = jsonObject.getString("publish");
        String s1 = jsonObject.getString("shenhe");

        List<ReleaseDocInfo> docs = new ArrayList<>();

        //未审核通过的
        List<DocInfo> docs2 = new ArrayList<>();
        docs2 = JSON.parseArray(s1, DocInfo.class);
        if (docs2 != null && docs2.size() > 0) {
            for (int i = 0; i < docs2.size(); i++) {
                ReleaseDocInfo releaseDocInfo = new ReleaseDocInfo();
                releaseDocInfo.setDocInfo(docs2.get(i));
                releaseDocInfo.setType(0);

                docs.add(releaseDocInfo);
            }
        }
        //已审核通过的
        List<DocInfo> docs1 = new ArrayList<>();
        docs1 = JSON.parseArray(s, DocInfo.class);
        if (docs1 != null && docs1.size() > 0) {
            for (int i = 0; i < docs1.size(); i++) {
                ReleaseDocInfo releaseDocInfo = new ReleaseDocInfo();
                releaseDocInfo.setDocInfo(docs1.get(i));
                releaseDocInfo.setType(1);

                docs.add(releaseDocInfo);
            }
        }

        return docs;
    }

    //解析我的收藏和购买
    public static List<DocInfo> parseMyCollectAndBuyDocs(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("files");
        return JSON.parseArray(s, DocInfo.class);

    }

    //解析正在进行中的信息
    public static List<MyExpertInfo> parseOrdering(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("orders");
        return JSON.parseArray(s, MyExpertInfo.class);
    }

    //解析专家评论信息
    public static List<ExpertComment> parseExpertComments(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("evaluate");
        return JSON.parseArray(s, ExpertComment.class);
    }

    //解析我的客户的json
    public static List<MyCustomer> parseCustomers(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("orders");
        return JSON.parseArray(s, MyCustomer.class);
    }

    //解析我的客户的支付状态
    public static List<MyCustomer> parseCustomersZF(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("DZF1");
        String s1 = jsonObject.getString("ZF1");

        List<MyCustomer> customers1 = JSON.parseArray(s, MyCustomer.class);
        List<MyCustomer> customers2 = JSON.parseArray(s1, MyCustomer.class);

        List<MyCustomer> customers = new ArrayList<>();
        if (customers1 != null && customers1.size() > 0) {
            customers.addAll(customers1);
        }
        if (customers2 != null && customers2.size() > 0) {
            customers.addAll(customers2);
        }
        return customers;
    }

    //解析我的收藏的专家
    public static List<ExpertInfo> parseMyExperts(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("exps");
        return JSON.parseArray(s, ExpertInfo.class);
    }

    public static List<ContactInfo> parseAllContacts(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("yh");

        return JSON.parseArray(s, ContactInfo.class);
    }

    //解析我的积分
    public static List<PointInfo> parseMyPoints(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("points");
        return JSON.parseArray(s, PointInfo.class);
    }

    //解析推荐讯息
    public static List<RecommendMSG> parseRecommends(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("recommends");
        return JSON.parseArray(s, RecommendMSG.class);
    }

    //解析系统消息
    public static List<SystemNotificationInfo> parseSystemNews(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("news");
        return JSON.parseArray(s, SystemNotificationInfo.class);
    }

    //解析群信息
    public static List<SearchGroupInfo> parseGroups(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("groups");
        return JSON.parseArray(s, SearchGroupInfo.class);
    }

    //解析需求信息
    public static List<DemandInfo> parseDemands(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("demands");
        return JSON.parseArray(s, DemandInfo.class);
    }

    //解析直播间
    public static List<LiveRoom> parseLiveRooms(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("lives");
        return JSON.parseArray(s, LiveRoom.class);
    }

    //解析直播回放
    public static List<ReLiveRoom> parseReLiveRooms(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("relives");
        return JSON.parseArray(s, ReLiveRoom.class);
    }

    //解析全部课程
    public static List<StudyInfo> parseStudys(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("studys");
        return JSON.parseArray(s, StudyInfo.class);
    }

    //解析交易记录
    public static List<TradeInfo> parseTrases(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("trades");
        return JSON.parseArray(s, TradeInfo.class);
    }
    //解析课程评论
    public static List<StudyCommendInfo> parseStudyCommends(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("vedioevas");
        return JSON.parseArray(s, StudyCommendInfo.class);
    }

    //解析专家页面发布的文档
    public static List<DocInfo> parseExpertDocs(String json) {
        JSONObject jsonObject = JSON.parseObject(json).getJSONObject("extend");
        String s = jsonObject.getString("publish");
        return JSON.parseArray(s, DocInfo.class);
    }
}
