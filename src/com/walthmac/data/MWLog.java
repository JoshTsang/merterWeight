package com.walthmac.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.walthmac.constant.ErrorNum;
import com.walthmac.constant.Server;
import com.walthmac.lib.Http;

public class MWLog {
	public final static String TAG = "MWLog";
	
	public class LogRow {
		String who;
		String when;
		String what;
		
		public LogRow(String uname, String time, String action) {
			who = uname;
			when = time;
			what = action;
		}
		
		public String getName() {
			return who;
		}
		
		public String getTime() {
			return when;
		}
		
		public String getAction() {
			return what;
		}
	}
	
	private ArrayList<LogRow> log = new ArrayList<MWLog.LogRow>();
	
	public int load() {
		String response;
		response = Http.get(Server.USER, "do=log");
		if (response != null) {
			try {
				JSONArray logList;
				logList = new JSONArray(response);
				int length = logList.length();
				for (int i = 0; i < length; i++) {
					JSONObject item = logList.getJSONObject(i);
					log.add(new LogRow(item.getString("who"), item.getString("when"), item.getString("what")));
				}
			} catch (JSONException e) {
				Log.e(TAG, response);
				e.printStackTrace();
				return ErrorNum.PARSE_JSON_ERR;
			}
			
			return 0;
		} else {
			Log.e(TAG, "response == null");
			return -1;
		}
	}
	
	public LogRow get(int index) {
		return log.get(index);
	}
	
	public int size() {
		return log.size();
	}
}
