package com.walthmac.lib;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.walthmac.constant.Server;

import android.util.Log;
import android.view.SurfaceHolder.Callback;
import android.content.Context;   
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;   
import android.graphics.Color;
import android.graphics.Paint;   
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.Animation;

public class CamaraView extends SurfaceView implements Callback, Runnable{
	public final static String TAG = "CamaraView";
	
    private SurfaceHolder sfh;    
    private Thread th;    
    private Canvas canvas;    
    private Paint paint;    
    URL videoUrl;
    
    public final static String host = "http://192.168.0.107";
	public final static String urlCamara = host + "/snapshot.cgi";
	public final static String urlCtrl = host + "/decoder_control.cgi?command=";
    HttpURLConnection conn;
    Bitmap bmp;
    boolean run = true;

	private final static  String username = "admin";
	private final static  String password = "123456";
	
    public final static int COMMAND_STOP = 1;
    public final static int COMMAND_UP = 2;
    public final static int COMMAND_DOWN = 0;
    public final static int COMMAND_LEFT = 6;
    public final static int COMMAND_RIGHT = 4;
    
    private List<Integer> commands = new ArrayList<Integer>();

    public CamaraView(Context context) {    
        super(context);    
            
        sfh = this.getHolder();    
        sfh.addCallback(this); 
        paint = new Paint();    
        paint.setAntiAlias(true);    
        paint.setColor(Color.RED);    
        this.setKeepScreenOn(true);// 保持屏幕常亮   
        
        
    }    
   
    @Override    
    public void startAnimation(Animation animation) {    
        super.startAnimation(animation);    
    } 
    
    public void surfaceCreated(SurfaceHolder holder) {    
//        ScreenW = this.getWidth();// 获取屏幕宽度    
//        ScreenH = this.getHeight();
    	if (th.getState() == Thread.State.NEW) {
    		th.start();
    	}
    }   
    
    private void draw() {    
    	if (commands.size() > 0) {
    		sendCommand();
    	}
        try {
        	Log.d(TAG, "a");
            InputStream inputstream;
            inputstream = null;
            videoUrl=new URL(urlCamara);    
            auth();
        	Log.d(TAG, "b");
            conn = (HttpURLConnection)videoUrl.openConnection();
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.connect();
        	Log.d(TAG, "c");
            inputstream = conn.getInputStream(); 
        	Log.d(TAG, "c1");
            bmp = BitmapFactory.decodeStream(inputstream);
        	Log.d(TAG, "d");
            canvas = sfh.lockCanvas(); 
            canvas.drawColor(Color.WHITE);   
            canvas.drawBitmap(bmp, 0, 0, paint);
        	Log.d(TAG, "e");
            //sfh.unlockCanvasAndPost(canvas); 
            conn.disconnect();
        } catch (Exception ex) {    
        	ex.printStackTrace();
        } finally {   
            if (canvas != null)    
                sfh.unlockCanvasAndPost(canvas);    
        }    
    }

	private void auth() {
		Authenticator.setDefault(new Authenticator(){
		   @Override
		   protected PasswordAuthentication getPasswordAuthentication() {
		          return (new PasswordAuthentication(username, password.toCharArray()));
		}});
	}    
    
    public void run() {    
    	while (run) {
    		draw();
    	}
    }
    
    public void control(final int command) {
    	commands.add(command);
//    	new Thread() {
//    		public void run() {
//    			HttpGet httpRequest = new HttpGet(urlCtrl + command); 
//    		}
//    	}.start();
    }
    
    public void stop() {
    	run = false;
    }
    
    public void surfaceChanged(SurfaceHolder holder, int format, int width,    
            int height) {    
    }    
    
    public void surfaceDestroyed(SurfaceHolder holder) {    
        // TODO Auto-generated method stub    
    }   
    
    public void startCapture(){
    	if (th == null) {
    		th = new Thread(this);
    	}
    }
    
    private void sendCommand() {
    	int command = commands.remove(0);
    	
    	HttpGet httpRequest = new HttpGet(urlCtrl + command); 
    	DefaultHttpClient client;
    	HttpParams httpParameters1 = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters1, 5 * 1000);
		HttpConnectionParams.setSoTimeout(httpParameters1, 15 * 1000);
		client = new DefaultHttpClient(httpParameters1);
		BasicCredentialsProvider bcp = new BasicCredentialsProvider();
        bcp.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(
                username, password));
        client.setCredentialsProvider(bcp);
		try 
		{
			HttpResponse httpResponse = client.execute(httpRequest); 
			Log.d(TAG, httpResponse.toString());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}  
    }
}
