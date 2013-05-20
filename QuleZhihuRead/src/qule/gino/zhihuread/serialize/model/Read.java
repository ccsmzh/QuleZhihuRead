package qule.gino.zhihuread.serialize.model;

import java.io.Serializable;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import qule.gino.zhihuread.utils.Utils;

import android.text.Html;
import android.util.Log;

public class Read implements Serializable {

	private static final String TAG = "Read";

	private static final String ANSWER_USER_SIGNATURES = "answer_user_signatures";
	public static final String ANSWER_CONTENT = "answer_content";
	public static final String ANSWER_CONTENT_NOHTML = "answer_content_nohtml";
	public static final String ANSWER_VOTE_NUMS = "answer_vote_nums";
	private static final String ANSWER_ID = "answer_id";

	private static final String ANSWER_USER_ID = "answer_user_id";
	public static final String ANSWER_USER_NAME = "answer_user_name";
	private static final String ANSWER_USER_ALIAS = "answer_user_alias";
	private static final String ANSWER_USER_HEADER_IMG = "answer_user_header_img";

	public static final String QUESTION_TITLE = "question_title";
	public static final String QUESTION_CONTENT = "question_content";
	public static final String QUESTION_ID = "question_id";
	private static final String QUESTION_FOLLOWER_COUNT = "question_follower_count";
	private static final String QUESTION_ANSWER_COUNT = "question_answer_count";
	private static final String QUESTION_READ_COUNT = "question_read_count";

	private int answerUserId;
	private String answerUserSignatures;
	private HashMap<String, Object> read = new HashMap<String, Object>();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Read(JSONArray jsonArray) {
		this.deserialize(jsonArray);
	}

	public HashMap<String, Object> getRead() {
		return read;
	}

	// public void deserialize(JSONArray object) {
	// // JsonReader reader = new JsonReader(new StringReader(jsonStr));
	// // try {
	// // reader.beginArray();
	// // answerUserId = reader.nextInt();
	// // answerUserSignatures = reader.nextString();
	// // reader.endArray();
	// // } catch (IOException e) {
	// // // TODO Auto-generated catch block
	// // e.printStackTrace();
	// // }
	// // Iterator<Entry<String, JsonElement>> iter = object.entrySet().iterator();
	// // while (iter.hasNext()) {
	// // type type = (type) en.nextElement();
	// // answerUserId = iter.next().getValue().getAsInt();
	// // answerUserSignatures = iter.next().getValue().getAsString();
	// try {
	// answerUserId = (Integer) object.get(0);
	// answerUserSignatures = (String) object.get(1);
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	//
	// // }
	// // answerUserId = ;
	// // answerUserSignatures = reader.nextString();
	// }
	public void deserialize(JSONArray object) {
		try {
			// read.put(ANSWER_USER_ID, object.get(0));
			read.put(ANSWER_USER_SIGNATURES, object.get(1));
			read.put(ANSWER_CONTENT, object.get(2));
			read.put(ANSWER_CONTENT_NOHTML, Html.fromHtml((String) object.get(2)).toString());// 去除html标签
			read.put(ANSWER_VOTE_NUMS, object.get(3));
			read.put(ANSWER_ID, object.get(5));

			if (object.get(6) instanceof Integer) {
				Log.d(TAG, " - deserialize - Error - " + object.get(5));
				Log.d(TAG, " - deserialize - Error - " + object.get(6));
				Log.d(TAG, " - deserialize - Error - " + object.toString());

				read.put(ANSWER_USER_NAME, "匿名用户");
				read.put(ANSWER_USER_ALIAS, null);
				read.put(ANSWER_USER_HEADER_IMG, null);
			} else {
				JSONArray answerUser = (JSONArray) object.get(6);
				read.put(ANSWER_USER_NAME, answerUser.get(0));
				read.put(ANSWER_USER_ALIAS, answerUser.get(1));
				read.put(ANSWER_USER_HEADER_IMG, answerUser.get(2));
			}

			JSONArray quesion = (JSONArray) object.get(7);
			read.put(QUESTION_TITLE, quesion.get(1));
			read.put(QUESTION_CONTENT, quesion.get(2));
			// read.put(ANSWER_CONTENT_NOHTML, Utils.stripHtml((String) quesion.get(2)));//去除html标签
			read.put(QUESTION_ID, quesion.get(3));
			read.put(QUESTION_FOLLOWER_COUNT, quesion.get(4));
			read.put(QUESTION_ANSWER_COUNT, quesion.get(5));
			read.put(QUESTION_READ_COUNT, quesion.get(6));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
