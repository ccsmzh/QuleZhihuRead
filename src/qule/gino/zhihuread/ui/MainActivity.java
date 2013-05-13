package qule.gino.zhihuread.ui;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import qule.gino.zhihuread.BuildConfig;
import qule.gino.zhihuread.R;
import qule.gino.zhihuread.http.HttpManger;
import qule.gino.zhihuread.serialize.model.Read;
import qule.gino.zhihuread.serialize.msg.ReadResponseMsg;
import qule.gino.zhihuread.utils.AsyncTask;
import qule.gino.zhihuread.utils.Utils;
import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends BaseActivity implements LoaderCallbacks<List<HashMap<String, Object>>> {
	private static final String TAG = "MainActivity";
	private int mCurrentItem = 0;

	/**
	 * loader id
	 */
	private static final int LOADER_REFERSH_ID = 0;
	private static final int LOADER_MORE_ID = 1;

	private ListView mContentListView;
	private SimpleAdapter mSimpleAdapter;
	private List<HashMap<String, Object>> mDatas;
	private MenuItem mRefreshItem;

	private View mLoadingProgressBar;

	private boolean isMore = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.qule_zhihu_read_icon);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

		mLoadingProgressBar = findViewById(R.id.m_pb_loading);

		mContentListView = (ListView) findViewById(R.id.m_lv_contesnt);
		mContentListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ReadDetailActivity.class);
				intent.putExtra("data", mDatas.get(position));
				startActivity(intent);
			}
		});
		mContentListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& view.getLastVisiblePosition() == (view.getCount() - 1)) {
					if (!isMore) {
						Toast.makeText(MainActivity.this, "加载更多...", Toast.LENGTH_SHORT).show();
						isMore = true;
						startRefreshItemAnimation();
						// loadReadData(false);
						getSupportLoaderManager().initLoader(LOADER_MORE_ID, null, MainActivity.this);
						getSupportLoaderManager().getLoader(LOADER_MORE_ID).forceLoad();
					}

				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
					int totalItemCount) {
			}
		});
		showProgress(true, true);
		getSupportLoaderManager().initLoader(LOADER_REFERSH_ID, null, this);
		getSupportLoaderManager().getLoader(LOADER_REFERSH_ID).forceLoad();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		mRefreshItem = menu.findItem(R.id.menu_main_refresh);
		startRefreshItemAnimation();
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_main_refresh:
			showProgress(true, false);
			startRefreshItemAnimation();
			getSupportLoaderManager().initLoader(LOADER_REFERSH_ID, null, this);
			getSupportLoaderManager().getLoader(LOADER_REFERSH_ID).forceLoad();
//			loadReadData(true);
			return true;
		case R.id.menu_settings:
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, SettingActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	public static class ReadListLoader extends AsyncTaskLoader<List<HashMap<String, Object>>> {
		public boolean isFirst;
		private int mCurrentItem = 0;

		public ReadListLoader(Context context, boolean isFirst) {
			super(context);
			this.isFirst = isFirst;
		}

		@Override
		public List<HashMap<String, Object>> loadInBackground() {
			List<HashMap<String, Object>> datas = null;
			if (isFirst) {
				mCurrentItem = 0;// 还原页标
			}
			HttpManger httpManger = new HttpManger();
			byte[] results = httpManger.httpGet("http://www.zhihu.com/reader/json/" + ++mCurrentItem);
			try {
				ReadResponseMsg respMsg = new ReadResponseMsg();
				respMsg.deserialize(new String(results, "UTF-8"));
				datas = respMsg.getReads();
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, e.toString());
			}
			return datas;
		}

	}

	private void startRefreshItemAnimation() {
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.rotate_360_loop);
		LayoutInflater inflater = (LayoutInflater) MainActivity.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ImageView refreshView = (ImageView) inflater.inflate(R.layout.refresh_action_view, null);
		refreshView.startAnimation(anim);
		mRefreshItem.setActionView(refreshView);
	}

	private void showProgress(boolean isShow, boolean isHide) {
		int mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

		final View showView = isShow ? mLoadingProgressBar : mContentListView;
		final View hideView = isShow ? mContentListView : mLoadingProgressBar;
		if (Utils.hasHoneycombMR1()) {
			showView.setAlpha(0f);
			showView.setVisibility(View.VISIBLE);

			showView.animate().alpha(1f).setDuration(mShortAnimationDuration).setListener(null);
			if (isHide) {
				hideView.animate().alpha(0f).setDuration(mShortAnimationDuration)
						.setListener(new android.animation.AnimatorListenerAdapter() {
							public void onAnimationEnd(Animator animation) {
								hideView.setVisibility(View.GONE);
							}
						});
			}

		} else {
			showView.setVisibility(View.VISIBLE);
			hideView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onLoadFinished(Loader<List<HashMap<String, Object>>> load, List<HashMap<String, Object>> data) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "- onLoadFinished - isRunning -");
		}

		switch (load.getId()) {
		case LOADER_REFERSH_ID:
		case LOADER_MORE_ID:
			if (data == null || data.size() == 0) {
				showProgress(false, true);
			} else if (((ReadListLoader) load).isFirst) {
				mDatas = data;
				mSimpleAdapter = new SimpleAdapter(
						MainActivity.this,
						mDatas,
						R.layout.main_item,
						new String[] { Read.QUESTION_TITLE, Read.ANSWER_USER_NAME, Read.ANSWER_CONTENT_NOHTML },
						new int[] { R.id.mi_tv_quesiontitle, R.id.mi_tv_answer_author,
								R.id.mi_tv_answer_content });
				mContentListView.setAdapter(mSimpleAdapter);
				showProgress(false, true);
			} else {
				mDatas.addAll(data);
				mSimpleAdapter.notifyDataSetChanged();
				if (mRefreshItem != null && mRefreshItem.getActionView() != null) {
					mRefreshItem.getActionView().clearAnimation();
					mRefreshItem.setActionView(null);
				}
				isMore = false;
			}

			if (mRefreshItem != null && mRefreshItem.getActionView() != null) {
				mRefreshItem.getActionView().clearAnimation();
				mRefreshItem.setActionView(null);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onLoaderReset(Loader<List<HashMap<String, Object>>> arg0) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "- onLoaderReset - isRunning -");
		}
	}

	@Override
	public Loader<List<HashMap<String, Object>>> onCreateLoader(int id, Bundle arg1) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "- onCreateLoader - isRunning -");
		}
		switch (id) {
		case LOADER_REFERSH_ID:
			return new ReadListLoader(this, true);
		case LOADER_MORE_ID:
			return new ReadListLoader(this, false);
		default:
			return null;
		}
	}

}
