package com.yj.chatheadbubblewidgetdemo.widget;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.yj.chatheadbubblewidgetdemo.widget.ChatHeadBubbleManager;
import com.yj.chatheadbubblewidgetdemo.widget.UserInfo;
import com.yj.chatheadbubblewidgetdemo.widget.Utils;

/**
 * Created by toltori on 4/25/16.
 */
public class ChatHeadBubbleService extends Service {

/*  +-----------------------------------------------------------------------------------------------
    | Data members
    +-----------------------------------------------------------------------------------------------  */

    public static final String MSG_MESSAGE_RECV = "msg_message_recv";    // called from front-end.
    public static final String MSG_MESSAGE_SEND = "msg_message_send";    // call to front-end.

    private ChatHeadBubbleManager m_manager = null;
    private BroadcastReceiver m_brMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().toString().equals(MSG_MESSAGE_RECV)) {
                UserInfo w_peerUser = (UserInfo) intent.getSerializableExtra("peer_user");
                String w_strMessage = intent.getStringExtra("message");
                if (w_peerUser != null && w_strMessage != null && !w_strMessage.isEmpty()) {
                    if (m_manager != null) {
                        m_manager.setNewMessage(w_peerUser, w_strMessage);
                    }
                }
            }
        }
    };


    @SuppressWarnings("deprecation")
/*  +-----------------------------------------------------------------------------------------------
    | Overrides
    +-----------------------------------------------------------------------------------------------  */

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Utils.LogTag, "ChatHeadBubbleService.onCreate()");
        registerReceiver(m_brMessage, new IntentFilter(MSG_MESSAGE_RECV));
    }

    @Override
    public int onStartCommand(Intent p_intent, int p_nFlags, int p_nServiceId) {
        Log.d(Utils.LogTag, "ChatHeadBubbleService.onStartCommand() -> startId=" + p_nServiceId);
        super.onStartCommand(p_intent, p_nFlags, p_nServiceId);

        if(p_intent != null){
//            Bundle bd = intent.getExtras();

//            if(bd != null)
//                sMsg = bd.getString(Utils.EXTRA_MSG);
//
//            if(sMsg != null && sMsg.length() > 0){
//                if(startId == Service.START_STICKY){
//                    new Handler().postDelayed(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            // TODO Auto-generated method stub
//                            showMsg(sMsg);
//                        }
//                    }, 300);
//
//                }else{
//                    showMsg(sMsg);
//                }
//
//            }

        }

        if (m_manager == null) {
            m_manager = new ChatHeadBubbleManager(this);
            //test();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(m_brMessage);
        m_manager.uninitialize();
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Utils.LogTag, "ChatHeadBubbleService.onBind()");
        return null;
    }

    public void test() {
        final UserInfo w_userInfo = new UserInfo();
        w_userInfo.id = 1;
        w_userInfo.name = "Arda Khan";
        w_userInfo.image_url = "http://i.telegraph.co.uk/multimedia/archive/03249/archetypal-male-fa_3249635c.jpg";
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                m_manager.setNewMessage(w_userInfo, "Hello Joe!\nGood morning?\nHow you today?");
                m_manager.setNewMessage(w_userInfo, "Today is busy day, isn't it?");
            }
        });
    }
}
