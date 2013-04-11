package com.walthmac.constant;

public class Server {
	public final static String SERVER_IP = "192.168.0.2";
	public final static String SERVER_DOMIN = "http://" + SERVER_IP;
	
	public final static String FTP_USERNAME = "root";
	public final static String FTP_PWD = "123456";
	public final static String FTP_DB_DIR = "db";
	
	/* php */
	public final static String PHP_DIR = "walth/api/";
	public final static String STATUS = PHP_DIR + "status.php";
	public final static String ALERT = PHP_DIR + "alerts.php";
	public final static String USER = PHP_DIR + "users.php";
	public final static String ARG = PHP_DIR + "args.php";
	public final static String LOGIN = PHP_DIR + "login.php";
	
}
