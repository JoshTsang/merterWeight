package com.walthmac.merterweight;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

public class BaseActivity extends Activity {
	public final static String TAG = "BaseActivity";
	
	protected TextView title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		TextView more = (TextView) findViewById(R.id.more);
		TextView date = (TextView) findViewById(R.id.date);
		Button alertBtn = (Button) findViewById(R.id.alert);
		title = (TextView) findViewById(R.id.title);
		more.setOnClickListener(moreClicked);

		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);
		String now = format.format(new Date());
		date.setText(now);
		alertBtn.setOnClickListener(alertClicked);
	}

	
	private OnClickListener moreClicked = new OnClickListener() {

		@Override
		public void onClick(View v) {
			PopupMenu popup = new PopupMenu(getBaseContext(), v);
	        popup.getMenuInflater().inflate(R.menu.more, popup.getMenu());
	        popup.setOnMenuItemClickListener(popupMenuClicked);

	        popup.show();
		}
	};
	
	private OnClickListener alertClicked = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			luanchActivity(AlertActivity.class);
		}
	};
	
	protected void luanchActivity(Class<?> setClass) {
		Log.d(TAG, getClass().getName() + "#" + setClass.getName());
		if (getClass().getName().equals(setClass.getName())) {
			return;
		}
		Intent intent = new Intent();
		intent.setClass(BaseActivity.this, setClass);
		startActivity(intent);
		Log.d(TAG, getLocalClassName());
		if (!getLocalClassName().equals("MainActivity")) {
			finish();
		}
	}
	
	private PopupMenu.OnMenuItemClickListener popupMenuClicked = new PopupMenu.OnMenuItemClickListener() {
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
			case R.id.menu_alert:
				luanchActivity(AlertActivity.class);
				break;
			case R.id.menu_arg:
				luanchActivity(ArgActivity.class);
				break;
			case R.id.menu_produce:
				luanchActivity(ProduceActivity.class);
				break;
			case R.id.menu_machine:
				luanchActivity(MachineActivity.class);
				break;
			case R.id.menu_user:
				luanchActivity(UserActivity.class);
				break;
			case R.id.menu_log:
				luanchActivity(LogActivity.class);
				break;
			case R.id.menu_camara:
				luanchActivity(CamaraActivity.class);
				break;
			default:
				break;
            }
            return true;
        }
    };
}
