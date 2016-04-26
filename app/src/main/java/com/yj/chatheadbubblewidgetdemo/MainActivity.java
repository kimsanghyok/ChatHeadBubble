package com.yj.chatheadbubblewidgetdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yj.chatheadbubblewidgetdemo.widget.ChatHeadBubbleService;
import com.yj.chatheadbubblewidgetdemo.widget.UserInfo;

public class MainActivity extends AppCompatActivity {

/*  +-----------------------------------------------------------------------------------------------
    | Data members
    +-----------------------------------------------------------------------------------------------  */

    private UserInfo m_person1;
    private UserInfo m_person2;
    private String m_strMyMessages = "";

    private BroadcastReceiver m_brMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().toString().equals(ChatHeadBubbleService.MSG_MESSAGE_SEND)) {
                UserInfo w_peerUser = (UserInfo) intent.getSerializableExtra("peer_user");
                String w_strMessage = intent.getStringExtra("message");
                if (w_peerUser != null && w_strMessage != null && !w_strMessage.isEmpty()) {
                    //
                    // New message from chat dialog.
                    //
                    String w_strNewMessageInfo = "To " + w_peerUser.name + " : '" + w_strMessage + "'\n";
                    m_strMyMessages = m_strMyMessages + w_strNewMessageInfo;
                    m_txtMyMessages.setText(m_strMyMessages);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendMessage(m_person1, "that sounds great");
                        }
                    }, 2000);

                }
            }
        }
    };


/*  +-----------------------------------------------------------------------------------------------
    | UI controls
    +-----------------------------------------------------------------------------------------------  */

    private EditText m_txtMessage1;
    private EditText m_txtMessage2;
    private TextView m_txtMyMessages;



/*  +-----------------------------------------------------------------------------------------------
    | Overrides
    +-----------------------------------------------------------------------------------------------  */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initData();
        registerReceiver(m_brMessage, new IntentFilter(ChatHeadBubbleService.MSG_MESSAGE_SEND));
        startService(new Intent(this, ChatHeadBubbleService.class));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendMessage(m_person1, "hello, how are you?");
            }
        }, 6000);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(m_brMessage);
        super.onDestroy();
    }


/*  +-----------------------------------------------------------------------------------------------
    | Event handlers
    +-----------------------------------------------------------------------------------------------  */

    public void onSend1BtnClick(View view) {
        if (m_txtMessage1.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.msg_input_message, Toast.LENGTH_LONG).show();
            return;
        }

        sendMessage(m_person1, m_txtMessage1.getText().toString());
        m_txtMessage1.setText("");
    }

    public void onSend2BtnClick(View view) {
        if (m_txtMessage2.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.msg_input_message, Toast.LENGTH_LONG).show();
            return;
        }

        sendMessage(m_person2, m_txtMessage2.getText().toString());
        m_txtMessage2.setText("");
    }

    public void onClearBtnClick(View view) {
        m_strMyMessages = "";
        m_txtMyMessages.setText(m_strMyMessages);
    }



/*  +-----------------------------------------------------------------------------------------------
    | Helper functions
    +-----------------------------------------------------------------------------------------------  */

    private void initUI() {
        m_txtMessage1 = (EditText) findViewById(R.id.txt_message1);
        m_txtMessage2 = (EditText) findViewById(R.id.txt_message2);
        m_txtMyMessages = (TextView) findViewById(R.id.txt_my_messages);
    }

    private void initData() {
        m_person1 = new UserInfo();
        m_person1.id = 1;
        m_person1.name = "Arda Khan";
        m_person1.image_url = "http://i.telegraph.co.uk/multimedia/archive/03249/archetypal-male-fa_3249635c.jpg";

        m_person2 = new UserInfo();
        m_person2.id = 2;
        m_person2.name = "James Rodrigez";
        m_person2.image_url = "http://cfile5.uf.tistory.com/image/26545B4653AF40B52857E6";
    }

    private void sendMessage(UserInfo p_peerUser, String p_strMessage) {
        Intent w_i = new Intent(ChatHeadBubbleService.MSG_MESSAGE_RECV);
        w_i.putExtra("peer_user", p_peerUser);
        w_i.putExtra("message", p_strMessage);
        sendBroadcast(w_i);
    }
}
