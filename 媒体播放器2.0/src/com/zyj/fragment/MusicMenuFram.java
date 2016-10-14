package com.zyj.fragment;
import java.io.File;

import com.zyj.app.App;
import com.zyj.example.Music;
import com.zyj.main.MusicPlaying;
import com.zyj.main.SearchActivity;
import com.zyj.utils.BitmapCallback;
import com.zyj.utils.BitmapUtils;
import com.zyj.utils.Control;
import com.zyj.utils.DateTimeUtils;
import com.zyj.zyj.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
public class MusicMenuFram extends Fragment implements OnClickListener,OnPageChangeListener{
	private Button btMusicLocal;
	private Button btMusicLine;
	private View layout;
	private ImageButton btMusicSearch;
	private ImageButton btPlayOrPause;
	private TextView tvNowTime;
	private ImageView ImgIco;
	private TextView tvTitle;
	private Intent intent =new Intent();
	private ProgressBar SeekBar;
	private BroadcastReceiver receiver;
	private App app;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
		if(layout==null){
			layout= inflater.inflate(R.layout.music_menu_fram, container, false);
			loadView();
			loadListener();
			laodDate();
		}else{
			((ViewGroup) layout.getParent()).removeView(layout); 
		}
		return layout;
	} 


	private void loadView() {
		btMusicLocal=(Button)layout.findViewById(R.id.music_bt_local);
		btMusicLine=(Button)layout.findViewById(R.id.music_bt_line);
		btMusicSearch=(ImageButton) layout.findViewById(R.id.music_bt_search);
		btPlayOrPause=(ImageButton)layout.findViewById(R.id.music_menu_play);
		tvNowTime=(TextView)layout.findViewById(R.id.music_menu_nowtime);
		ImgIco=(ImageView)layout.findViewById(R.id.music_menu_album_ico);
		tvTitle=(TextView)layout.findViewById(R.id.music_menu_title);
		SeekBar=(ProgressBar)layout.findViewById(R.id.music_menu_bar);
	}
	private void loadListener() {
		// TODO Auto-generated method stub
		btMusicLocal.setOnClickListener(this);
		btMusicLine.setOnClickListener(this);
		btMusicSearch.setOnClickListener(this);
		tvNowTime.setOnClickListener(this);
		ImgIco.setOnClickListener(this);
		btPlayOrPause.setOnClickListener(this);
	}
	private void laodDate() {
		// TODO Auto-generated method stub
		app=(App) getActivity().getApplication();
		show(new MusicLocalFram());
		IntentFilter filter=new IntentFilter();
		filter.addAction(Control.NOW_PLAY);
		filter.addAction(Control.NOW_PAUSE);
		filter.addAction(Control.UPDSTE_NOW_TIME);
		receiver=new PlayBroad();
		getActivity().registerReceiver(receiver, filter);
		show(new MusicLocalFram());
		enabled(btMusicLocal);	
		Log.i("123", "menu");
	}
	
	private void show(Fragment fms){
		FragmentManager fm = getFragmentManager();  
		// 开启Fragment事务  
		FragmentTransaction tr = fm.beginTransaction();
		tr.replace(R.id.music_fram, fms);
		tr.commit();
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.music_bt_local:
			show(new MusicLocalFram());
			enabled(v);	
			break;
			//本地
		case R.id.music_bt_line:
			show(new MusicLineFram());
			enabled(v);	
			break;
			//网络
		case R.id.music_bt_search:
			startActivity(new Intent(getActivity(),SearchActivity.class));
			break;
			//搜索  单独一个页面

		case R.id.music_menu_play:
			intent.setAction(Control.PLAY_OR_PAUSE);
			getActivity().sendBroadcast(intent);
			break;
		case R.id.music_menu_album_ico:
			startActivity(new Intent(getActivity(),MusicPlaying.class));
		}
	}

	private void enabled(View v) {
		btMusicLocal.setEnabled(true);
		btMusicLine.setEnabled(true);
		v.setEnabled(false);
	}  
	public class PlayBroad extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Control.NOW_PLAY)){
				fixed();
				btPlayOrPause.setImageResource(R.drawable.ic_music_menu_stop_);
			}else if(intent.getAction().equals(Control.NOW_PAUSE)){
				btPlayOrPause.setImageResource(R.drawable.ic_music_menu_play);
			}else if(intent.getAction().equals(Control.UPDSTE_NOW_TIME)){
				int time=intent.getIntExtra("now_time", 0);
				tvNowTime.setText(DateTimeUtils.getDateFormat(time));
				SeekBar.setProgress(time);
			}
		}
	}
	void fixed(){
		if(app.getMusic()==null)return;
		Music music = app.getMusic();
		SeekBar.setMax(music.getFile_duration());
		tvTitle.setText(music.getTitle());
		String pic=music.getPic_small();
		if(pic==null||pic.equals("")){
			if(music.getSongInfo()==null){
				return;
			}else{
				pic=music.getSongInfo().getPic_small();
				if(pic==null||pic.equals(""))
				return;
			}
		}
		String path =pic.substring(pic.lastIndexOf("/"));
		File file = new File(getActivity().getCacheDir(), "images"+path);
		BitmapUtils.loadBitmap(file,pic,new BitmapCallback() {
			@Override
			public void onBitmapLoaded(Bitmap result) {
				// TODO Auto-generated method stub
				if(result!=null){
					ImgIco.setImageBitmap(result);
					RotateAnimation anim = new RotateAnimation(0, 360, ImgIco.getWidth()/2, ImgIco.getHeight()/2);
					anim.setDuration(10000);
					//匀速旋转
					anim.setInterpolator(new LinearInterpolator());
					//无限重复
					anim.setRepeatCount(Animation.INFINITE);
					ImgIco.startAnimation(anim);
				}
			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		getActivity().unregisterReceiver(receiver);
		intent.setAction(Control.BACK);
		getActivity().sendBroadcast(intent);
		super.onDestroy();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub

		super.onStop();
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method s..tub
		super.onPause();
		intent=new Intent() ;
		intent.setAction(Control.ACTIVITY_INBACK);
		getActivity().sendBroadcast(intent);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		fixed();
		SeekBar.setProgress(app.getNowTime());
		tvNowTime.setText(DateTimeUtils.getDateFormat(app.getNowTime()));
		intent.setAction(Control.ACTIVITY_NOBACK);
		getActivity().sendBroadcast(intent);
		super.onResume();
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onPageSelected(int arg0) {
		switch(arg0){
		case 0:
			enabled(btMusicLocal);
			break;
		case 1:
			enabled(btMusicLine);
			break;
		}

	}
}
