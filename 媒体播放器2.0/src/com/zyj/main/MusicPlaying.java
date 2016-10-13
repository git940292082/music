package com.zyj.main;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.zyj.adapter.LrcAdapter;
import com.zyj.adapter.MusicAdapter;
import com.zyj.app.App;
import com.zyj.example.Lrc;
import com.zyj.example.Music;
import com.zyj.example.SongInfo;
import com.zyj.example.SongUrl;
import com.zyj.model.LrcCallBack;
import com.zyj.model.MusicModel;
import com.zyj.services.DownloadService;
import com.zyj.utils.BitmapCallback;
import com.zyj.utils.BitmapUtils;
import com.zyj.utils.Control;
import com.zyj.utils.DateTimeUtils;
import com.zyj.zyj.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
public class MusicPlaying extends Activity{
	private TextView tvTitle;
	private TextView tvName;
	private TextView tvNowTime;
	private TextView tvDuraction;
	private SeekBar musicSeekBar;
	private ImageButton btPrevious;
	private ImageButton btPlayOrPause;
	private ImageButton btNext;
	private PlayBroad Playreceiver;
	private Intent intent;
	private TextView btBack;
	LinearLayout layout;
	private App app;
	private ImageView bg;
	private ListView lvLrc;
	private Button btMenu;
	private ImageButton btDownload;
	private View view;
	private ListView lvMusic;
	private PopupWindow popuwindow;
	private MusicAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_playing);
		loadview();
		loadlistener();
		loaddate();
	}
	private void loadview() {
		// 初始化控件
		tvTitle=(TextView)findViewById(R.id.music_playing_title);
		tvName=(TextView)findViewById(R.id.music_playing_name);
		tvNowTime=(TextView)findViewById(R.id.music_playing_nowtime);
		tvDuraction=(TextView)findViewById(R.id.music_playing_duraction);
		musicSeekBar=(SeekBar)findViewById(R.id.music_playing_seekbar);
		btPrevious=(ImageButton)findViewById(R.id.music_playing_previous);
		btPlayOrPause=(ImageButton)findViewById(R.id.music_playing_playorpause);
		btNext=(ImageButton)findViewById(R.id.music_playing_next);
		btBack=(TextView)findViewById(R.id.music_playing_back);
		bg=(ImageView)findViewById(R.id.music_playing_bg);
		btMenu=(Button)findViewById(R.id.music_playing_menu);
		lvLrc=(ListView)findViewById(R.id.music_playing_lv_lrc);
		layout=(LinearLayout) findViewById(R.id.music_play_layout);
		btDownload=(ImageButton) findViewById(R.id.music_playing_download);
		view=View.inflate(this, R.layout.music_playing_lv, null);
		lvMusic=(ListView) view.findViewById(R.id.playing_lv);
	}

	private void loadlistener() {
		// 监听器
		Listener listener=new Listener();
		musicSeekBar.setOnSeekBarChangeListener(listener);
		btPrevious.setOnClickListener(listener);
		btPlayOrPause.setOnClickListener(listener);
		btNext.setOnClickListener(listener);
		btBack.setOnClickListener(listener);
		btMenu.setOnClickListener(listener);
		btDownload.setOnClickListener(listener);
		lvMusic.setOnItemClickListener(listener);

	}
	private void loaddate() {
		app=(App) getApplication();
		IntentFilter filter=new IntentFilter();
		Playreceiver=new PlayBroad();
		filter.addAction(Control.NOW_PAUSE);
		filter.addAction(Control.NOW_PLAY);
		filter.addAction(Control.UPDSTE_NOW_TIME);
		// 加载数据
		registerReceiver(Playreceiver, filter);
		List<Music> musics=App.mapMusics.get(app.getMusicMode());
		adapter=new MusicAdapter(this, musics);
		lvMusic.setAdapter(adapter);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		intent=new Intent();
		intent.setAction(Control.ACTIVITY_NOBACK);
		sendBroadcast(intent);
		super.onResume();
	}
	int x;
	private List<Lrc> lrvs;
	private LrcAdapter lrcAdapter;
	public class Listener implements OnClickListener,OnSeekBarChangeListener,OnItemClickListener{
		@Override
		public void onClick(View v) {
			// 单机事件
			switch(v.getId()){
			case R.id.music_playing_previous:
				intent.setAction(Control.PREVIOUS);
				sendBroadcast(intent);
				break;
			case R.id.music_playing_playorpause:	
				intent.setAction(Control.PLAY_OR_PAUSE);
				sendBroadcast(intent);
				break;
			case R.id.music_playing_next:
				intent.setAction(Control.NEXT);
				sendBroadcast(intent);
				break;
			case R.id.music_playing_back:
				finish();
				break;
			case R.id.music_playing_menu:
				lvMusic();
				break;
			case R.id.music_playing_download:
				downLoad();
				break;
			}
		}
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			// TODO Auto-generated method stub
			tvNowTime.setText(DateTimeUtils.getDateFormat(progress));
			if(lrvs!=null){
				for (int i=0;i<lrvs.size();i++) {
					lrvs.get(i).setPlaying(false);
					if(DateTimeUtils.getDateFormat(progress).equals(lrvs.get(i).getDate())){
						lrvs.get(i).setPlaying(true);
						lrcAdapter.notifyDataSetChanged();
						lvLrc.smoothScrollToPosition(i);
						break;
					}
				}
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			intent.setAction(Control.ACTIVITY_INBACK);
			sendBroadcast(intent);

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			intent.setAction(Control.SEEK_TO);
			intent.putExtra("seek_to", seekBar.getProgress());
			sendBroadcast(intent);
		}
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setAction(Control.PLAY_POSITION);
			intent.putExtra("play_position", arg2);
			intent.putExtra("lien_name",app.getMusicMode());
			sendBroadcast(intent);
		}
	}
	protected class PlayBroad extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String action=intent.getAction();
			if(action.equals(Control.NOW_PLAY)){
				adapter.notifyDataSetChanged();
				fixed();
				btPlayOrPause.setImageResource(R.drawable.pause_selector);
			}else if(action.equals(Control.NOW_PAUSE)){
				btPlayOrPause.setImageResource(R.drawable.play_selector);
			}else if(action.equals(Control.UPDSTE_NOW_TIME)){
				musicSeekBar.setProgress(intent.getIntExtra("now_time", 0));
			}
		}
	}
	void fixed(){
		Music music=app.getMusic();
		tvTitle.setText(music.getTitle());
		tvName.setText(music.getAuthor());
		musicSeekBar.setMax(music.getFile_duration());
		tvDuraction.setText(DateTimeUtils.getDateFormat(music.getFile_duration()));
		SongInfo info=music.getSongInfo();
		lrc(music.getLrclink());
		if(info==null)return; 
		String s=info.getArtist_640_1136();
		if(s==null)s=info.getArtist_480_800();
		loadImgBg(s);
	}

	public void lvMusic() {
		// TODO Auto-generated method stub
		if(popuwindow==null){
			popuwindow=new PopupWindow(view, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,true);
			Drawable drawable = getResources().getDrawable(R.drawable.ic_window_bg);
			popuwindow.setBackgroundDrawable(drawable);
			popuwindow.showAsDropDown(btMenu, 0, 500);
		}else{
			popuwindow.showAsDropDown(btMenu, 0, 500);
		}
	}
	public void downLoad() {
		// TODO Auto-generated method stub
		if(app.getMusicMode()==666){
			Toast.makeText(this, "该文件是本地文件不必下载", Toast.LENGTH_SHORT).show();
			return;
		}
		AlertDialog.Builder builder=new Builder(this);
		final List<SongUrl> urls=app.getMusic().getSongUrls();
		String [] items =new String[urls.size()];
		for(int i=0;i<urls.size();i++){
			items[i]=Math.floor(urls.get(i).getFile_size() * 100.0 / 1024 / 1024) / 100.0  +"M　　　　"+urls.get(i).getFile_bitrate()+"Bit";
		}
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				SongUrl url = urls.get(which);
				//执行下载操作
				Log.i("info", "文件大小:"+url.getFile_size()+"文件路径:"+url.getFile_link());
				//启动Service
				Intent i = new Intent(MusicPlaying.this,DownloadService.class);
				i.putExtra("url", url.getFile_link());
				i.putExtra("title", app.getMusic().getTitle()+".mp3");
				i.putExtra("bit", url.getFile_bitrate()+"");
				i.putExtra("size", url.getFile_size());
				startService(i);
			}
		});
		AlertDialog dialog=builder.create();
		dialog.show();
	}
	protected void lrc(final String lrclink) {
		// TODO Auto-generated method stub
		if(lrclink==null||lrclink.equals(""))return;
		Log.i("123", lrclink);
		MusicModel model=new MusicModel();
		model.loadLrc(lrclink, new LrcCallBack() {
			@Override
			public void lrcDownLoad(List<Lrc> lrc) {
				// TODO Auto-generated method stub
				if(lrc!=null){
					lrvs=lrc;
				}else{
					lrvs=new ArrayList<Lrc>();
				}
				lrcAdapter=new LrcAdapter(getApplicationContext(), lrvs);
				lvLrc.setAdapter(lrcAdapter);
			}
		});
	}

	private void loadImgBg(String s) {
		// TODO Auto-generated method stub
		if(s==null||s.equals(""))return;
		String path = s.substring(s.lastIndexOf("/"));
		File file = new File(this.getCacheDir(), "images"+path);
		Bitmap b=BitmapUtils.loadBitmap(file);
		if(b!=null){
			Log.i("info", "图片是从文件缓存中读取的...");
			bg.setImageBitmap(b);
		}else{
			BitmapUtils.loadBitmap(file, s, new BitmapCallback() {
				@Override
				public void onBitmapLoaded(Bitmap result) {
					// TODO Auto-generated method stub
					if(result!=null){
						bg.setImageBitmap(result);
					}
				}
			});
		}
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(Playreceiver);
		super.onDestroy();
	}
}
