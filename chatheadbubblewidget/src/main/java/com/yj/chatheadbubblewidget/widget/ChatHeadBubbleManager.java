package com.yj.chatheadbubblewidget.widget;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.WindowManager;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;

/**
 * Created by toltori on 4/25/16.
 */
public class ChatHeadBubbleManager implements BubbleEventListener, ChatDialogEventListener {

/*  +-----------------------------------------------------------------------------------------------
    | Data members
    +-----------------------------------------------------------------------------------------------  */

    private Context m_context;
    private WindowManager m_windowManager;
    private LayoutInflater m_inflater;
    private BubbleWidget m_bubbleWidget;
    private ChatDialogWidget m_chatDialogWidget;
    private boolean m_bBubbleViewVisible = false;
    private boolean m_bChatDialogVisible = false;
    private UserInfo m_peerUser = null;
    private ArrayList<MessageInfo> m_lstMessages = new ArrayList<>();

/*  +-----------------------------------------------------------------------------------------------
    | Overrides
    +-----------------------------------------------------------------------------------------------  */

    public ChatHeadBubbleManager(Context p_context) {
        m_context = p_context;
        m_windowManager = (WindowManager) m_context.getSystemService(Context.WINDOW_SERVICE);
        m_inflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_bubbleWidget = new BubbleWidget(m_context, m_windowManager, m_inflater, this);
        m_chatDialogWidget = new ChatDialogWidget(m_context, m_windowManager, m_inflater, this);

        initialize();
    }

    @Override
    public void onBubbleClick() {
        m_bChatDialogVisible = !m_bChatDialogVisible;

        if (m_bChatDialogVisible) {
            m_chatDialogWidget.setPeerUser(m_peerUser);
            m_chatDialogWidget.setMessages(m_lstMessages);
        }
        m_chatDialogWidget.show(m_bChatDialogVisible);
    }

    @Override
    public void onBubbleClose() {
        if (m_bBubbleViewVisible) {
            m_bubbleWidget.show(false);
            m_bBubbleViewVisible = false;
        }
        if (m_bChatDialogVisible) {
            m_chatDialogWidget.show(false);
            m_bChatDialogVisible = false;
        }
        m_peerUser = null;
        m_lstMessages.clear();

        m_bubbleWidget = new BubbleWidget(m_context, m_windowManager, m_inflater, this);
        m_chatDialogWidget = new ChatDialogWidget(m_context, m_windowManager, m_inflater, this);
        m_bubbleWidget.show(false);
    }

    @Override
    public void onMessageSend(UserInfo p_peerUser, String p_strMessage) {
        Intent w_i = new Intent(ChatHeadBubbleService.MSG_MESSAGE_SEND);
        w_i.putExtra("peer_user", p_peerUser);
        w_i.putExtra("message", p_strMessage);
        m_context.sendBroadcast(w_i);

        m_bubbleWidget.setMyMessage(p_strMessage);
    }

    @Override
    public void onChatDialogClose() {
        m_chatDialogWidget.show(false);
        m_bChatDialogVisible = false;
    }


/*  +-----------------------------------------------------------------------------------------------
    | Methods
    +-----------------------------------------------------------------------------------------------  */

    public void setNewMessage(UserInfo p_peerUser, String p_strMessage) {
        if (!m_bBubbleViewVisible) {
            m_bBubbleViewVisible = true;
            m_bubbleWidget.show(m_bBubbleViewVisible);
        }

        boolean w_bClearUnread;
        if (m_peerUser == null) {
            w_bClearUnread = true;
        } else {
            w_bClearUnread = m_peerUser.id != p_peerUser.id;
        }
        m_peerUser = p_peerUser;
        MessageInfo w_newMessage = new MessageInfo(p_strMessage, true);
        if (w_bClearUnread)
            m_lstMessages.clear();
        m_lstMessages.add(w_newMessage);

        m_bubbleWidget.setNewUserMessage(m_peerUser, w_newMessage, w_bClearUnread, !m_bChatDialogVisible);
        if (m_bChatDialogVisible)
            m_chatDialogWidget.refreshMessagesUI();
    }

    private void initialize() {
        initImageLoader();
        initIconify();
        m_bubbleWidget.show(false);
    }

    private void initImageLoader() {
        ImageLoaderConfiguration.Builder w_builder = new ImageLoaderConfiguration.Builder(m_context);
		w_builder.threadPriority(Thread.MAX_PRIORITY);
		w_builder.denyCacheImageMultipleSizesInMemory();
		w_builder.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		w_builder.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		w_builder.tasksProcessingOrder(QueueProcessingType.LIFO);
		w_builder.writeDebugLogs(); // Remove for release app

        ImageLoader.getInstance().init(w_builder.build());
    }

    private void initIconify() {
        Iconify.with(new FontAwesomeModule());
    }

    public void uninitialize() {
        m_bubbleWidget.uninitialize();
        if (m_bChatDialogVisible)
            m_chatDialogWidget.uninitialize();
    }
}
