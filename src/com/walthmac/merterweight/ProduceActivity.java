package com.walthmac.merterweight;

import android.os.Bundle;

public class ProduceActivity extends BaseActivity {

	public final static String TAG = "ArgActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_produce);
	}

	@Override
	protected void onResume() {
		super.onResume();
		title.setText("生产页面");
	}
}
