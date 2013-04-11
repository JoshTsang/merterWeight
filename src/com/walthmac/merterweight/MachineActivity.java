package com.walthmac.merterweight;

import java.util.Timer;
import java.util.TimerTask;

import com.walthmac.data.Status;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MachineActivity extends StatusBaseActivity {

	public final static String TAG = "MachineActivity";
	
	private TextView wgtPerRev;
	private TextView speedPerMin;
	private TextView meterPerMin;
	private LinearLayout extruderInPer;
	private LinearLayout extruderOutPer;
	private LinearLayout dragerInPer;
	private LinearLayout dragerOutPer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_machine);
		findViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		title.setText("挤出机\n牵引机显示");
	}
	
	private void findViews() {
		wgtPerRev = (TextView) findViewById(R.id.wgtPerRev);
		speedPerMin = (TextView) findViewById(R.id.speedPerMin);
		meterPerMin = (TextView) findViewById(R.id.meterPerMin);
		
		extruderInPer = (LinearLayout) findViewById(R.id.extruder_in);
		extruderOutPer = (LinearLayout) findViewById(R.id.extruder_out);
		dragerInPer = (LinearLayout) findViewById(R.id.drager_in);
		dragerOutPer = (LinearLayout) findViewById(R.id.drager_out);
	}
	
	private void setOnClickListener() {
		
	}
	
	public void updateData() {
		try {
			wgtPerRev.setText(Double.toString(status.get("wgtPerRev")));
			speedPerMin.setText(Double.toString(status.get("speedPerMin")));
			meterPerMin.setText(Double.toString(status.get("meterPerMin")));
			setPercentage(extruderInPer, status.get("AIN2Extruder")/10000);
			setPercentage(extruderOutPer, status.get("extrudDect")/10000);
			setPercentage(dragerInPer, status.get("AIN1Drager")/10000);
			setPercentage(dragerOutPer, status.get("dragerDect")/10000);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setPercentage(LinearLayout l, double percentage) {
		//l.setLayoutParams(new LinearLayout.LayoutParams(103, 20+(int)(170*percentage)));
		android.view.ViewGroup.LayoutParams lp =l.getLayoutParams();
		lp.height = 20+(int)(170*percentage);
		l.setLayoutParams(lp);
	}
	
	private OnClickListener settingClicked = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			luanchActivity(AlertActivity.class);
		}
	};
}
