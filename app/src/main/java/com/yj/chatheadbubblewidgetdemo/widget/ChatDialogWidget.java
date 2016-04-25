package com.yj.chatheadbubblewidgetdemo.widget;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconButton;
import com.yj.chatheadbubblewidgetdemo.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by toltori on 4/25/16.
 */
public class ChatDialogWidget {

/*  +-----------------------------------------------------------------------------------------------
    | Data members
    +-----------------------------------------------------------------------------------------------  */

    private ChatDialogEventListener m_eventListener;
    private UserInfo m_peerUser;
    private ArrayList<MessageInfo> m_lstMessages = new ArrayList<>();
    private ArrayList<MessageItem> m_lstMessageItems;
    private MessagesListAdapter m_adapter;



/*  +-----------------------------------------------------------------------------------------------
    | UI controls
    +-----------------------------------------------------------------------------------------------  */

    private Context m_context;
    private WindowManager m_windowManager;
    private LayoutInflater m_inflater;

    private View m_vwChatDialog;
    private TextView m_txtName;
    private IconButton m_btnClose;
    private ListView m_lvMessages;
    private EditText m_txtMessage;
    private IconButton m_btnSend;

    private WindowManager.LayoutParams m_lpChatDialog;


/*  +-----------------------------------------------------------------------------------------------
    | Overrides
    +-----------------------------------------------------------------------------------------------  */

    public ChatDialogWidget(Context p_context,
                            WindowManager p_windowManager,
                            LayoutInflater p_inflater,
                            ChatDialogEventListener p_eventListener) {
        m_context = p_context;
        m_windowManager = p_windowManager;
        m_inflater = p_inflater;
        m_eventListener = p_eventListener;

        initData();
        initUI();
    }



/*  +-----------------------------------------------------------------------------------------------
    | Methods
    +-----------------------------------------------------------------------------------------------  */

    public void setPeerUser(UserInfo p_peerUser) {
        m_peerUser = p_peerUser;
        if (m_peerUser != null)
            m_txtName.setText(m_peerUser.name);
    }

    public void setMessages(ArrayList<MessageInfo> p_lstMessages) {
        m_lstMessages.clear();
        if (p_lstMessages != null)
            m_lstMessages.addAll(p_lstMessages);
        initMessageItems();
        m_adapter.notifyDataSetChanged();
    }

    public void addNewMessage(MessageInfo p_message) {
        m_lstMessages.add(p_message);
        initMessageItems();
        m_adapter.notifyDataSetChanged();
    }

    public void show(boolean p_bShow) {
        if (p_bShow) {
            m_windowManager.addView(m_vwChatDialog, m_lpChatDialog);
        } else {
            m_windowManager.removeView(m_vwChatDialog);
        }
    }

    public void refreshMessagesUI() {
        initMessageItems();
        m_adapter.notifyDataSetChanged();
    }

    public void uninitialize() {
        m_windowManager.removeView(m_vwChatDialog);
    }


/*  +-----------------------------------------------------------------------------------------------
    | Event handlers
    +-----------------------------------------------------------------------------------------------  */

    private void onCloseBtnClick() {
        if (m_eventListener != null) {
            m_eventListener.onChatDialogClose();
        }
    }

    private void onSendBtnClick() {
        if (m_txtMessage.getText().toString().isEmpty()) {
            Toast.makeText(m_context, R.string.msg_input_message, Toast.LENGTH_LONG).show();
            return;
        }

        if (m_eventListener != null) {
            m_eventListener.onMessageSend(m_peerUser, m_txtMessage.getText().toString());
            MessageInfo w_newMessage = new MessageInfo(m_txtMessage.getText().toString(), false);
            addNewMessage(w_newMessage);
            m_txtMessage.setText("");
        }
    }


/*  +-----------------------------------------------------------------------------------------------
    | Helper functions
    +-----------------------------------------------------------------------------------------------  */
    
    private void initData() {
        m_lstMessageItems = new ArrayList<MessageItem>();
        initMessageItems();
        m_adapter = new MessagesListAdapter(m_context, m_lstMessageItems);
    }

    private void initUI() {
        m_vwChatDialog = m_inflater.inflate(R.layout.chat_dialog_view, null);
        m_vwChatDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCloseBtnClick();
            }
        });

        m_lpChatDialog = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        m_lpChatDialog.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
        m_lpChatDialog.gravity = Gravity.CENTER;
        m_vwChatDialog.setVisibility(View.VISIBLE);
        m_txtName = (TextView) m_vwChatDialog.findViewById(R.id.txt_name);
        m_btnClose = (IconButton) m_vwChatDialog.findViewById(R.id.ibtn_close);
        m_lvMessages = (ListView) m_vwChatDialog.findViewById(R.id.lv_messages);
        m_lvMessages.setAdapter(m_adapter);
        m_txtMessage = (EditText) m_vwChatDialog.findViewById(R.id.txt_message);
        m_btnSend = (IconButton) m_vwChatDialog.findViewById(R.id.ibtn_send);

        m_btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCloseBtnClick();
            }
        });
        m_btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendBtnClick();
            }
        });
    }

    //
    // MessageInfo배열에 날짜항목들을 삽입.
    //
    private void initMessageItems() {
        m_lstMessageItems.clear();
        Calendar w_cal = Calendar.getInstance();
        int w_nLastMessageDay = -1;

        for (int i = 0; i < m_lstMessages.size(); i++) {
            MessageInfo w_msgInfo = m_lstMessages.get(i);
            w_cal.setTime(w_msgInfo.time);

            if (w_nLastMessageDay == -1) {
                w_nLastMessageDay = w_cal.get(Calendar.DAY_OF_MONTH);
                m_lstMessageItems.add(new MessageItem(1, null, w_msgInfo.time));
            }

            int w_nMessageDay = w_cal.get(Calendar.DAY_OF_MONTH);

            if (w_nLastMessageDay != w_nMessageDay) {
                m_lstMessageItems.add(new MessageItem(1, null, w_msgInfo.time));
            }

            m_lstMessageItems.add(new MessageItem(0, w_msgInfo, null));

            w_nLastMessageDay = w_cal.get(Calendar.DAY_OF_MONTH);
        }
    }
}
