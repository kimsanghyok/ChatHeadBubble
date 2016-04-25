package com.yj.chatheadbubblewidgetdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yj.chatheadbubblewidgetdemo.widget.ChatHeadBubbleService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, ChatHeadBubbleService.class));
        finish();
    }
}
