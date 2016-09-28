package com.zyj.fragment;
import java.util.ArrayList;
import java.util.List;

import com.zyj.adapter.VideoAdapter;
import com.zyj.example.Video;
import com.zyj.model.IVideoCallback;
import com.zyj.model.VideoModel;
import com.zyj.zyj.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
public class FramVideo extends Fragment {
	private View view;
	private ListView lv;
	private VideoAdapter adapter;
	private List<Video> Videos=new ArrayList<Video>();
	private boolean isResing;
	protected boolean isBotton;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(view==null){
			view=inflater.inflate(R.layout.fram_video, null);
			Log.i("123", "FramVideo");
			loadView();
			loadListener();
			loadData();
		}else{
			
			((ViewGroup) view.getParent()).removeView(view); 
		}
		
		return view;
	}
	private void loadView() {
		// TODO Auto-generated method stub
		lv=(ListView)view.findViewById(R.id.fram_video_lv);
	}
	private void loadListener() {
		// TODO Auto-generated method stub
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
			}
		});
		lv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if(scrollState==OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
					if(isBotton&&isResing){
						isResing=false;
						Log.i("123", "µ½µ×ÁË"+Videos.size());
						VideoModel model=new VideoModel();
						model.loadVideo(Videos.size(), new IVideoCallback() {
							@Override
							public void showVideo(List<Video> videos) {
								// TODO Auto-generated method stub
								isResing=true;
								Videos.addAll(videos);
								adapter.notifyDataSetChanged();
							}
						});					
					}
				}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(firstVisibleItem+visibleItemCount==totalItemCount){
					isBotton=true;
				}else{
					isBotton=false;
				}
			}
		});
	}
	private void loadData() {
		// TODO Auto-generated method stub
		VideoModel model = new VideoModel();
		isResing=false;
		model.loadVideo(0, new IVideoCallback() {
			@Override
			public void showVideo(List<Video> videos) {
				// TODO Auto-generated method stub
				Videos=videos;
				adapter=new VideoAdapter(getActivity(), videos);
				isResing=true;
				lv.setAdapter(adapter);
				
			}
		});
	}
}
