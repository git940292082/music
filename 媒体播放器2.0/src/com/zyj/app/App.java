package com.zyj.app;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.x;

import com.zyj.dao.AudioFratory;
import com.zyj.dao.IDao;
import com.zyj.example.Music;
import com.zyj.example.Music;
import com.zyj.example.Pictures;
import com.zyj.example.Video;
import com.zyj.model.IOnMusicLoaded;
import com.zyj.model.MusicModel;
import com.zyj.services.MusicService;
import com.zyj.utils.Control;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.ContactsContract.Contacts.Data;
import android.widget.Toast;
@SuppressLint("UseSparseArrays")
public class App extends Application{
	public static final List<Pictures> Pictures=new ArrayList<Pictures>();
	public static Map<Integer, List<Music>> mapMusics;
	public static AsyncTask<String, String, List<Music>> asy;
	public static Map<String, SoftReference<Bitmap>> mapPic=new HashMap<String, SoftReference<Bitmap>>();
	private Music music;
	private int position;
	private int musicMode=666;
	private int PlayMore;
	private int NowTime;
	public static Context context;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		x.Ext.init(this);
		mapMusics=new HashMap<Integer, List<Music>>();
		load(getContentResolver());
		Control.share=getSharedPreferences("data",MODE_PRIVATE);
		context=getApplicationContext();
		Intent i = new Intent();
		i.setAction("music.zyj.action.musicservice2.0");
		startService(i);
		super.onCreate();
	}
	public static void load(ContentResolver conres){
		IDao<Music> musicDao=AudioFratory.getMusics(conres);
		mapMusics.put(666,musicDao.getMedias(null, null));
	}
	public Music getMusic() {
		return music;
	}
	public void setMusic(Music music) {
		this.music = music;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getMusicMode() {
		return musicMode;
	}
	public void setMusicMode(int musicMode) {
		this.musicMode = musicMode;
	}
	public int getPlayMore() {
		return PlayMore;
	}
	public void setPlayMore(int playMore) {
		PlayMore = playMore;
	}
	public int getNowTime() {
		return NowTime;
	}
	public void setNowTime(int nowTime) {
		NowTime = nowTime;
	}
}
