package com.zyj.services;

import java.util.List;
import java.util.Random;

import com.zyj.app.App;
import com.zyj.example.Music;
import com.zyj.model.IOnMusicLoaded;
import com.zyj.model.MusicModel;
import com.zyj.model.OnSongIdInfo;
import com.zyj.utils.Control;
import com.zyj.zyj.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.Notification.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
public class MusicService  extends Service {
	public int NowTime;
	public int NowPosition;
	private MediaPlayer player;
	private int PlayMore;
	Intent i=new Intent();
	private BarMusicThread musicBar;
	private MusicControlBroad musicControlBroad;
	private HeadsetBroadService receiver;
	private List<Music> musics;
	private App app;
	protected boolean isPlay;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onCreate() {
		super.onCreate();
		app=(App) getApplication();
		loadData();
		Log.i("123", "绑定成功1");
		loadBoastCast();
		//监听播放末尾
		player=new MediaPlayer();
		player.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				if(isPlay){
					nexts();
				}
				Log.i("123", "播放完");
			}	
		});
	}
	private void loadBoastCast() {
		// 加载广播
		musicControlBroad =new MusicControlBroad();
		IntentFilter inf=new IntentFilter();
		inf.addAction(Control.PLAY_OR_PAUSE);
		inf.addAction(Control.PREVIOUS);
		inf.addAction(Control.NEXT);
		inf.addAction(Control.SEEK_TO);
		inf.addAction(Control.MODE);
		inf.addAction(Control.PLAY_POSITION);
		inf.addAction(Control.ACTIVITY_INBACK);
		inf.addAction(Control.ACTIVITY_NOBACK);
		inf.addAction(Control.MUSIC_LINE_PLAY);
		inf.addAction(Control.BACK);
		inf.addAction(Control.PAUSE);
		registerReceiver(musicControlBroad, inf);
		receiver=new HeadsetBroadService();
		IntentFilter headset=new IntentFilter();
		headset.addAction(Intent.ACTION_HEADSET_PLUG);
		registerReceiver(receiver, headset);
		sendBroadcast(i);
	}
	private void loadData() {
		// 加载数据
		NowTime=app.getNowTime();
		NowPosition=app.getPosition();
		musicType=app.getMusicMode();
		Log.i("213", musicType+"servidce loadad");
		if(musicType!=666){
			MusicModel model=new MusicModel();
			model.loadNewMusic(musicType, 0, 100, new IOnMusicLoaded() {
				@Override
				public void OnMusicLoaded(List<Music> musics) {
					// TODO Auto-generated method stub
					if(musics!=null){
						MusicService.this.musics=musics;
						App.mapMusics.put(musicType, musics);
					}else{
						MusicService.this.musics=App.mapMusics.get(666);
					}
					app.setMusic(MusicService.this.musics.get(NowPosition));
					Log.i("213", musics.size()+"servidce loadad");
				}
			});
		}else{
			musics=App.mapMusics.get(666);
		}
		app.setPosition(NowPosition);
		app.setNowTime(NowTime);
		app.setMusicMode(musicType);
	}
	//暂停
	public void pauses(){
		NowTime=player.getCurrentPosition();
		app.setNowTime(NowTime);
		player.pause();
		i.setAction(Control.NOW_PAUSE);
		sendBroadcast(i);
		Log.i("123", "暂停成功");
		stopBarMusicThread();
	}
	//上一首
	public void previous(){

		plays(--NowPosition);
	}
	//下一首
	public void nexts(){
		switch(PlayMore){
		case Control.TYPE_REPEAT:
			plays(++NowPosition);
			break;
		case Control.TYPE_SINGLE:
			plays(NowPosition);
			break;
		case Control.TYPE_RANDOM:
			plays(NowPosition=new Random().nextInt(musics.size()));
		}
	}
	//播放
	public void plays(int position){
		NowTime=0;
		if(position<0)position=musics.size()-1;
		if(position>=musics.size())position=0;
		NowPosition=position;
		if(musics.get(position).getPath()==null){
			mode();
		}else{
			plays();
		}
	}
	void mode(){
		final MusicModel model=new MusicModel();
		model.getSongIdInfo(musics.get(NowPosition).getSong_id(),new OnSongIdInfo() {
			@Override
			public void onSongIdInfo(Music music) {
				// TODO Auto-generated method stub
				if(music!=null){
					musics.get(NowPosition).setSongInfo(music.getSongInfo());
					musics.get(NowPosition).setSongUrls(music.getSongUrls());
					musics.get(NowPosition).setPath(music.getSongUrls().get(0).getFile_link());
					Log.i("123", "播fsdjkafhsiadf放");
					plays();
					
				}else{
					Toast.makeText(getApplicationContext(), "请检查网络", Toast.LENGTH_SHORT).show();
					return;
				}
			}
		});
	}
	public void plays(){
		new  Thread(){public void run() {
			try {
				isPlay=false;
				player.reset();
				player.setDataSource(musics.get(NowPosition).getPath());
				player.prepare();
				player.seekTo(NowTime);
				player.start();
				app.setNowTime(0);
				Log.i("123","播放成功"+NowPosition );
			} catch (Exception e) {
			}
			i.setAction(Control.NOW_PLAY);
			musics.get(NowPosition).setFile_duration(player.getDuration());
			app.setMusic(musics.get(NowPosition));
			app.setPosition(NowPosition);
			app.setMusicMode(musicType);
			sendBroadcast(i);
			isPlay=true;
		};}.start();
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	private int musicType;
	private class MusicControlBroad extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String a=intent.getAction();
			if(a.equals(Control.PLAY_OR_PAUSE)){ //播放暂停
				if(musics==null)return;
				if(player.isPlaying()){
					pauses();
				}else{
					player.start();
					i.setAction(Control.NOW_PLAY);
					sendBroadcast(i);
					startBarMusicThread();
				}
			}else if(a.equals(Control.PREVIOUS)){ //上一首
				previous();
				Log.i("13", "上一首");
			}else if(a.equals(Control.NEXT)){//下一首
				nexts();
				Log.i("13", "下一首");
			}else if(a.equals(Control.SEEK_TO)){//指定位置
				NowTime=intent.getIntExtra("seek_to", 0);
				plays();
				startBarMusicThread();
				Log.i("13", "指定位置");
			}else if(a.equals(Control.MODE)){//播放模式
				PlayMore=intent.getIntExtra("mode", 0);
				Log.i("13", PlayMore+"");
			}else if(a.equals(Control.PLAY_POSITION)){
				musicType=intent.getIntExtra("lien_name",0);
				musics=App.mapMusics.get(musicType);
				Log.i("123", musicType+"");
				int position=intent.getIntExtra("play_position", 0);
				plays(position);
				startBarMusicThread();
			}else if(a.equals(Control.ACTIVITY_INBACK)){
				stopBarMusicThread();

			}else if(a.equals(Control.ACTIVITY_NOBACK)){
				if(player.isPlaying()){
					startBarMusicThread();
					i.putExtra("play_position", NowPosition);
					i.putExtra("dutation", player.getDuration());
					i.putExtra("title", musics.get(NowPosition).getTitle());
					i.putExtra("line_name", musicType);
					i.putExtra("artist_name", musics.get(NowPosition).getArtist_name());
					i.setAction(Control.NOW_PLAY);
				}else{
					i.setAction(Control.NOW_PAUSE);
				}
				i.putExtra("play_position", NowPosition);
				sendBroadcast(i);
			}else if(a.equals(Control.BACK)){
				app.setPosition(NowPosition);
				app.setMusicMode(musicType);
				if(player.isPlaying()){
					app.setNowTime(NowTime);
					pauses();
				}
			}else if(a.equals(Control.PAUSE)){
				if(player.isPlaying()){
					pauses();
				}
			}
		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(receiver);
		unregisterReceiver(musicControlBroad);
		if(player.isPlaying()){
			pauses();
			NowTime=player.getCurrentPosition();
			stopBarMusicThread();
		}
		app.setNowTime(NowTime);
		app.setPosition(NowPosition);
		app.setMusicMode(musicType);
		super.onDestroy();
	}
	//开启进度条线程
	public void startBarMusicThread(){
		if(musicBar==null){
			musicBar=new BarMusicThread();
			musicBar.start();
		}

	}
	//关闭进度挑线程
	public void stopBarMusicThread(){
		if(musicBar!=null){
			musicBar.setRunning(false);
			musicBar=null;
		}
	}
	//音乐进度条线程
	public class BarMusicThread extends Thread{
		private boolean isRunning=true;

		public boolean isRunning() {
			return isRunning;
		}

		public void setRunning(boolean isRunning) {
			this.isRunning = isRunning;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while(isRunning){
				i.setAction(Control.UPDSTE_NOW_TIME);
				i.putExtra("now_time",player.getCurrentPosition());
				sendBroadcast(i);
				try {
					Thread.sleep(490);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	//监听耳机
	public class HeadsetBroadService extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if(player.isPlaying()){
				if(intent.getIntExtra("state",1)==0){
					pauses();
					stopBarMusicThread();
					i.setAction(Control.NOW_PAUSE);
					sendBroadcast(i);
				}
			}
		}

	}
}


