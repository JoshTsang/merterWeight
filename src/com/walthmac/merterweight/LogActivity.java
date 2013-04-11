package com.walthmac.merterweight;

import com.walthmac.data.Alert;
import com.walthmac.data.MWLog;
import com.walthmac.merterweight.AlertActivity.ItemViewHolder;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LogActivity extends BaseActivity {

public final static String TAG = "AlertActivity";
	
	private ListView logList;
	private BaseAdapter logListAdapter;
	private MWLog log = new MWLog();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log);
		findViews();
		setOnClickListener();
		fetchData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		title.setText("日志页面");
	}
	
	private void findViews() {
		logList = (ListView) findViewById(R.id.logList);
	}
	
	private void setOnClickListener() {
		
	}
	
	private void fetchData() {
		new Thread() {
			public void run() {
				handler.sendEmptyMessage(log.load());
			}
		}.start();
	}

	private BaseAdapter getAdapterInstance() {
		return new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ItemViewHolder itemViewHolder;
				if (convertView == null) {
					convertView = LayoutInflater.from(LogActivity.this)
							.inflate(R.layout.row_log, null);
					itemViewHolder = new ItemViewHolder(convertView);
					convertView.setTag(itemViewHolder);
				} else {
					itemViewHolder = (ItemViewHolder) convertView.getTag();
				}
				itemViewHolder.setTag(position);
				itemViewHolder.fillData(log.get(position));
				return convertView;
			}
			
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getCount() {
				return log.size();
			}
		};
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if (msg.what >= 0) {
				if (logListAdapter == null) {
					logListAdapter = getAdapterInstance();
					logList.setAdapter(logListAdapter);
				} else {
					logListAdapter.notifyDataSetChanged();
				}
			} else {
				Log.d(TAG, "load failed:" + msg.what);
			}
			super.handleMessage(msg);
		}
	};
	
	class ItemViewHolder {
		TextView name;
		TextView datetime;
		TextView action;

		public ItemViewHolder(View convertView) {
			name = (TextView) convertView.findViewById(R.id.who);
			datetime = (TextView) convertView.findViewById(R.id.when);
			action = (TextView) convertView.findViewById(R.id.what);
		}

		public void setOnClickListener() {
			
		}

		public void setTag(int position) {
			
		}

		public void fillData(MWLog.LogRow logRow) {
			name.setText(logRow.getName());
			datetime.setText(logRow.getTime());
			action.setText(logRow.getAction());
		}
	}
	
}
