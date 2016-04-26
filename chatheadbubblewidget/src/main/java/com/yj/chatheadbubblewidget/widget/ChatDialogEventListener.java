package com.yj.chatheadbubblewidget.widget;

/**
 * Created by toltori on 4/25/16.
 */
public interface ChatDialogEventListener {
    public void onMessageSend(UserInfo p_peerUser, String p_strMessage);
    public void onChatDialogClose();
}
