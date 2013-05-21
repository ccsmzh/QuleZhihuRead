package qule.gino.zhihuread.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by zhuohong on 13-5-21.
 */
public class GestureUtils implements GestureDetector.OnGestureListener{
    private GestureDetector detector;
    public GestureUtils(Context context){
        detector = new GestureDetector(context,this);
    }

//    public  void test(Context context){
//        detector = new GestureDetector(context,new SwipeG)
//    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    //滑动手势事件.
    //用户从按下触摸屏、快速移动到松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
