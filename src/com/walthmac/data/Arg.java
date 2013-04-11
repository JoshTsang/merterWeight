package com.walthmac.data;

import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.walthmac.constant.ErrorNum;
import com.walthmac.constant.Server;
import com.walthmac.lib.Http;

public class Arg {
	public final static String TAG = "Arg";
	private Hashtable<String, Double> args = new Hashtable<String, Double>();
	private Hashtable<String, String> names = new Hashtable<String, String>();
	
	public Arg() {
		names.put("ExtrAlgLen", "过滤系数");
		names.put("ExtrAlgLendis", "控制系数");
		names.put("extrpGain", "P参数");
		names.put("extriGain", "I参数");
		names.put("extrDeadBand", "P控制死区");
		names.put("WgtPerHWarnUp", "挤出量报警上限");
		names.put("WgtPerHWarnDw", "挤出量报警下限");
		
		names.put("calFrq", "过滤系数");
		names.put("calFrqdis", "控制系数");
		names.put("dragpGain", "P参数");
		names.put("dragiGain", "I参数");
		names.put("dragDeadBand", "P控制死区");
		names.put("kgPerMeterWarnUp", "米重报警上限");
		names.put("kgPerMeterWarnDw", "米重报警下限");
	}
	
	public int load() {
		String response;
		response = Http.get(Server.ARG, "");
		if (response != null) {
			try {
				JSONArray statusList;
				statusList = new JSONArray(response);
				int length = statusList.length();
				for (int i = 0; i < length; i++) {
					JSONObject item = statusList.getJSONObject(i);
					args.put(item.getString("id"), item.getDouble("value"));
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
	
	public double get(String key) {
		Double value = args.get(key);
		if (value == null) {
			return -1;
		} else {
			return value;
		}
	}
	
	public String getName(String key) {
		return names.get(key);
	}
	public int set(String key, double value) {
		args.put(key, value);
		return 0;
	}
	
	public void test() {
		if (load() < 0) {
			Log.d(TAG, "load failed");
			return;
		}
		
		Log.d(TAG, "args: " + args.toString());
	}
}
