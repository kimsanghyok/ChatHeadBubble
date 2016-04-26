package com.yj.chatheadbubblewidget.widget;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yj.chatheadbubblewidget.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by toltori on 4/25/16.
 */
public class BubbleWidget {

/*  +-----------------------------------------------------------------------------------------------
    | Data members
    +-----------------------------------------------------------------------------------------------  */

    private BubbleEventListener m_eventListener;
    private UserInfo m_peerUser = null;
    private String m_strLastMessage = "";
    private String m_strTime = "Just";
    private int m_nUnreadCnt = 0;

    private int x_init_cord, y_init_cord, x_init_margin, y_init_margin; // For touch event handling.
    private Point szWindow = new Point();
    private boolean isLeft = true;


/*  +-----------------------------------------------------------------------------------------------
    | UI controls
    +-----------------------------------------------------------------------------------------------  */

    private Context m_context;
    private WindowManager m_windowManager;
    private LayoutInflater m_inflater;

    private View m_vwBubbleLayout;
    private RoundedImageView m_ivUserImage;
    private TextView m_txtName;
    private TextView m_txtUnreadCnt;

    private LinearLayout m_llLastMessage;
    private TextView m_txtTime;
    private TextView m_txtLastMessage;

    private View m_vwRemove;
    private ImageView m_ivRemove;

    private WindowManager.LayoutParams m_lpBubbleView;
    private WindowManager.LayoutParams m_lpLastMessage;


/*  +-----------------------------------------------------------------------------------------------
    | Overrides
    +-----------------------------------------------------------------------------------------------  */

    public BubbleWidget(Context p_context,
                        WindowManager p_m_windowManager,
                        LayoutInflater p_inflater,
                        BubbleEventListener p_eventListener) {
        m_context = p_context;
        m_windowManager = p_m_windowManager;
        m_inflater = p_inflater;
        m_eventListener = p_eventListener;

        initUI();
    }



/*  +-----------------------------------------------------------------------------------------------
    | Methods
    +-----------------------------------------------------------------------------------------------  */

    public void show(boolean p_bShow) {
        if (p_bShow) {
            m_windowManager.addView(m_vwBubbleLayout, m_lpBubbleView);
            m_windowManager.addView(m_llLastMessage, m_lpLastMessage);
        } else {
            m_windowManager.removeView(m_vwBubbleLayout);
            m_windowManager.removeView(m_llLastMessage);
        }
    }

    public void setMyMessage(String p_strMyMessage) {
        m_strLastMessage = "Me: " + p_strMyMessage;
        SimpleDateFormat w_sdf = new SimpleDateFormat("M/d k:m a");
        m_strTime = w_sdf.format(new Date());
        refreshUI();
    }

    public void setNewUserMessage(UserInfo p_peerUser, MessageInfo p_message, boolean p_bClearUnreadCnt, boolean p_bIncreaseUnreadCnt) {
        if (p_bClearUnreadCnt)
            m_nUnreadCnt = 0;
        m_peerUser = p_peerUser;
        m_strLastMessage = p_message.message;
        SimpleDateFormat w_sdf = new SimpleDateFormat("M/d k:m a");
        m_strTime = w_sdf.format(new Date());

        if (p_bIncreaseUnreadCnt)
            m_nUnreadCnt++;
        refreshUI();
    }

    public void uninitialize() {
        m_windowManager.removeView(m_vwBubbleLayout);
        m_windowManager.removeView(m_vwRemove);
    }


/*  +-----------------------------------------------------------------------------------------------
    | Helper functions
    +-----------------------------------------------------------------------------------------------  */

