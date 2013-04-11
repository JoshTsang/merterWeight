package com.walthmac.data;

import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.util.SparseArray;

import com.walthmac.constant.ErrorNum;
import com.walthmac.constant.Server;
import com.walthmac.lib.Http;

public class User {
	public final static String TAG = "User";
	
	private static int uid;
	private static int permission;
	private static String uname;
	
	public class UserRow {
		int id;
		String name;
		String pwd;
		int permission;
		
		public UserRow(int id, String name, int permission) {
			this.id = id;
			this.name = name;
			this.permission = permission;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String n) {
			name = n;
		}
		
		public int getId() {
			return id;
		}
		
		public int getPermission() {
			return permission;
		}
		
		public void setPermission(int p) {
			permission = p;
		}
		
		public String getPwd() {
			return this.pwd;
		}
		
		public void setPwd(String pwd) {
			this.pwd = pwd;
		}
		
		public String getPermissionString() {
			String p;
			switch (permission) {
			case 0:
				p = "浏览权限";
				break;
			case 1:
				p = "操作权限";
				break;
			case 2:
				p = "管理权限";
				break;
			case 3:
				p = "超级用户";
				break;
			default:
				p = "错误的权限";
				break;
			}
			
			return p;
		}
		
		public String toString() {
			return name + "#" + id + "$" + permission;
		}
	}
	
	private static SparseArray<UserRow> users = new SparseArray<UserRow>();
	
	public int load() {
		String response;
		users.clear();
		response = Http.get(Server.USER, "");
		if (response != null) {
			try {
				JSONArray userList;
				userList = new JSONArray(response);
				int length = userList.length();
				for (int i = 0; i < length; i++) {
					JSONObject item = userList.getJSONObject(i);
					users.put(item.getInt("id"), new UserRow(item.getInt("id"), item.getString("name"), item.getInt("permission")));
				}
			} catch (JSONException e) {
				Log.e(TAG, response);
				e.printStackTrace();
				return ErrorNum.PARSE_JSON_ERR;
			}
			
			return 0;
		} else {
			Log.e(TAG, "response == null");
			return ErrorNum.HTTP_TIMEOUT;
		}
	}
	
	public UserRow newInstanceOfUserRow() {
		return new UserRow(-1, "", -1);
	}
	
	public int login(String name, String pwd) {
		String response;
		JSONObject user = new JSONObject();
		try {
			user.put("name", name);
			user.put("passwd", getMD5(pwd));
			response = Http.post(Server.LOGIN, "user", user.toString());
			Log.d(TAG, response);
			
			JSONObject ret = new JSONObject(response);
			if (ret.has("username")) {
				uid = ret.getInt("uid");
				uname = ret.getString("username");
				permission = ret.getInt("permission");
				return 0;
			} else {
				return -ret.getInt("err_code");
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return ErrorNum.ENCODE_JSON_ERR;
		}
	}
	
	public int add(String username, String pwd, int permission) {
		String response;
		JSONObject user = new JSONObject();
		try {
			user.put("name", username);
			user.put("pwd", pwd);
			user.put("permission", permission);
			response = Http.post(Server.USER + "?do=add", "user", user.toString());
			Log.d(TAG, response);
			//TODO check response
		} catch (JSONException e) {
			e.printStackTrace();
			return ErrorNum.ENCODE_JSON_ERR;
		}
		
		return 0;
	}
	
	public int update(int id, String name, String pwd, int permission) {
		String response;
		JSONObject user = new JSONObject();
		try {
			user.put("id", id);
			if (name != null) {
				user.put("name", name);
			}
			if (pwd != null) {
				user.put("pwd", pwd);
			}
			
			if (permission >= 0) {
				user.put("permission", permission);
			}
			
			response = Http.post(Server.USER + "?do=update", "user", user.toString());
			Log.d(TAG, response);
			//TODO check response
		} catch (JSONException e) {
			e.printStackTrace();
			return ErrorNum.ENCODE_JSON_ERR;
		}
		return 0;
	}
	
	public int delete(int id) {
		String response;
		JSONObject user = new JSONObject();
		try {
			user.put("id", id);
			response = Http.post(Server.USER + "?do=delete", "user", user.toString());
			Log.d(TAG, response);
			//TODO check response
		} catch (JSONException e) {
			e.printStackTrace();
			return ErrorNum.ENCODE_JSON_ERR;
		}
		
		return 0;
	}
	
	public UserRow getById(int id) {
		return users.get(id);
	}
	
	public UserRow get(int index) {
		return users.valueAt(index);
	}
	
	public int size() {
		return users.size();
	}
	
	public int getCurUID() {
		return uid;
	}
	
	public int getCurPermission() {
		return permission;
	}
	
	public String getCurUname() {
		return uname;
	}
	private String getMD5(String src) {
		String s = null;
		byte[] source = src.getBytes();
	    char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',  'E', 'F'}; 
		try
		{
		    java.security.MessageDigest md = java.security.MessageDigest.getInstance( "MD5" );
			md.update(source);
			byte tmp[] = md.digest();         
			char str[] = new char[16 * 2];   
			
			int k = 0;                                
			for (int i = 0; i < 16; i++) {
				 byte byte0 = tmp[i];
				 str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				                                        
				 str[k++] = hexDigits[byte0 & 0xf];    
			} 
			s = new String(str);
		
	   	}catch( Exception e ) {
	   		e.printStackTrace();
	    }
	   	return s;
	}
	
	public void test() {
		if (load() < 0) {
			Log.d(TAG, "load failed");
			return;
		}
		
		Log.d(TAG, "users: " + users.toString());
		login("admin", "admin");
	}
}
