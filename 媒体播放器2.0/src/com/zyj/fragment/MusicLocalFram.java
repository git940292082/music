package com.zyj.fragment;

import java.util.List;
import com.zyj.adapter.MusicAdapter;
import com.zyj.app.App;
import com.zyj.example.Music;
import com.zyj.utils.Control;
import com.zyj.zyj.R;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
public class MusicLocalFram extends Fragment {
	private View layout;
	private ListView lvMusic;
	private MusicAdapter adapter;
	private PositionBroad receiver;
	private int Position=-1;
	private List<Music> musics;
	private App app;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
		if(layout==null){
			layout= inflater.inflate(R.layout.music_local_fram, container, false);
		}else{
			((ViewGroup) layout.getParent()).removeView(layout); 
		}
		loadview();
		loadlietner();
		loaddate();
		return layout;  
	}
	//初始化控件
	private void loadview() {
		lvMusic=(ListView) layout.findViewById(R.id.music_local_list);
		
	}
	//初始化监听器
	private void loadlietner() {
		Listener listener=new Listener();
		lvMusic.setOnItemClickListener(listener);
	}
	private void loaddate() {
		app=(App) getActivity().getApplication();
		musics=App.mapMusics.get(666);
		adapter=new MusicAdapter(getActivity(), musics);
		lvMusic.setAdapter(adapter);
		IntentFilter filter=new IntentFilter();
		filter.addAction(Control.NOW_PLAY);
		receiver=new PositionBroad();
		getActivity().registerReceiver(receiver, filter);
		Log.i("123", "menu");
		fixed();
	}
	public class Listener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(position!=Position){
				Intent intent=new Intent();
				intent.setAction(Control.PLAY_POSITION);
				intent.putExtra("play_position", position);
				intent.putExtra("lien_name",666);
				getActivity().sendBroadcast(intent);
			}
		}
	}
	public class PositionBroad extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Control.NOW_PLAY)){
				fixed();
			}
		}
	}
	
	void fixed(){
		int position=app.getPosition();
		int mode=app.getMusicMode();
		if(musics.isEmpty()){
			return;
		}
		for (Music music : musics) {
			music.setPlaying(false);
		}
		adapter.notifyDataSetChanged();
		if(mode==666){
			Log.i("123", position+mode+"");
			musics.get(position).setPlaying(true);
			adapter.notifyDataSetChanged();
		}
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		getActivity().unregisterReceiver(receiver);
		super.onDestroy();
	}
}