    private void initUI() {
        m_vwBubbleLayout = m_inflater.inflate(R.layout.bubble_widget_view, null);
        m_lpBubbleView = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        m_lpBubbleView.gravity = Gravity.TOP | Gravity.LEFT;
        m_vwBubbleLayout.setVisibility(View.VISIBLE);
        m_ivUserImage = (RoundedImageView) m_vwBubbleLayout.findViewById(R.id.iv_user_image);
        m_txtName = (TextView) m_vwBubbleLayout.findViewById(R.id.txt_name);
        m_txtUnreadCnt = (TextView) m_vwBubbleLayout.findViewById(R.id.txt_unread_cnt);
        m_windowManager.addView(m_vwBubbleLayout, m_lpBubbleView);

        m_vwRemove = m_inflater.inflate(R.layout.remove_view, null);
        WindowManager.LayoutParams w_lpRemoveView = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        w_lpRemoveView.gravity = Gravity.TOP | Gravity.LEFT;
        m_vwRemove.setVisibility(View.GONE);
        m_ivRemove = (ImageView) m_vwRemove.findViewById(R.id.remove_img);
        //m_windowManager.addView(m_vwRemove, w_lpRemoveView);

        m_llLastMessage = (LinearLayout) m_inflater.inflate(R.layout.bubble_text_view, null);
        m_txtTime = (TextView) m_llLastMessage.findViewById(R.id.txt_time);
        m_txtLastMessage = (TextView) m_llLastMessage.findViewById(R.id.txt_last_message);
        m_lpLastMessage = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        m_lpLastMessage.gravity = Gravity.TOP | Gravity.LEFT;
        m_llLastMessage.setVisibility(View.GONE);
        m_windowManager.addView(m_llLastMessage, m_lpLastMessage);
        
        initTouch();
        refreshUI();
    }

