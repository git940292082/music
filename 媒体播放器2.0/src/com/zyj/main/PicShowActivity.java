package com.zyj.main;

import com.zyj.app.App;
import com.zyj.zyj.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

public class PicShowActivity extends Activity{
	private PictureThread picThread;
	private int  Position;
	private ImageSwitcher imgs;
	private float downx;
	private Animation leftIn;
	private Animation leftOut;
	private Animation rightIn;
	private Animation rightOut;
	private ImageView img;
	private AutoThread thread;
	private  boolean isAuto;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pic_show_activity);
		Intent intent=getIntent();
		Position=intent.getIntExtra("position", 0);
		imgs=(ImageSwitcher)findViewById(R.id.pic_show_imgs);
		imgs.setFactory(new ViewFactory() {
			@Override
			public View makeView() {
				img = new ImageView(getApplicationContext());
				LayoutParams lp=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
				img.setLayoutParams(lp);
				img.setScaleType(ScaleType.CENTER_INSIDE);
				return img;
			}
		});
		leftIn=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_in);
		leftOut=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_out);
		rightIn=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_in);
		rightOut=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_out);
		show();
	}


	private void previous(){
		imgs.setInAnimation(leftIn);
		imgs.setOutAnimation(rightOut);
		Position--;
		if(Position<0)Position=App.Pictures.size();
		show();
	}
	private void next(){
		imgs.setInAnimation(rightIn);
		imgs.setOutAnimation(leftOut);
		Position++;
		if(Position>=App.Pictures.size())Position=0;
		show();
	}
	private void show() {
		picThread=new PictureThread(imgs);
		picThread.execute();
	}
	public class PictureThread extends AsyncTask<Integer,Bitmap,Bitmap> {
		private ImageSwitcher imgs;
		private int max=1000;
		private int min=1;
		public PictureThread(ImageSwitcher imgs) {
			super();
			this.imgs = imgs;
		}
		@Override
		protected Bitmap doInBackground(Integer... position) {
			// TODO Auto-generated method stub
			Bitmap bm = null;
				int width=App.Pictures.get(Position).getWidth();
				int height=App.Pictures.get(Position).getHeight();
				if(width>max&&height>max){
					if(width>height){
						min=width/max;
					}else{
						min=height/max;
					}
				} 
				Options opts=new Options();
				opts.inSampleSize=min;
				bm=BitmapFactory.decodeFile(App.Pictures.get(Position).getPath(), opts);
			return bm;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			Drawable drawable=new BitmapDrawable(getResources(),result);
			imgs.setImageDrawable(drawable);
		}
	}
	private long time;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			downx=event.getX();
			break;
		case MotionEvent.ACTION_UP:
			imgs.setX(0);
			if(downx-event.getX()>400){
				next();
			}
			if(event.getX()-downx>400){
				previous();
			}
			autoPlay();
			break;
		case  MotionEvent.ACTION_MOVE:
			imgs.setX(event.getX()-downx);
		}
		return super.onTouchEvent(event);
	}
	private void autoPlay(){

		if(System.currentTimeMillis()-time<500){
			if(isAuto){
				thread.isRunning=false;
				thread=null;
				isAuto=false;
				Toast.makeText(getApplicationContext(), "播放暂停", Toast.LENGTH_SHORT).show();
			}else{
				thread=new AutoThread();
				thread.start();
				isAuto=true;
				Toast.makeText(getApplicationContext(), "播放开始", Toast.LENGTH_SHORT).show();
			}
			
		}
		time=System.currentTimeMillis();
	}
	public class AutoThread extends Thread{
		public boolean isRunning=true;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while(isRunning){
					next();
					Log.i("123", "123");
				try {
					Thread.sleep(2000);
				} catch (Exception e) {

				}
			}
		}
	}
	public void onDestroy() {
		if(isAuto){
			thread.isRunning=false;
			thread=null;
		}
		super.onDestroy();
	}
}
