package com.zyj.main;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.spec.MGF1ParameterSpec;

import org.json.JSONObject;

import com.zyj.utils.Control;
import com.zyj.zyj.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
@SuppressLint("HandlerLeak")
public class LoginDialog extends Activity {

	private EditText edUserName;
	private EditText edPassword;
	private Button btRegist;
	private Button btLogin;
	private ImageButton btEms;
	private EditText edEms;
	private Bitmap bf;
	public static final int LOGIN_OK=1;
	public static final int LOGIN_OVER=2;
	public static final int LOGIN_CODE_OK=3;
	public static final int LOGIN_TISHI=4;
	private String cookieId;
	private Button btBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_dialog);
		loadView();
		loadLestener();
		loadData();
	}

	private void loadData() {
		// TODO Auto-generated method stub
		edUserName.setText(Control.share.getString("username", ""));
		edPassword.setText(Control.share.getString("password", ""));
	}

	private void loadView() {
		edUserName=(EditText)findViewById(R.id.login_ed_username);
		edPassword=(EditText)findViewById(R.id.login_ed_pass);
		btLogin=(Button)findViewById(R.id.login_bt_login);
		btRegist=(Button)findViewById(R.id.login_bt_regist);
		btEms=(ImageButton)findViewById(R.id.login_img_ems);
		edEms=(EditText)findViewById(R.id.login_ed_ems);
		btBack=(Button)findViewById(R.id.login_bt_back);
	}

	private void loadLestener() {
		// TODO Auto-generated method stub
		Listeners listener=new Listeners();
		btLogin.setOnClickListener(listener);
		btRegist.setOnClickListener(listener);
		btEms.setOnClickListener(listener);
		btBack.setOnClickListener(listener);
		updateEms();
	}
	protected class Listeners implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.login_bt_login:
				new Thread(){
					public void run() {
						try {
							login();
						} catch (Exception e) {
							
						}
					};}.start();
					break;
			case R.id.login_bt_regist:
				startActivityForResult(new Intent(getApplicationContext(),RegistActivity.class),Control.REGIST_RES);
				break;
			case R.id.login_img_ems:
				updateEms();
				break;
			case R.id.login_bt_back:
				finish();
			}
		}

	}
	private void updateEms(){
		new Thread(){
			public void run() {
				try {
					loadEms();
				} catch (Exception e) {
					// TODO: handle exception
				}
			};
		}.start();
	}
	protected void loadEms() throws Exception{
		// TODO Auto-generated method stub
		URL url=new URL("http://123.206.212.202:8080/ems/getCode.do"); 
		HttpURLConnection http=(HttpURLConnection) url.openConnection();
		http.setRequestMethod("GET");
		InputStream in=http.getInputStream();
		//解析响应消息头
		cookieId=http.getHeaderField("Set-Cookie").split(";")[0];
		bf=BitmapFactory.decodeStream(in);
		Log.i(cookieId, cookieId);
		handler.sendEmptyMessage(LOGIN_CODE_OK);
	}
	protected void login() throws Exception {
		// TODO Auto-generated method stub
		String userName = edUserName.getText().toString();
		String password = edPassword.getText().toString();
		if(userName.equals("")||password.equals("")){
			handler.sendEmptyMessage(LOGIN_TISHI);
			return;
		}
		String code=edEms.getText().toString();
		URL url=new URL("http://123.206.212.202:8080/ems/login.do");
		HttpURLConnection http=(HttpURLConnection) url.openConnection();
		http.setRequestProperty("Cookie", cookieId);
		http.setRequestMethod("POST");
		http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		http.setDoOutput(true);
		String outData="loginname="+userName+"&password="+password+"&code="+code;
		OutputStream out=http.getOutputStream();
		out.write(outData.getBytes("UTF-8"));
		out.flush();
		BufferedReader read=new BufferedReader(new InputStreamReader( http.getInputStream()));
		Log.i("提示","2");
		StringBuilder sb=new StringBuilder();
		String line=null;
		while((line=read.readLine())!=null){
			sb.append(line);
		}
		read.close();
		JSONObject jso=new JSONObject(sb.toString());
		if("ok".equals(jso.getString("result"))){
			handler.sendEmptyMessage(LOGIN_OK);
		}else{
			Message msg=Message.obtain();
			msg.obj=jso.getString("msg");
			msg.what=LOGIN_OVER;
			handler.sendMessage(msg);
		}
	}
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			String msgs="";
			switch (msg.what) {
			case LOGIN_OK:
				msgs="登录成功";
				setResult(Activity.RESULT_OK,new Intent());
				finish();
				break;
			case LOGIN_OVER:
				msgs=(String) msg.obj;
				break;
			case LOGIN_CODE_OK:
				btEms.setImageBitmap(bf);
				return;
			case LOGIN_TISHI:
				msgs="不能为空";
			}
			Toast.makeText(getApplicationContext(),msgs, Toast.LENGTH_SHORT).show();
		};
	};
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==Control.REGIST_RES){
			if(resultCode==Activity.RESULT_OK){
				edUserName.setText("");
				edUserName.setText("");
				edUserName.setText(data.getStringExtra("username"));
				edPassword.setText(data.getStringExtra("password"));
			}
		}
	};

}
