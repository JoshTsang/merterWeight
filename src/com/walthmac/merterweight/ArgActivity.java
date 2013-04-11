package com.walthmac.merterweight;

import com.walthmac.data.Arg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ArgActivity extends BaseActivity {

	public final static String TAG = "ArgActivity";
	
	private TextView extrAlgLen;
	private TextView extrAlgLendis;
	private TextView extrpGain;
	private TextView extriGain;
	private TextView extrDeadBand;
	private TextView wgtPerHWarnUp;
	private TextView wgtPerHWarnDw;
	
	private TextView calFrq;
	private TextView calFrqdis;
	private TextView dragpGain;
	private TextView dragiGain;
	private TextView dragDeadBand;
	private TextView kgPerMeterWarnUp;
	private TextView kgPerMeterWarnDw;
	
	private Arg arg = new Arg();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_arg);
		findViews();
		new Thread() {
			public void run() {
				int ret = arg.load();
				handler.sendEmptyMessage(ret);
			}
		}.start();
		setOnClickListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
		title.setText("参数页面");
	}
	
	private void fillData() {
		try {
			extrAlgLen.setText(Double.toString(arg.get("ExtrAlgLen")));
			extrAlgLendis.setText(Double.toString(arg.get("ExtrAlgLendis")));
			extriGain.setText(Double.toString(arg.get("extriGain")));
			extrpGain.setText(Double.toString(arg.get("extrpGain")));
			extrDeadBand.setText(Double.toString(arg.get("extrDeadBand")));
			wgtPerHWarnUp.setText(Double.toString(arg.get("WgtPerHWarnUp")));
			wgtPerHWarnDw.setText(Double.toString(arg.get("WgtPerHWarnDw")));
			
			calFrq.setText(Double.toString(arg.get("calFrq")));
			calFrqdis.setText(Double.toString(arg.get("calFrqdis")));
			dragiGain.setText(Double.toString(arg.get("dragiGain")));
			dragpGain.setText(Double.toString(arg.get("dragpGain")));
			dragDeadBand.setText(Double.toString(arg.get("dragDeadBand")));
			kgPerMeterWarnDw.setText(Double.toString(arg.get("kgPerMeterWarnDw")));
			kgPerMeterWarnUp.setText(Double.toString(arg.get("kgPerMeterWarnUp")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setOnClickListener() {
		extrAlgLen.setOnClickListener(updateValueClicked);
		extrAlgLendis.setOnClickListener(updateValueClicked);
		extriGain.setOnClickListener(updateValueClicked);
		extrpGain.setOnClickListener(updateValueClicked);
		extrDeadBand.setOnClickListener(updateValueClicked);
		wgtPerHWarnUp.setOnClickListener(updateValueClicked);
		wgtPerHWarnDw.setOnClickListener(updateValueClicked);
		
		calFrq.setOnClickListener(updateValueClicked);
		calFrqdis.setOnClickListener(updateValueClicked);
		dragiGain.setOnClickListener(updateValueClicked);
		dragpGain.setOnClickListener(updateValueClicked);
		dragDeadBand.setOnClickListener(updateValueClicked);
		kgPerMeterWarnDw.setOnClickListener(updateValueClicked);
		kgPerMeterWarnUp.setOnClickListener(updateValueClicked);
	}
	
	private void findViews() {
		extrAlgLen = (TextView) findViewById(R.id.ExtrAlgLen);
		extrAlgLendis = (TextView) findViewById(R.id.ExtrAlgLendis);
		extrpGain = (TextView) findViewById(R.id.extrpGain);
		extriGain = (TextView) findViewById(R.id.extriGain);
		extrDeadBand = (TextView) findViewById(R.id.extrDeadBand);
		wgtPerHWarnUp = (TextView) findViewById(R.id.WgtPerHWarnUp);
		wgtPerHWarnDw = (TextView) findViewById(R.id.WgtPerHWarnDw);
		
		calFrq = (TextView) findViewById(R.id.calFrq);
		calFrqdis = (TextView) findViewById(R.id.calFrqdis);
		dragpGain = (TextView) findViewById(R.id.dragpGain);
		dragiGain = (TextView) findViewById(R.id.dragiGain);
		dragDeadBand = (TextView) findViewById(R.id.dragDeadBand);
		kgPerMeterWarnUp = (TextView) findViewById(R.id.kgPerMeterWarnUp);
		kgPerMeterWarnDw = (TextView) findViewById(R.id.kgPerMeterWarnDw);
		
		extrAlgLen.setTag("ExtrAlgLen");
		extrAlgLendis.setTag("ExtrAlgLendis");
		extriGain.setTag("extriGain");
		extrpGain.setTag("extrpGain");
		extrDeadBand.setTag("extrDeadBand");
		wgtPerHWarnUp.setTag("WgtPerHWarnUp");
		wgtPerHWarnDw.setTag("WgtPerHWarnDw");
		
		calFrq.setTag("calFrq");
		calFrqdis.setTag("calFrqdis");
		dragiGain.setTag("dragiGain");
		dragpGain.setTag("dragpGain");
		dragDeadBand.setTag("dragDeadBand");
		kgPerMeterWarnDw.setTag("kgPerMeterWarnDw");
		kgPerMeterWarnUp.setTag("kgPerMeterWarnUp");
	}
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what >= 0) {
				fillData();
			}
			super.handleMessage(msg);
		}
		
	};
	
	private OnClickListener updateValueClicked = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String id = (String) v.getTag();
			showArgSetDlg(id, Double.toString(arg.get(id)), arg.getName(id));
		}
	};
	private void showArgSetDlg(final String id, final String curValue, final String argName) {
		LayoutInflater factory = LayoutInflater.from(this);
        final View v = factory.inflate(R.layout.arg_set_dlg, null);
        final TextView name = (TextView) v.findViewById(R.id.name);
        final EditText value = (EditText) v.findViewById(R.id.value);
        name.setText(argName);
        value.setText(curValue);
        value.setTag(id);
        value.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        new AlertDialog.Builder(ArgActivity.this)
            .setTitle("确认修改")
            .setView(v)
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	new AlertDialog.Builder(ArgActivity.this)
                    .setTitle("修改")
                    .setMessage("确认将 " + argName + " 修改为 " + value.getText() + " ?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        	try {
                        		arg.set(id, Double.parseDouble(value.getText().toString()));
                        		fillData();
							} catch (Exception e) {
								Toast.makeText(getApplicationContext(), "非法的值", Toast.LENGTH_LONG).show();
							}
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    })
                    .create().show();
                }
            })
            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            })
            .create().show();
	}
}
