package qule.gino.zhihuread.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import qule.gino.zhihuread.R;

/**
 * Created by zhuohong on 13-5-23.
 */
public class AppMessage{
    private Context mContext;
    private View mView;

    public AppMessage(Context context){
        this.mContext = context;
    }

    public static AppMessage makeText(Context context){
        AppMessage appMessage = new AppMessage(context);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.refresh_listview_msg_item,null);
        appMessage.setView(view);
        return appMessage;
    }

    public void setText(int contestResId){
        setText(mContext.getText(contestResId));
    }

    public void setText(CharSequence contentStr){
        if(mView == null){
            return;
        }

        TextView textView = (TextView) mView.findViewById(R.id.rlmi_tv_content);

        if(textView != null){
            return;
        }

        textView.setText(contentStr);
    }

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        this.mView = view;
    }

    public void show(){
        AppMessageUtil.getInstance().addMsg(this);
    }
}
