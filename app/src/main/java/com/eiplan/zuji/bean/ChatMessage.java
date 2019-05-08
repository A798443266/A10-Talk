package com.eiplan.zuji.bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 机器人聊天消息类
 */

public class ChatMessage {
    /**
     * 消息类型
     */
    private Type type ;
    /**
     * 消息内容
     */
    private String msg;
    /**
     * 日期
     */
    private Date date;
    /**
     * 日期的字符串格式
     */
    private String dateStr;

    public enum Type
    {
        INPUT, OUTPUT
    }

    public ChatMessage()
    {
    }

    public ChatMessage(Type type, String msg)
    {
        super();
        this.type = type;
        this.msg = msg;
        setDate(new Date());
    }

    public String getDateStr()
    {
        return dateStr;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        this.dateStr = df.format(date);

    }

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

}
