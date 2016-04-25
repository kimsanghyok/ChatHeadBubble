package com.yj.chatheadbubblewidgetdemo.widget;

import java.util.Date;

/**
 * Created by toltori on 2014. 9. 24..
 */
public class MessageItem {
    public int Type;   // 0 : MessageInfo, 1 : Date object
    public MessageInfo MessageInfo;
    public Date DateObj;

    public MessageItem(int p_nType, MessageInfo p_messageInfo, Date p_dateObj) {
        Type = p_nType;
        MessageInfo = p_messageInfo;
        DateObj = p_dateObj;
    }
}
