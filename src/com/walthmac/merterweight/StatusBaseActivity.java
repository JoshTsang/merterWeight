package com.walthmac.merterweight;

import java.util.Timer;
import java.util.TimerTask;

import com.walthmac.data.Status;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class StatusBaseActivity extends BaseActivity {
	public final static String TAG = "StatusBaseActivity";
	
	public final static int LOAD_DATA = 0;
	public final static int REFRESH_UI = 1;
	public final static int NETWORK_ERR = 2;
	
	protected Status status = new Status();
	private final Timer timer = new Timer();
	private TimerTask task;
	
	protected Toast netWorkErr;
	
	public void updateData() {};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		netWorkErr = Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initDataRefreshTask();
	}

	private void initDataRefreshTask() {
		if (task != null) {
			task.cancel();
		}
		task = new TimerTask() {
			
		    @Override
		    public void run() {
		        handler.sendEmptyMessage(LOAD_DATA);
		    }
		};
		timer.schedule(task, 0, 1000);
	}
	
	private void stopDataRefreshTask() {
		timer.cancel();
	}
	
	Handler handler = new Handler() {
		
	    @Override
	    public void handleMessage(Message msg) {
	    	switch (msg.what) {
			case LOAD_DATA:
				new Thread() {
					public void run() {
						if (status.load() >= 0) {
							handler.sendEmptyMessage(REFRESH_UI);
						} else {
							handler.sendEmptyMessage(NETWORK_ERR);
						}
					}
				}.start();
				break;
			case REFRESH_UI:
				updateData();
				break;
			case NETWORK_ERR:
				netWorkErr.cancel();
				netWorkErr.show();
				break;
			default:
				break;
			}
	    	
	        super.handleMessage(msg);
	    }
	};
}
