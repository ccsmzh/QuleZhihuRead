package qule.gino.zhihuread.utils;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuohong on 13-5-22.
 */
public class AppMessageUtil extends Handler {
    private static final int SHOW_MSG = 1;
    private static final int REMOVE_MEG = 2;

    private static AppMessageUtil mInstance;
    private List<AppMessage> mMsgQueue;

    private AppMessageUtil() {
        mMsgQueue = new ArrayList<AppMessage>();
    }

    public static AppMessageUtil getInstance() {
        if (mInstance == null) {
            synchronized (AppMessageUtil.class) {
                mInstance = new AppMessageUtil();
            }
        }

        return mInstance;
    }

    public void addMsg(AppMessage appMessage) {
        mMsgQueue.add(appMessage);
        Message msg = obtainMessage(SHOW_MSG);
//        msg.obj =

        sendEmptyMessage(SHOW_MSG);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_MSG:
                break;
            case REMOVE_MEG:
                break;
            default:
                super.handleMessage(msg);
        }
    }

}
