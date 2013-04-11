package com.walthmac.merterweight;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.walthmac.data.Alert;

public class AlertActivity extends BaseActivity {
	
	public final static String TAG = "AlertActivity";
	
	private BaseAdapter alertListAdapter;
	private Alert alert = new Alert();
	
	private Button cleanAlertBtn;
	private ListView alertList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alert);
		findViews();
		setOnClickListener();
		fetchData();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		title.setText("告警页面");
	}

	private void findViews() {
		alertList = (ListView) findViewById(R.id.alertList);
		cleanAlertBtn = (Button) findViewById(R.id.cleanAlert);
		
		cleanAlertBtn.setVisibility(View.VISIBLE);
	}
	
	private void setOnClickListener() {
		
	}
	
	private void fetchData() {
		new Thread() {
			public void run() {
				handler.sendEmptyMessage(alert.load());
			}
		}.start();
	}

	private BaseAdapter getAdapterInstance() {
		return new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ItemViewHolder itemViewHolder;
				if (convertView == null) {
					convertView = LayoutInflater.from(AlertActivity.this)
							.inflate(R.layout.row_alert, null);
					itemViewHolder = new ItemViewHolder(convertView);
					convertView.setTag(itemViewHolder);
				} else {
					itemViewHolder = (ItemViewHolder) convertView.getTag();
				}
				itemViewHolder.setTag(position);
				itemViewHolder.fillData(alert.get(position));
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
				return alert.size();
			}
		};
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if (msg.what >= 0) {
				if (alertListAdapter == null) {
					alertListAdapter = getAdapterInstance();
					alertList.setAdapter(alertListAdapter);
				} else {
					alertListAdapter.notifyDataSetChanged();
				}
			} else {
				Log.d(TAG, "load failed:" + msg.what);
			}
			super.handleMessage(msg);
		}
	};
	
	class ItemViewHolder {
		TextView type;
		TextView datetime;
		TextView alert;

		public ItemViewHolder(View convertView) {
			type = (TextView) convertView.findViewById(R.id.type);
			datetime = (TextView) convertView.findViewById(R.id.time);
			alert = (TextView) convertView.findViewById(R.id.alert);
		}

		public void setOnClickListener() {
			
		}

		public void setTag(int position) {
			
		}

		public void fillData(Alert.AlertRow alertRow) {
			type.setText(alertRow.getType());
			datetime.setText(alertRow.getTime());
			alert.setText(alertRow.getAlert());
		}
	}
}
