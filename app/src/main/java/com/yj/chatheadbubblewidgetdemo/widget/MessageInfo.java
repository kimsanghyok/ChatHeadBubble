package com.yj.chatheadbubblewidgetdemo.widget;

import java.util.Date;

/**
 * Created by toltori on 4/25/16.
 */
public class MessageInfo {
    public String message;
    public Date time;
    public boolean isRecv;  // true: Peer user's message, false: My message.
}