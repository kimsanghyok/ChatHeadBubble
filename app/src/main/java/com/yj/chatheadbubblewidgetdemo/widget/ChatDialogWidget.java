package com.yj.chatheadbubblewidgetdemo.widget;

import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by toltori on 4/25/16.
 */
public class ChatDialogWidget {

/*  +-----------------------------------------------------------------------------------------------
    | Data members
    +-----------------------------------------------------------------------------------------------  */

    private ChatDialogEventListener m_eventListener;
    private UserInfo m_peerUser;
    private ArrayList<MessageInfo> m_lstMessages;



/*  +-----------------------------------------------------------------------------------------------
    | UI controls
    +-----------------------------------------------------------------------------------------------  */

    private View m_vwChatDialogLayout;



/*  +-----------------------------------------------------------------------------------------------
    | Overrides
    +-----------------------------------------------------------------------------------------------  */

    public ChatDialogWidget(WindowManager p_windowManager, ChatDialogEventListener p_eventListener) {
        m_eventListener = p_eventListener;
    }



/*  +-----------------------------------------------------------------------------------------------
    | Methods
    +-----------------------------------------------------------------------------------------------  */

    public void setPeerUser(UserInfo p_peerUser) {
        m_peerUser = p_peerUser;
    }

    public void setMessages(ArrayList<MessageInfo> p_lstMessages) {
        m_lstMessages = p_lstMessages;
    }

    public void addNewMessage(MessageInfo p_message) {

    }

    public void setNewUserMessage(UserInfo p_peerUser, MessageInfo p_message, boolean p_bClearUnreadCnt) {

    }


/*  +-----------------------------------------------------------------------------------------------
    | Helper functions
    +-----------------------------------------------------------------------------------------------  */

    private void initUI() {

    }
}