    private void initTouch() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            m_windowManager.getDefaultDisplay().getSize(szWindow);
        } else {
            int w = m_windowManager.getDefaultDisplay().getWidth();
            int h = m_windowManager.getDefaultDisplay().getHeight();
            szWindow.set(w, h);
        }

        m_vwBubbleLayout.setOnTouchListener(new View.OnTouchListener() {
            long time_start = 0, time_end = 0;
            boolean isLongclick = false, inBounded = false;
            int remove_img_width = 0, remove_img_height = 0;

            Handler handler_longClick = new Handler();
            Runnable runnable_longClick = new Runnable() {

                @Override
                public void run() {
                    Log.d(Utils.LogTag, "Into runnable_longClick");

                    /*
                    isLongclick = true;
                    m_vwRemove.setVisibility(View.VISIBLE);
                    chathead_longclick();
                    */
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) m_vwBubbleLayout.getLayoutParams();

                int x_cord = (int) event.getRawX();
                int y_cord = (int) event.getRawY();
                int x_cord_Destination, y_cord_Destination;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        time_start = System.currentTimeMillis();
                        handler_longClick.postDelayed(runnable_longClick, 600);

                        remove_img_width = m_ivRemove.getLayoutParams().width;
                        remove_img_height = m_ivRemove.getLayoutParams().height;

                        x_init_cord = x_cord;
                        y_init_cord = y_cord;

                        x_init_margin = layoutParams.x;
                        y_init_margin = layoutParams.y;

                        break;
                    case MotionEvent.ACTION_MOVE:
                        int x_diff_move = x_cord - x_init_cord;
                        int y_diff_move = y_cord - y_init_cord;

                        x_cord_Destination = x_init_margin + x_diff_move;
                        y_cord_Destination = y_init_margin + y_diff_move;

                        if (isLongclick) {
                            int x_bound_left = szWindow.x / 2 - (int) (remove_img_width * 1.5);
                            int x_bound_right = szWindow.x / 2 + (int) (remove_img_width * 1.5);
                            int y_bound_top = szWindow.y - (int) (remove_img_height * 1.5);

                            if ((x_cord >= x_bound_left && x_cord <= x_bound_right) && y_cord >= y_bound_top) {
                                inBounded = true;

                                int x_cord_remove = (int) ((szWindow.x - (remove_img_height * 1.5)) / 2);
                                int y_cord_remove = (int) (szWindow.y - ((remove_img_width * 1.5) + getStatusBarHeight()));

                                if (m_ivRemove.getLayoutParams().height == remove_img_height) {
                                    m_ivRemove.getLayoutParams().height = (int) (remove_img_height * 1.5);
                                    m_ivRemove.getLayoutParams().width = (int) (remove_img_width * 1.5);

                                    WindowManager.LayoutParams param_remove = (WindowManager.LayoutParams) m_vwRemove.getLayoutParams();
                                    param_remove.x = x_cord_remove;
                                    param_remove.y = y_cord_remove;

                                    m_windowManager.updateViewLayout(m_vwRemove, param_remove);
                                }

                                layoutParams.x = x_cord_remove + (Math.abs(m_vwRemove.getWidth() - m_vwBubbleLayout.getWidth())) / 2;
                                layoutParams.y = y_cord_remove + (Math.abs(m_vwRemove.getHeight() - m_vwBubbleLayout.getHeight())) / 2;

                                m_windowManager.updateViewLayout(m_vwBubbleLayout, layoutParams);
                                break;
                            } else {
                                inBounded = false;
                                m_ivRemove.getLayoutParams().height = remove_img_height;
                                m_ivRemove.getLayoutParams().width = remove_img_width;

                                WindowManager.LayoutParams param_remove = (WindowManager.LayoutParams) m_vwRemove.getLayoutParams();
                                int x_cord_remove = (szWindow.x - m_vwRemove.getWidth()) / 2;
                                int y_cord_remove = szWindow.y - (m_vwRemove.getHeight() + getStatusBarHeight());

                                param_remove.x = x_cord_remove;
                                param_remove.y = y_cord_remove;

                                m_windowManager.updateViewLayout(m_vwRemove, param_remove);
                            }

                        }


                        layoutParams.x = x_cord_Destination;
                        layoutParams.y = y_cord_Destination;

                        m_windowManager.updateViewLayout(m_vwBubbleLayout, layoutParams);
                        showMsg();
                        break;
                    case MotionEvent.ACTION_UP:
                        isLongclick = false;
                        m_vwRemove.setVisibility(View.GONE);
                        m_ivRemove.getLayoutParams().height = remove_img_height;
                        m_ivRemove.getLayoutParams().width = remove_img_width;
                        handler_longClick.removeCallbacks(runnable_longClick);

                        if (inBounded) {
                            if (m_eventListener != null)
                                m_eventListener.onBubbleClose();
                            inBounded = false;
                            break;
                        }


                        int x_diff = x_cord - x_init_cord;
                        int y_diff = y_cord - y_init_cord;

                        if (Math.abs(x_diff) < 5 && Math.abs(y_diff) < 5) {
                            time_end = System.currentTimeMillis();
                            if ((time_end - time_start) < 300) {
                                chathead_click();
                            }
                        }

                        y_cord_Destination = y_init_margin + y_diff;

                        int BarHeight = getStatusBarHeight();
                        if (y_cord_Destination < 0) {
                            y_cord_Destination = 0;
                        } else if (y_cord_Destination + (m_vwBubbleLayout.getHeight() + BarHeight) > szWindow.y) {
                            y_cord_Destination = szWindow.y - (m_vwBubbleLayout.getHeight() + BarHeight);
                        }
                        layoutParams.y = y_cord_Destination;

                        inBounded = false;
                        resetPosition(x_cord);

                        break;
                    default:
                        Log.d(Utils.LogTag, "m_vwBubbleLayout.setOnTouchListener  -> event.getAction() : default");
                        break;
                }
                return true;
            }
        });
    }

    private void refreshUI() {
        if (m_peerUser != null) {
            ImageLoader.getInstance().displayImage(m_peerUser.image_url, m_ivUserImage);
            m_txtName.setVisibility(View.VISIBLE);
            m_txtName.setText(m_peerUser.name);
        } else {
            m_ivUserImage.setImageResource(R.drawable.unknown_user);
            m_txtName.setVisibility(View.INVISIBLE);
        }
        m_txtUnreadCnt.setVisibility(m_nUnreadCnt == 0 ? View.INVISIBLE : View.VISIBLE);
        m_txtUnreadCnt.setText(String.format("%d", m_nUnreadCnt));
        showMsg();
    }

    private void showMsg(){
        Log.d(Utils.LogTag, "ChatHeadService.showMsg -> m_strLastMessage=" + m_strLastMessage);
        m_txtTime.setText(m_strTime);
        m_txtLastMessage.setText(m_strLastMessage);
        m_llLastMessage.measure(0, 0);

        WindowManager.LayoutParams param_chathead = (WindowManager.LayoutParams) m_vwBubbleLayout.getLayoutParams();
        WindowManager.LayoutParams param_txt = (WindowManager.LayoutParams) m_llLastMessage.getLayoutParams();

        if (m_llLastMessage.getLayoutParams() == null) {
            return;
        }
        m_llLastMessage.getLayoutParams().height = ActionBar.LayoutParams.WRAP_CONTENT;//m_vwBubbleLayout.getHeight();
        m_llLastMessage.getLayoutParams().width = ActionBar.LayoutParams.WRAP_CONTENT;

        int w_fHorizontalGap = 10;
        if(isLeft){
            param_txt.x = param_chathead.x + w_fHorizontalGap + m_ivUserImage.getWidth();
            param_txt.y = param_chathead.y;

            m_llLastMessage.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            m_llLastMessage.setBackgroundResource(R.drawable.chatting_left_bg);
        }else{
            param_txt.x = param_chathead.x - w_fHorizontalGap - (m_llLastMessage.getMeasuredWidth() > 1.0f ? m_llLastMessage.getMeasuredWidth() : szWindow.x / 2);
            param_txt.y = param_chathead.y;

            m_llLastMessage.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            m_llLastMessage.setBackgroundResource(R.drawable.chatting_right_bg);
        }

        m_llLastMessage.setVisibility(View.VISIBLE);
        m_windowManager.updateViewLayout(m_llLastMessage, param_txt);
    }

    private void resetPosition(int x_cord_now) {
        if (x_cord_now <= szWindow.x / 2) {
            isLeft = true;
            moveToLeft(x_cord_now);
        } else {
            isLeft = false;
            moveToRight(x_cord_now);
        }
    }

    private void moveToLeft(final int x_cord_now) {
        final int x = szWindow.x - x_cord_now;

        new CountDownTimer(500, 5) {
            WindowManager.LayoutParams mParams = (WindowManager.LayoutParams) m_vwBubbleLayout.getLayoutParams();

            public void onTick(long t) {
                long step = (500 - t) / 5;
                mParams.x = 0 - (int) (double) bounceValue(step, x);
                m_windowManager.updateViewLayout(m_vwBubbleLayout, mParams);
                showMsg();
            }

            public void onFinish() {
                mParams.x = 0;
                m_windowManager.updateViewLayout(m_vwBubbleLayout, mParams);
                showMsg();
            }
        }.start();
    }

    private void moveToRight(final int x_cord_now) {
        new CountDownTimer(500, 5) {
            WindowManager.LayoutParams mParams = (WindowManager.LayoutParams) m_vwBubbleLayout.getLayoutParams();

            public void onTick(long t) {
                long step = (500 - t) / 5;
                mParams.x = szWindow.x + (int) (double) bounceValue(step, x_cord_now) - m_vwBubbleLayout.getWidth();
                m_windowManager.updateViewLayout(m_vwBubbleLayout, mParams);
                showMsg();
            }

            public void onFinish() {
                mParams.x = szWindow.x - m_vwBubbleLayout.getWidth();
                m_windowManager.updateViewLayout(m_vwBubbleLayout, mParams);
                showMsg();
            }
        }.start();
    }

    private double bounceValue(long step, long scale) {
        double value = scale * java.lang.Math.exp(-0.055 * step) * java.lang.Math.cos(0.08 * step);
        return value;
    }

    private int getStatusBarHeight() {
        int statusBarHeight = (int) Math.ceil(25 * m_context.getResources().getDisplayMetrics().density);
        return statusBarHeight;
    }

    private void chathead_click() {
        m_nUnreadCnt = 0;
        m_txtUnreadCnt.setText("");
        m_txtUnreadCnt.setVisibility(View.INVISIBLE);
        if (m_eventListener != null) {
            m_eventListener.onBubbleClick();
        }
    }

    private void chathead_longclick() {
        Log.d(Utils.LogTag, "Into ChatHeadService.chathead_longclick() ");

        WindowManager.LayoutParams param_remove = (WindowManager.LayoutParams) m_vwRemove.getLayoutParams();
        int x_cord_remove = (szWindow.x - m_vwRemove.getWidth()) / 2;
        int y_cord_remove = szWindow.y - (m_vwRemove.getHeight() + getStatusBarHeight());

        param_remove.x = x_cord_remove;
        param_remove.y = y_cord_remove;

        m_windowManager.updateViewLayout(m_vwRemove, param_remove);
    }

}
