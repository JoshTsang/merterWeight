package com.walthmac.data;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.walthmac.constant.ErrorNum;
import com.walthmac.constant.Server;
import com.walthmac.lib.Http;

public class Alert {
	public final static String TAG = "Alert";
	
	public class AlertRow {
		int type;
		String msg;
		String time;
		
		public AlertRow(int type, String msg, String timestamp) {
			this.msg = msg;
			time = timestamp;
			this.type = type;
		}
		
		public String toString() {
			return msg + "@" + time;
		}
		
		public String getType() {
			return type==0?"O":"X"; 
		}
		
		public String getTime() {
			return time;
		}
		
		public String getAlert() {
			return msg;
		}
	}
	
	private ArrayList<AlertRow> alerts = new ArrayList<AlertRow>();
	
	public int load() {
		String response;
		response = Http.get(Server.ALERT, "");
		if (response != null) {
			try {
				JSONArray alertList;
				alertList = new JSONArray(response);
				int length = alertList.length();
				for (int i = 0; i < length; i++) {
					JSONObject item = alertList.getJSONObject(i);
					alerts.add(new AlertRow(item.getInt("type"), item.getString("alert"), item.getString("timestamp")));
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
	
	public int size() {
		return alerts.size();
	}
	
	public AlertRow get(int position) {
		if (position > alerts.size()) {
			return null;
		} else {
			return alerts.get(position);
		}
	}
	
	public void test() {
		if (load() < 0) {
			Log.d(TAG, "load failed");
			return;
		}
		
		Log.d(TAG, "alerts: " + alerts.toString());
	}
}
