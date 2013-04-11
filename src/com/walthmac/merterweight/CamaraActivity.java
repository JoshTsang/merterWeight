package com.walthmac.merterweight;

import com.walthmac.lib.CamaraView;
import com.walthmac.lib.MjpegInputStream;
import com.walthmac.lib.MjpegView.MjpegViewThread;

import com.walthmac.lib.MjpegView;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class CamaraActivity extends BaseActivity {
	public final static String TAG = "CamaraActivity";
	
	
	//private CamaraView r;
	private com.walthmac.lib.MjpegView mv;
	private Button ctrlUp;
	private Button ctrlDown;
	private Button ctrlLeft;
	private Button ctrlRight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);    
        String URL = "http://192.168.0.107/videostream.cgi?user=admin&pwd=123456"; 
        LinearLayout camara = (LinearLayout) findViewById(R.id.camara);
//        r = new CamaraView(CamaraActivity.this);
//        r.setLayoutParams(new LinearLayout.LayoutParams(640, 480));
//        camara.addView(r);
        ctrlUp = (Button) findViewById(R.id.ctrl_up);
        ctrlDown = (Button) findViewById(R.id.ctrl_down);
        ctrlLeft = (Button) findViewById(R.id.ctrl_left);
        ctrlRight = (Button) findViewById(R.id.ctrl_right);
//        r.startCapture();
        mv = new MjpegView(this);
        
        camara.addView(mv);
        mv.setSource(MjpegInputStream.read(URL));//���������Դ����MjpegInputStreamȥ��ȡ
        mv.setDisplayMode(MjpegView.SIZE_BEST_FIT);
        mv.showFps(true);
        
        ctrlUp.setOnTouchListener(ctrlBtn);
        ctrlDown.setOnTouchListener(ctrlBtn);
        ctrlLeft.setOnTouchListener(ctrlBtn);
        ctrlRight.setOnTouchListener(ctrlBtn);
   }

	@Override
	protected void onResume() {
		super.onResume();
		title.setText("实时影像");
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		//r.stop();
		super.finish();
	}
    
	private OnTouchListener ctrlBtn = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction() == MotionEvent.ACTION_UP){  
               mv.control(CamaraView.COMMAND_STOP);
            }   
            if(event.getAction() == MotionEvent.ACTION_DOWN){  
            	int command = -1;
            	switch (v.getId()) {
            	case R.id.ctrl_up:
            		command = CamaraView.COMMAND_UP;
            		break;
            	case R.id.ctrl_down:
            		command = CamaraView.COMMAND_DOWN;
            		break;
            	case R.id.ctrl_left:
            		command = CamaraView.COMMAND_LEFT;
            		break;
            	case R.id.ctrl_right:
            		command = CamaraView.COMMAND_RIGHT;
            		break;
            	default:
            		Log.d(TAG, "unknow id:" + v.getId());
                }
            	if (command >= 0) {
            		mv.control(command);
            	}
            }  
			return false;
		}
	};
}
