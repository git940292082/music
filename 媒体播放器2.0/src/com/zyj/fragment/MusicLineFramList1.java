package com.zyj.fragment;
import java.util.List;

import com.zyj.adapter.LineMusicAdapter;
import com.zyj.app.App;
import com.zyj.example.Music;
import com.zyj.model.IOnMusicLoaded;
import com.zyj.model.MusicModel;
import com.zyj.utils.Control;
import com.zyj.zyj.R;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
@SuppressLint("ValidFragment")
public class MusicLineFramList1 extends Fragment implements OnItemClickListener,OnScrollListener{
	private View layout;
	private ListView lvMusics;
	private LineMusicAdapter adapter;
	private List<Music> musics;
	private String title;
	private int type;
	private int offset;
	private View loading;
	private TextView tv;
	private MusicModel model;
	private Intent i=new Intent();
	private App app;
	private BroadcastReceiver receiver;
	public MusicLineFramList1(String string, int i, int j) {
		// TODO Auto-generated constructor stub
		title=string;
		type=i;
		offset=j;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(layout==null){
			layout= inflater.inflate(R.layout.music_line_fram_list,null);
		}else{
			((ViewGroup) layout.getParent()).removeView(layout); 
		}
		loadView();
		loadLitener();
		loadData();
		return layout;
	}

	private void loadView() {
		// TODO Auto-generated method stub
		lvMusics=(ListView)layout.findViewById(R.id.music_line_farm_list);
		loading=(ProgressBar)layout.findViewById(R.id.music_line_fram_loading);
		tv = (TextView)layout.findViewById(R.id.music_line_farm_tv_title);
	}
	private void loadLitener() {
		// TODO Auto-generated method stub
		lvMusics.setOnItemClickListener(this);
		lvMusics.setOnScrollListener(this);
	}

	private void loadData() {
		// TODO Auto-generated method stub
		app=(App) getActivity().getApplication();
		IntentFilter filter=new IntentFilter();
		filter.addAction(Control.NOW_PLAY);
		receiver=new PositionBroad();
		getActivity().registerReceiver(receiver, filter);
		tv.setText(title);
		lvMusics.setEmptyView(loading);
		musics=App.mapMusics.get(type);
		model=new MusicModel();
		if(musics==null){
			mode();

		}else{
			adapter=new LineMusicAdapter(getActivity(),musics);
			lvMusics.setAdapter(adapter);
			fixed();
		}
	}
	private void mode() {
		// TODO Auto-generated method stub
		model.loadNewMusic(type,offset,30,new IOnMusicLoaded() {
			@Override
			public void OnMusicLoaded(List<Music> musics) {
				// TODO Auto-generated method stub
				if(musics==null){
					Toast.makeText(getActivity(), "连接网络失败，请检查网络设置", Toast.LENGTH_SHORT).show();
					lvMusics.setEmptyView(null);
					return;
				}
				adapter=new LineMusicAdapter(getActivity(),musics);
				lvMusics.setAdapter(adapter);          
				MusicLineFramList1.this.musics=musics;
				App.mapMusics.put(type,musics);
				// TODO: handle exception
				fixed();
			}
		});
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
		// TODO Auto-generated method stub
		show(arg2);
	}
	
	protected void show(int arg2) {
		i.setAction(Control.PLAY_POSITION);
		i.putExtra("play_position", arg2);
		i.putExtra("lien_name",type);
		getActivity().sendBroadcast(i);
	}
	public class PositionBroad extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Control.NOW_PLAY)){
				Log.i("123", "MusicLineFramList1");
				fixed();
			}
		}
	}
	private void fixed() {
		// TODO Auto-generated method stub
		int position=app.getPosition();
		int mode=app.getMusicMode();
		if(mode==type){
			for (Music music : musics) {
				music.setPlaying(false);
			}
			Log.i("123", position+mode+"");
			musics.get(position).setPlaying(true);
			adapter.notifyDataSetChanged();
		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		getActivity().unregisterReceiver(receiver);
		super.onDestroy();
	}

	boolean isBotton=false;
	boolean isResitying=false;
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if(firstVisibleItem+visibleItemCount==totalItemCount){
				isBotton=true;
		}else{
			isBotton=false;
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if(scrollState==OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
			if(isBotton&&!isResitying){
				isResitying=true;
				Log.i("123", "到底了"+musics.size());
				model.loadNewMusic(type,musics.size(),20,new IOnMusicLoaded() {
					@Override
					public void OnMusicLoaded(List<Music> music) {
						// TODO Auto-generated method stub
						if(musics!=null){
							musics.addAll(music);
							adapter.notifyDataSetChanged();
							app.mapMusics.put(type, musics);
						}else{
							Toast.makeText(getActivity(), "没有数据了",Toast.LENGTH_SHORT ).show();
						}
						isResitying=false;
					}
				});
			}
		}
	}
}
