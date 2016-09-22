package com.zyj.adapter;

import java.io.File;
import java.lang.ref.SoftReference;
import java.security.spec.MGF1ParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zyj.app.App;
import com.zyj.example.Music;
import com.zyj.example.Pictures;
import com.zyj.utils.BitmapUtils;
import com.zyj.zyj.R;

import android.content.Context;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class PictureAdapter extends MyAdapter<Pictures>{
	List<View666> imgs=new ArrayList<View666>();
	private PictureThread thread;
	public boolean isRunnin=true;
	Animation imgsAim = AnimationUtils.loadAnimation(getContext(), R.anim.img_set);
	private Handler hander=new Handler(){
		public void handleMessage(Message msg) {
			View666 view=(View666) msg.obj;
			view.img.setImageBitmap(view.bm);
			view.img.startAnimation(imgsAim);
		};
	};
	public PictureAdapter(List<Pictures> data, Context context) {
		super(context,data);
		thread=new PictureThread();
		thread.start();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		HolderView holederView=new HolderView();
		if(convertView==null){
			convertView=getInflater().inflate(R.layout.list_picture, null);
			holederView.img=(ImageView) convertView.findViewById(R.id.music_line_item_ico);
			holederView.tv=(TextView)convertView.findViewById(R.id.video_tv_title);
			convertView.setTag(holederView);
		}else{
			holederView=(HolderView) convertView.getTag();
		}
		imgs.add(new View666(holederView.img,getData().get(position)));
		synchronized (thread) {
			thread.notify();
		}
		return convertView;
	}
	public class HolderView{
		ImageView img;
		TextView tv;
	}
	class View666{
		ImageView img;
		Pictures picture;
		Bitmap bm;
		public View666( ImageView img, Pictures picture) {
			super();
			this.img = img;
			this.picture = picture;
		}
	}
	public class PictureThread extends Thread {
		private int min;
		private static final int max=200;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(isRunnin){
				if(imgs.isEmpty()){
					synchronized (thread) {
						try {
							thread.wait();
						} catch (InterruptedException e) {
						}
					}
				}else{
					View666 view= imgs.remove(0);
					Bitmap bm = null;
					Pictures picture=view.picture;
					String s=picture.getPath();
					String path = s.substring(s.lastIndexOf("/"));
					File file = new File(getContext().getCacheDir(), "images"+path);
					bm=BitmapUtils.loadBitmap(file);
					if(bm!=null){
						Log.i("info", "图片是从文件缓存中读取的...");
					}else{
						int width=picture.getWidth();
						int height=picture.getHeight();
						if(width>max&&height>max){
							if(width>height){
								min=width/max;
							}else{
								min=height/max;
							}
						}
						Options opts=new Options();
						opts.inSampleSize=min;
						bm=BitmapFactory.decodeFile(s, opts);
						try {
							BitmapUtils.saveBitmap(bm, file);
						} catch (Exception e) {
						}
					}
					Message msg=Message.obtain();
					view.bm=bm;
					msg.obj=view;
					msg.what=1;
					hander.sendMessage(msg);
				}
			}
			Log.i("asd", "关闭");
		}
	}
	public void close(){
		isRunnin=false;
		synchronized (thread) {
			thread.notify();
		}
		
	}
}
