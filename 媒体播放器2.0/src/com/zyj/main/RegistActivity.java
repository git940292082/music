package com.zyj.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.zyj.utils.Control;
import com.zyj.zyj.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistActivity extends Activity implements OnClickListener {

	private EditText edUserName;
	private EditText edPassword;
	private EditText edRePassword;
	private EditText edName;
	private EditText edEmail;
	private Button btRegist;
	private Button btBack;
	private String userName;
	private String password;
	private String rePassword;
	private String name;
	private String email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regist_activity);
		edUserName=(EditText)findViewById(R.id.regist_ed_username);
		edPassword=(EditText)findViewById(R.id.regist_ed_password);
		edRePassword=(EditText)findViewById(R.id.regist_ed_repassword);
		edName=(EditText)findViewById(R.id.regist_ed_name);
		edEmail=(EditText)findViewById(R.id.regist_ed_email);
		btRegist=(Button)findViewById(R.id.regist_bt_login);
		btBack=(Button)findViewById(R.id.regist_back);
		btRegist.setOnClickListener(this);
		btBack.setOnClickListener(this);

	}
	public static final int REGIST_OK=1;
	public static final int REGIST_TISHI=3;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.regist_bt_login:
			new Thread(){
				public void run() {
					try {
						regist();
					} catch (Exception e) {
						// TODO: handle exception

					}
				};
			}.start();
			break;

		case R.id.regist_back:
			finish();
			break;
		}
	}
	protected void regist() throws Exception{
		// TODO Auto-generated method stub
		userName = edUserName.getText().toString();
		password = edPassword.getText().toString();
		rePassword=edRePassword.getText().toString();
		name=edName.getText().toString();
		email=edEmail.getText().toString();
		if(userName.equals("")||
				password.equals("")||
				rePassword.equals("")||
				name.equals("")||
				email.equals("")){
			Message msg=Message.obtain();
			msg.what=REGIST_TISHI;
			msg.obj="不能为空";
			handler.sendMessage(msg);
			return;
		}
		if(!password.equals(rePassword)){
			Message msg=Message.obtain();
			msg.what=REGIST_TISHI;
			msg.obj="两次密码输入不一致";
			handler.sendMessage(msg);
			edRePassword.setText("");
			return;
		}
		URL url=new URL("http://123.206.212.202:8080/ems/regist.do");
		HttpURLConnection http=(HttpURLConnection) url.openConnection();
		http.setRequestMethod("POST");
		http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		http.setDoOutput(true);
		String outdata="loginname="+userName+"&password="+password+"&realnam="+name+"&email="+email;
		OutputStream out=http.getOutputStream();
		out.write(outdata.getBytes("UTF-8"));
		out.flush();
		BufferedReader read=new BufferedReader(new InputStreamReader( http.getInputStream()));
		StringBuilder sb=new StringBuilder();
		String line=null;
		while((line=read.readLine())!=null){
			sb.append(line);
		}
		read.close();
		JSONObject jso=new JSONObject(sb.toString());
		if("ok".equals(jso.getString("result"))){
			handler.sendEmptyMessage(REGIST_OK);
		}else{
			Message msg=Message.obtain();
			msg.obj=jso.getString("msg");
			msg.what=REGIST_TISHI;
			handler.sendMessage(msg);
		}
	}
	private void save(){
		Editor ed=Control.share.edit();
		ed.putString("username",userName);
		ed.putString("password", password);
		ed.putString("name", name);
		ed.putString("emali", email);
		ed.commit();
		Intent i=new Intent();
		i.putExtra("username", userName);
		i.putExtra("password", password);
		setResult(Activity.RESULT_OK,i);
		finish();
	}
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			String msgs=null;
			switch (msg.what) {
			case REGIST_OK:
				msgs="注册成功";
				save();
				break;
			case REGIST_TISHI:
				msgs=(String) msg.obj;
			}
			Toast.makeText(getApplicationContext(), msgs, Toast.LENGTH_SHORT).show();
		};
	};

}
