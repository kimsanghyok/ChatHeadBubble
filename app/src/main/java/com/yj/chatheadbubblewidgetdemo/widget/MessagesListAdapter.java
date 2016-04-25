package com.yj.chatheadbubblewidgetdemo.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.yj.chatheadbubblewidgetdemo.R;

import java.util.ArrayList;

/**
 * Created by toltori on 2014. 8. 29..
 */
public class MessagesListAdapter extends BaseAdapter {

    ////////////////////////////////////
    // Data members
    ////////////////////////////////////
    private Context m_context;
    private LayoutInflater m_layoutInflater;
    private ArrayList<MessageItem> m_lstMessageItems;


    public MessagesListAdapter(Context p_context, ArrayList<MessageItem> p_lstMessageItems) {
        m_context = p_context;
        m_layoutInflater = (LayoutInflater) p_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_lstMessageItems = p_lstMessageItems;
    }


    @Override
    public int getCount() {
        return m_lstMessageItems.size();
    }

    @Override
    public Object getItem(int i) {
        return m_lstMessageItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int p_nPosition) {
        if (m_lstMessageItems.get(p_nPosition).Type == 0)
            return m_lstMessageItems.get(p_nPosition).MessageInfo.isRecv ? 1 : 0;
        else return 2;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MessageItem w_msgItem = m_lstMessageItems.get(i);

        if (w_msgItem.Type == 0) {
            final MessageInfo w_messageInfo = w_msgItem.MessageInfo;

            if (!w_messageInfo.isRecv) {
                view = m_layoutInflater.inflate(R.layout.item_message_me, viewGroup, false);
                TextView w_txtMessage = (TextView) view.findViewById(R.id.chatting_me_msg);
                TextView w_txtRegDate = (TextView) view.findViewById(R.id.chatting_me_regDate);

                w_txtMessage.setText(w_messageInfo.message);
                w_txtRegDate.setText(Utils.getTimeExpression(w_messageInfo.time));
            } else {
                view = m_layoutInflater.inflate(R.layout.item_message_target, viewGroup, false);
                TextView w_txtMessage = (TextView) view.findViewById(R.id.chatting_target_msg);
                TextView w_txtRegDate = (TextView) view.findViewById(R.id.chatting_target_regDate);
                w_txtMessage.setText(w_messageInfo.message);
                w_txtRegDate.setText(Utils.getTimeExpression(w_messageInfo.time));
            }
        } else {
            view = m_layoutInflater.inflate(R.layout.item_message_date, viewGroup, false);
            TextView w_txtDate = (TextView) view.findViewById(R.id.txt_date);
            w_txtDate.setText(Utils.getDateExpression(w_msgItem.DateObj));
        }

        return view;
    }
}
