package com.walthmac.lib;

import java.io.Serializable;

import org.json.JSONObject;

import android.util.Log;

public class ErrorPHP implements Serializable{
	private static final long serialVersionUID = 2L;
	private static String mSucc;
	private static String mError;
	
	public static boolean isSucc(String responsePkg,String errorTAG) {
		if(responsePkg == null){
			Log.e(errorTAG, errorTAG+".timeOut");
			return false;
		}
		try {
			JSONObject errorString = new JSONObject(responsePkg);
			String succ = errorString.getString("succ");
			if(succ.equals("true")){
				mSucc = succ;
				return true;
			}else {
				mSucc = "flase";
				mError = responsePkg;
			}
		} catch (Exception e) {
			Log.e(errorTAG, responsePkg);
			return false;
		}
		return false;
	}

	public static String getSucc(){
		return mSucc;
	}
	
	public static String getErroe(){
		return mError;
	}
}
