package com.zyj.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.zyj.app.App;
import com.zyj.utils.HttpUntils;
import com.zyj.zyj.R;

import android.app.IntentService;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

public class DownloadService  extends IntentService{

	private NotificationManager nm;

	public DownloadService() {
		super("xiaoming");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("213", "asdasdsad");
		this.nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}
	public void sndPregress(int min,int max,String titck,boolean b){
		Builder builder =new Builder(this);
		builder.setSmallIcon(R.drawable.ic_download)
		.setContentTitle(titck)
		.setTicker(titck);
		builder.setProgress(max, min, b);
		Notification n = builder.build();
		nm.notify(7879, n);
	}
	public void sendOver(String title,String text,String titck){
		Builder builder =new Builder(this);
		builder.setSmallIcon(R.drawable.ic_download)
		.setContentTitle(title)
		.setContentText(text)
		.setTicker(titck);
		Notification n = builder.build();
		nm.notify(7879, n);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.i("213", "asdasdsad");
		String title=intent.getStringExtra("title");
		String url=intent.getStringExtra("url");
		String bit=intent.getStringExtra("bit");
		int size=intent.getIntExtra("size", 0);
		File  file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),"_"+bit+"bit/"+title);
		Log.i("213", file.getAbsolutePath());
		if(file.exists()){
			return ;
		}
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		
		try {
			FileOutputStream out=new FileOutputStream(file);
			InputStream in=HttpUntils.getInputStream(url);
			sendOver("开始下载："+file.getName(),"开始下载", "开始下载："+file.getName());
			int length=0;
			byte[] buffer=new byte[1024*100];
			int aa=0;
			while((length=in.read(buffer))!=-1){
				Log.i("213", "asdasdsad"+length);
				sndPregress(aa, size,"正在下载:"+title,false);
				out.write(buffer, 0, length);
				out.flush();
				aa=aa+length;
			}
			out.close();
			sendOver("下载完成："+file.getName(),"点击打开", "下载完成："+file.getName());
			App.load(getContentResolver());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
