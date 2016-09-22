package com.zyj.adapter;

import java.util.List;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zyj.example.Music;
import com.zyj.utils.DateTimeUtils;
import com.zyj.zyj.R;

public class MusicAdapter extends MyAdapter<Music>{
	public MusicAdapter(Context context, List<Music> data) {
		super(context, data);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return getData().get(position).isPlaying()?0:1;
	}
	
	@Override
	public View getView(int position, View layout, ViewGroup parent) {
		Music music=getData().get(position);
		HolerView holdView=new HolerView();
		if(layout==null){
			if(getItemViewType(position)==0){
				layout=getInflater().inflate(R.layout.music_local_list_playing, null);
			}else{
				layout=getInflater().inflate(R.layout.music_local_list, null);
			}
			holdView.tvTitle=(TextView) layout.findViewById(R.id.music_local_list_name);
			holdView.tvArtist=(TextView) layout.findViewById(R.id.music_local_list_people);
			holdView.tvDuraction=(TextView) layout.findViewById(R.id.music_local_list_sumtime);
			layout.setTag(holdView);
		}else{
			holdView=(HolerView) layout.getTag();
		}
		holdView.tvTitle.setText(music.getTitle());
		holdView.tvArtist.setText(music.getAuthor());
		holdView.tvDuraction.setText(DateTimeUtils.getDateFormat(music.getFile_duration()));
		return layout;
	}
	
	protected class HolerView {
		TextView tvTitle;
		TextView tvArtist;
		TextView tvDuraction;
	}

}
