package com.walthmac.lib;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.walthmac.constant.ErrorNum;
import com.walthmac.constant.Limit;
import com.walthmac.constant.Server;

/**
 * @author josh
 * 
 */
public class Http {
	final static String TAG = "Http";
	private static int mErrno;
	
	public static String get(String page, String param) {
		String ret;
		
		for (int i=0; i<Limit.RETRY; i++) {
			ret = httpRequestGet(page, param);
			if (ret != null) {
				return ret;
			}
		}
		
		return null;
	}
	
	public static String post(String page, String argName, String argValue) {
		String ret;
		
		for (int i=0; i<Limit.RETRY; i++) {
			ret = httpRequestPost(page, argName, argValue);
			if (ret != null) {
				return ret;
			}
		}
		
		return null;
	}
	
	private static String httpRequestPost(String page, String argName, String argValue) {
		HttpParams httpParameters1 = new BasicHttpParams();

		// 超时设置
		HttpConnectionParams.setConnectionTimeout(httpParameters1, 15 * 1000);
		HttpConnectionParams.setSoTimeout(httpParameters1, 60 * 1000);

		List<NameValuePair> ls = constructHttpPkg(argName, argValue);

		String url = Server.SERVER_DOMIN + "/" + page;

		DefaultHttpClient client;
		client = new DefaultHttpClient(httpParameters1);
		
		if ("".equals(url)) {
			mErrno = ErrorNum.HTTP_NO_CONECTION;
			return null;
		}

		HttpPost post = new HttpPost(url);
		post.setHeader("User-Agent", "Mozilla/4.5");
		post.setHeader("connection", "Keep-Alive");
		post.setHeader("Accept-Language", "zh-cn,zh;q=0.5");

		try {
			post.setEntity(new UrlEncodedFormEntity(ls, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			mErrno = ErrorNum.UTF8_NOT_SUPPORTED;
			return null;
		}

		HttpResponse httpResponse = null;
		try {
			httpResponse = client.execute(post);
		} catch (IOException e) {
			e.printStackTrace();
			mErrno = ErrorNum.HTTP_NO_CONECTION;
			return null;
		}

		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (statusCode == 200) {
			try {
				return EntityUtils.toString(httpResponse.getEntity());
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} else {
	    	mErrno = -statusCode;
	    	Log.e(TAG, Integer.toString(mErrno) + ",url:" + url);
			return null;
		}
	}
		
	private static String httpRequestGet(String page, String param) {
		/*声明网址字符串*/
		String uriAPI = Server.SERVER_DOMIN + "/" + page + "?" + param;
		/*建立HTTP Get联机*/
		HttpGet httpRequest = new HttpGet(uriAPI); 
		
		HttpParams httpParameters1 = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters1, 5 * 1000);
		HttpConnectionParams.setSoTimeout(httpParameters1, 15 * 1000);
		
		DefaultHttpClient client;
		client = new DefaultHttpClient(httpParameters1);
		
		try 
		{
			HttpResponse httpResponse = client.execute(httpRequest); 
		
			if(httpResponse.getStatusLine().getStatusCode() == 200) { 
				return EntityUtils.toString(httpResponse.getEntity());
			} else {
				Log.d(TAG, "http err:" + httpResponse.getStatusLine().getStatusCode());
				return null;
			} 
		} 
		catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} 
		catch (IOException e) {
			e.printStackTrace();
			return null;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}  
	}


	private static List<NameValuePair> constructHttpPkg(String argName, String argValue) {
		Map<String, String> params = new HashMap<String, String>();
		String param = "";
		
		try {
			param = new String(argValue.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		params.put(argName, param);

		List<NameValuePair> ls = new ArrayList<NameValuePair>();
		Set<String> keys = params.keySet();
		for (String key : keys) {
			ls.add(new BasicNameValuePair(key, params.get(key).toString()));
		}
		return ls;
	}
}
