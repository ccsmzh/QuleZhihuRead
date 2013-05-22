package qule.gino.zhihuread.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.ListView;
import qule.gino.zhihuread.BuildConfig;

/**
 * Created by Administrator on 13-5-21.
 */
public class RefreshListView extends ListView {
    private static final String TAG = "RefreshListView";

    public static final int LEFT_DIRECTION = 1;
    public static final int RIGHT_DIRECTION = 2;
    private VelocityTracker mVelocityTracker;
    private boolean mTrack = false;

    private int mLastDirectoin = -1;

    private float mX;

    public RefreshListView(Context context) {
        super(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (BuildConfig.DEBUG) {
            Log.d(TAG, " - onTouchEvent -  " + event.getAction());
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (mX - event.getX() > 0) {
                    Log.d(TAG, "在右边刷新");
                    mLastDirectoin = RIGHT_DIRECTION;
                } else {
                    mLastDirectoin = LEFT_DIRECTION;
                    Log.d(TAG, "在左边刷新");
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public int getLastDirection(){
        return mLastDirectoin;
    }
}
