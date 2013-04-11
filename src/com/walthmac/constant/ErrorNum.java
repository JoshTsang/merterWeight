package com.walthmac.constant;

public class ErrorNum {
	public final static int SERVER_NO_RESPOND = -1;
	public final static int PARSE_JSON_ERR = -2;
	public final static int ENCODE_JSON_ERR = -3;
	
	/* API errors */
	public final static int LOGIN_USER_NOT_EXIST = -410;
	public final static int LOGIN_PWD_ERR = -411;

	/* Http errors */
	public final static int HTTP_TIMEOUT = -400;
	public final static int HTTP_PAGE_NOT_FOUND = -404;
	public final static int HTTP_INTERNAL_ERR = -500;
	public final static int HTTP_NO_CONECTION = -401;
	public final static int UTF8_NOT_SUPPORTED = -402;
	public final static int FETCH_DATA_FAILED = -403;
	
	public static String getErrMsg(int err) {
		switch (err) {
		case HTTP_TIMEOUT:
		case HTTP_PAGE_NOT_FOUND:
		case HTTP_INTERNAL_ERR:
		case HTTP_NO_CONECTION:
			return "网络错误！请检查网络连接";
		case PARSE_JSON_ERR:
			return "数据错误，请重试";
		default:
			return "未知错误";
		}
	}
}
