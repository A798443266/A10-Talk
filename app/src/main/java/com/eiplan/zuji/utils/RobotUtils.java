package com.eiplan.zuji.utils;

import com.eiplan.zuji.bean.ChatMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

/**
 * 机器人发消息的工具类 ，联网
 */

public class RobotUtils {
    private static String API_KEY = "5c0af5c34181493e800f3863971eb4df";
    private static String URL = "http://www.tuling123.com/openapi/api";

    /**
     * 发送一个消息，并得到返回的消息
     *
     * @param msg
     * @return
     */
    public static ChatMessage sendMsg(String msg) {
        Result result = null;
        ChatMessage message = new ChatMessage();
        String url = setParams(msg);
        String res = doGet(url);

        //用原生api解析
        try {
            JSONObject jsonObject = new JSONObject(res);
            int code = jsonObject.optInt("code");
            String text = jsonObject.optString("text");
            result = new Result(code,text);

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        Gson gson = new Gson();
//        Result result = gson.fromJson(res, Result.class);
        if(result != null)
            if (result.getCode() > 400000 || result.getText() == null
                    || result.getText().trim().equals("")) {
                message.setMsg("该功能等待开发...");
            } else {
                message.setMsg(result.getText());
            }
        message.setType(ChatMessage.Type.INPUT);
        message.setDate(new Date());

        return message;
    }

    /**
     * 拼接Url
     *
     * @param msg
     * @return
     */
    private static String setParams(String msg) {
        try {
            msg = URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return URL + "?key=" + API_KEY + "&info=" + msg;
    }

    /**
     * Get请求，获得返回数据
     *
     * @param urlStr
     * @return
     */
    private static String doGet(String urlStr) {
        java.net.URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5 * 1000);
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];

                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                return baos.toString();
            } else {
//                throw new CommonException("服务器连接错误！");
            }

        } catch (Exception e) {
            e.printStackTrace();
//            throw new CommonException("服务器连接错误！");
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.disconnect();
        }

        return "";

    }

    static class Result {
        private int code;
        private String text;

        public Result(int code, String text) {
            this.code = code;
            this.text = text;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }


}
