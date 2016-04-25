package com.yj.chatheadbubblewidgetdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

import com.yj.chatheadbubblewidgetdemo.widget.ChatHeadBubbleManager;

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
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Utils.LogTag, "ChatHeadBubbleService.onStartCommand() -> startId=" + startId);

        if(intent != null){
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

        if(startId == Service.START_STICKY) {
            if (m_manager == null) {
                m_manager = new ChatHeadBubbleManager(this);
            }
            return super.onStartCommand(intent, flags, startId);
        }else{
            return  Service.START_NOT_STICKY;
        }
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
}
