package qule.gino.zhihuread.ui;

import qule.gino.zhihuread.R;

import com.actionbarsherlock.app.ActionBar;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.qule_zhihu_read_icon);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

		MobclickAgent.updateOnlineConfig(this);
		UmengUpdateAgent.update(this);
		// UMFeedbackService.enableNewReplyNotification(this, NotificationType.NotificationBar);

		Intent intent = new Intent();
		intent.setClass(SplashActivity.this, MainActivity.class);
		startActivity(intent);

		finish();
	}

}
