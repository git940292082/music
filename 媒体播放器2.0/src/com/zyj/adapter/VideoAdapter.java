package com.zyj.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.zyj.example.Video;
import com.zyj.utils.DateTimeUtils;
import com.zyj.zyj.R;

public class VideoAdapter  extends MyAdapter<Video>{
	public VideoAdapter(Context context, List<Video> data) {
		super(context, data);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return getData().get(position);
	}
	@Override
	public View getView(int position, View layout, ViewGroup parent) {
		// TODO Auto-generated method stub
		Video video=(Video) getItem(position);
		HolderView holder=null;
		if(layout==null){
			layout=getInflater().inflate(R.layout.video_item,null);
			holder=new HolderView();
			holder.tvTitle=(TextView)layout.findViewById(R.id.video_tv_title);
			holder.tvDuraction=(TextView)layout.findViewById(R.id.video_tv_duration);
			layout.setTag(holder);
		}else{
			holder=(HolderView) layout.getTag();
		}
		holder.tvTitle.setText(video.getName());
		holder.tvDuraction.setText(DateTimeUtils.getDateFormat(video.getDurrction()));
		return layout;
	}
	protected class HolderView{
		TextView tvTitle;
		TextView tvDuraction;
	}
}
