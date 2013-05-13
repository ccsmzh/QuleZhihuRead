package qule.gino.zhihuread.ui;

import qule.gino.zhihuread.R;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.umeng.fb.UMFeedbackService;
import com.umeng.update.UmengUpdateAgent;

public class SettingActivity extends SherlockPreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.qule_zhihu_read_icon);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

		addPreferencesFromResource(R.xml.setting);

		getListView().setBackgroundColor(Color.WHITE);

		// 当前版本
		PreferenceScreen versionPreferenceScreen = (PreferenceScreen) findPreference(getResources()
				.getString(R.string.pref_key_version));
		String versionName;
		try {
			versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			versionPreferenceScreen.setSummary(versionName);
		} catch (NameNotFoundException e) {
		}
		versionPreferenceScreen.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				UmengUpdateAgent.update(SettingActivity.this);
				return false;
			}
		});

		// 意见反馈
		PreferenceScreen fedbackPreferenceScreen = (PreferenceScreen) findPreference(getResources()
				.getString(R.string.pref_key_fedback));
		fedbackPreferenceScreen.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				UMFeedbackService.openUmengFeedbackSDK(SettingActivity.this);
				return false;
			}
		});

		// 访问微博
		PreferenceScreen weiboPreferenceScreen = (PreferenceScreen) findPreference(getResources().getString(
				R.string.pref_key_author_weibo));
		weiboPreferenceScreen.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Uri uri = Uri.parse(getResources().getString(R.string.author_weibo_url));
				// 通过Uri获得编辑框里的//地址，加上http://是为了用户输入时可以不要输入
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				// 建立Intent对象，传入uri
				startActivity(intent);
				return false;
			}
		});
	}

}
