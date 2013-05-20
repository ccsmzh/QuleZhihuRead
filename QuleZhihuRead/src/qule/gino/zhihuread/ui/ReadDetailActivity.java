package qule.gino.zhihuread.ui;

import java.util.HashMap;

import qule.gino.zhihuread.R;
import qule.gino.zhihuread.serialize.model.Read;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;

public class ReadDetailActivity extends BaseActivity {
	private HashMap<String, Object> mData;
	private WebView mContentWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.read_detail);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.qule_zhihu_read_icon);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.setDisplayHomeAsUpEnabled(true);

		mContentWebView = (WebView) findViewById(R.id.rd_wv_content);
		mContentWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int mDensity = metrics.densityDpi;

		if (mDensity == 120) {
			mContentWebView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
		} else if (mDensity == 160) {
			mContentWebView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
		} else if (mDensity == 240) {
			mContentWebView.getSettings().setDefaultZoom(ZoomDensity.FAR);
		}

		mData = (HashMap<String, Object>) getIntent().getSerializableExtra("data");

		if (mData != null) {
			mContentWebView.loadDataWithBaseURL(null, (String) mData.get(Read.ANSWER_CONTENT), "text/html",
					"utf-8", null);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

}
