package com.walthmac.data;

import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.util.Log;

import com.walthmac.constant.ErrorNum;
import com.walthmac.constant.Server;
import com.walthmac.lib.ErrorPHP;
import com.walthmac.lib.Http;

public class Status {
	public final static String TAG = "Status";
	private static Hashtable<String, Double> status = new Hashtable<String, Double>();
	private static int err;
	
	public int load() {
		String response;
		response = Http.get(Server.STATUS, "");
		if (response != null) {
			try {
				JSONArray statusList;
				statusList = new JSONArray(response);
				int length = statusList.length();
				for (int i = 0; i < length; i++) {
					JSONObject item = statusList.getJSONObject(i);
					status.put(item.getString("id"), item.getDouble("value"));
				}
				err = 0;
			} catch (JSONException e) {
				Log.e(TAG, response);
				e.printStackTrace();
				err = ErrorNum.PARSE_JSON_ERR;
			}
			
		} else {
			Log.e(TAG, "response == null");
			err = -1;
		}
		return err;
	}
	
	public double get(String key) {
		Double value = status.get(key);
		if (value == null) {
			return -1;
		} else {
			return value;
		}
	}
	
	public int set(String key, double value) {
		return 0;
	}
	
	public int getErr() {
		return err;
	}
	
	public void test() {
		if (load() < 0) {
			Log.d(TAG, "load failed");
			return;
		}
		
		Log.d(TAG, "status: " + status.toString());
	}
}
