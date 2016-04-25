package com.yj.chatheadbubblewidgetdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

import com.yj.chatheadbubblewidgetdemo.widget.ChatHeadBubbleManager;
import com.yj.chatheadbubblewidgetdemo.widget.UserInfo;

/**
 * Created by toltori on 4/25/16.
 */
public class ChatHeadBubbleService extends Service {

/*  +-----------------------------------------------------------------------------------------------
    | Data members
    +-----------------------------------------------------------------------------------------------  */

    private ChatHeadBubbleManager m_manager = null;


    @SuppressWarnings("deprecation")
/*  +-----------------------------------------------------------------------------------------------
    | Overrides
    +-----------------------------------------------------------------------------------------------  */

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Utils.LogTag, "ChatHeadBubbleService.onCreate()");
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
            test();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
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
