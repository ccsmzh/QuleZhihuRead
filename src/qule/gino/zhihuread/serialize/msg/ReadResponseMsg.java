package qule.gino.zhihuread.serialize.msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import qule.gino.zhihuread.serialize.model.Read;

public class ReadResponseMsg {
	private static final String TAG = "ReadResponseMsg";

	private List<HashMap<String, Object>> reads = new ArrayList<HashMap<String, Object>>();

	public List<HashMap<String, Object>> getReads() {
		return reads;
	}

	public void deserialize(String jsonStr) {
		try {
			JSONArray result = new JSONArray(jsonStr);
			int length = result.length();

			for (int i = 0; i < length; i++) {
				Read tempRead = new Read((JSONArray) result.get(i));
				reads.add(tempRead.getRead());
			}
		} catch (JSONException e) {
			Log.e(TAG, "- deserialize Error - " + e);
		}

	}
}
