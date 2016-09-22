package com.zyj.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zyj.example.Lrc;
import com.zyj.zyj.R;

public class LrcAdapter extends MyAdapter<Lrc> {
	public LrcAdapter(Context context, List<Lrc> data) {
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
		// TODO Auto-generated method stub
		if(layout==null){
			if(getItemViewType(position)==0){
				layout=getInflater().inflate(R.layout.lrc_item_true, null);
			}else{
				layout=getInflater().inflate(R.layout.lrc_item, null);
			}
		}
		TextView tv = (TextView)layout.findViewById(R.id.lrc_item_tv);
		tv.setText(getData().get(position).getContext());
		return layout;
	}
}
