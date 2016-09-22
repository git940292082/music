package com.zyj.fragment;

import com.zyj.adapter.VideoAdapter;
import com.zyj.app.App;
import com.zyj.zyj.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class VideoMenuFram extends Fragment implements OnItemClickListener {
	private View layout;
	private Button bt;
	private TextView tv;
	private ListView lvVideo;
	private VideoAdapter adapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
		if(layout==null){
			layout= inflater.inflate(R.layout.video_menu_fram, container, false);
		}else{
			  ((ViewGroup) layout.getParent()).removeView(layout); 
		}
		return layout;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}   
}
