package qule.gino.zhihuread.ui;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPause(this);
	}

}
