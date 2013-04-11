package com.walthmac.merterweight;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.walthmac.constant.ErrorNum;
import com.walthmac.data.User;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity{

	private EditText name;
	private EditText pwd;
	private Button loginBtn;
	private Button resetBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		findViews();
		setOnClickListener();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		TextView date = (TextView) findViewById(R.id.date);

		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);
		String now = format.format(new Date());
		date.setText(now);
	}

	private void findViews() {
		name = (EditText) findViewById(R.id.name);
		name.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		pwd = (EditText) findViewById(R.id.pwd);
		pwd.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		resetBtn = (Button) findViewById(R.id.loginReset);
		resetBtn.setFocusableInTouchMode(true);
		resetBtn.requestFocus();
	}
	
	private void setOnClickListener() {
		loginBtn.setOnClickListener(loginClicked);
		resetBtn.setOnClickListener(resetClicked);
	}
	
	private OnClickListener loginClicked = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			final String uname = name.getText().toString();
			final String upwd = pwd.getText().toString();
			//TODO for debug
			Intent intent = new Intent();
			intent.setClass(Login.this, MainActivity.class);
			startActivity(intent);
			finish();
			
			if ("".equals(uname) || "".equals(upwd)) {
				Toast.makeText(getApplicationContext(), "用户名密码不能为空", Toast.LENGTH_LONG).show();
			} else {
				new Thread() {
					public void run() {
						User user = new User();
						handler.sendEmptyMessage(user.login(uname, upwd));
					}
				}.start();
			}
		}
	};
	
	private OnClickListener resetClicked = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			name.setText("");
			pwd.setText("");
		}
	};
	
	private Handler handler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			if (msg.what >= 0) {
				Intent intent = new Intent();
				intent.setClass(Login.this, MainActivity.class);
				startActivity(intent);
				finish();
			} else {
				String err = "未知错误";
				if (msg.what == ErrorNum.LOGIN_PWD_ERR || msg.what == ErrorNum.LOGIN_USER_NOT_EXIST) {
					err = "用户名密码不正确";
				}
				Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();;
			}
		}
	};
}
