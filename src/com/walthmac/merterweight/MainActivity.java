package com.walthmac.merterweight;

import java.util.Timer;
import java.util.TimerTask;

import com.walthmac.data.Status;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends StatusBaseActivity {

	public final static String TAG = "MainActivity";

	private TextView wgtPerHour;
	private TextView wgtPerHourSet;
	private TextView kgPerMeter;
	private TextView kgPerMeterSet;
	private TextView speedPerMin;
	private TextView meterPerMin;
	private Button settingBtn;
	private View prgBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViews();
		setOnClickListener();
	}
	
	private void findViews() {
		wgtPerHour = (TextView) findViewById(R.id.wgtPerHour);
		wgtPerHourSet = (TextView) findViewById(R.id.setWgtPerHour);
		kgPerMeter = (TextView) findViewById(R.id.kgPerMeter);
		kgPerMeterSet = (TextView) findViewById(R.id.setKgPerMeter);
		speedPerMin = (TextView) findViewById(R.id.speedPerMin);
		meterPerMin = (TextView) findViewById(R.id.meterPerMin);
		settingBtn = (Button) findViewById(R.id.setting);
		prgBar = findViewById(R.id.prgBar);
	}
	
	private void setOnClickListener() {
		settingBtn.setOnClickListener(settingClicked);
	}

	public void updateData() {
		try {
			wgtPerHour.setText(Double.toString(status.get("wgtPerHour")));
			wgtPerHourSet.setText(Double.toString(status.get("setWgtPerHour")));
			kgPerMeter.setText(Double.toString(status.get("kgPerMeterShow")));
			kgPerMeterSet.setText(Double.toString(status.get("setkgPerMeter")));
			speedPerMin.setText(Double.toString(status.get("speedPerMin")));
			meterPerMin.setText(Double.toString(status.get("meterPerMin")));
			setPercentage(prgBar, 0.5);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setPercentage(View l, double percentage) {
		//l.setLayoutParams(new LinearLayout.LayoutParams(103, 20+(int)(170*percentage)));
		android.view.ViewGroup.LayoutParams lp =l.getLayoutParams();
		lp.height = 20+(int)(160*percentage);
		l.setLayoutParams(lp);
	}
	
	private OnClickListener settingClicked = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			luanchActivity(ArgActivity.class);
		}
	};
	
	
}
