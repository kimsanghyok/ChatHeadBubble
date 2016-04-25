package com.yj.chatheadbubblewidgetdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.WindowManager;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.yj.chatheadbubblewidgetdemo.R;

import java.lang.reflect.Array;
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

        initialize();
    }

    @Override
    public void onBubbleClick() {

    }

    @Override
    public void onBubbleClose() {

    }

    @Override
    public void onMessageSend(UserInfo p_peerUser, String p_strMessage) {

    }

    @Override
    public void onChatDialogClose() {

    }


/*  +-----------------------------------------------------------------------------------------------
    | Methods
    +-----------------------------------------------------------------------------------------------  */

    public void setNewMessage(UserInfo p_peerUser, String p_strMessage) {
        boolean w_bClearUnread;
        if (m_peerUser == null) {
            w_bClearUnread = true;
        } else {
            w_bClearUnread = m_peerUser.id != p_peerUser.id;
        }
        m_peerUser = p_peerUser;
        MessageInfo w_newMessage = new MessageInfo(p_strMessage, true);
        m_lstMessages.add(w_newMessage);

        m_bubbleWidget.setNewUserMessage(m_peerUser, w_newMessage, w_bClearUnread);
    }

    private void initialize() {
        initImageLoader();
    }

    private void initImageLoader() {
        ImageLoaderConfiguration.Builder w_builder = new ImageLoaderConfiguration.Builder(m_context);
		w_builder.threadPriority(Thread.NORM_PRIORITY - 2);
		w_builder.denyCacheImageMultipleSizesInMemory();
		w_builder.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		w_builder.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		w_builder.tasksProcessingOrder(QueueProcessingType.LIFO);
		w_builder.writeDebugLogs(); // Remove for release app

        ImageLoader.getInstance().init(w_builder.build());
    }

    public void uninitialize() {
        m_bubbleWidget.uninitialize();
    }
}
