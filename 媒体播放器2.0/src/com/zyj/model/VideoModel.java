package com.zyj.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.zyj.example.Video;
import com.zyj.utils.HttpUntils;
import com.zyj.utils.JsonParser;
import com.zyj.utils.UrlFactory;

import android.os.AsyncTask;
import android.util.Log;
public class VideoModel {
	public void  loadVideo(final int startIndex,final IVideoCallback videoCallback) { 
		AsyncTask<String, String, List<Video>> asy=new AsyncTask<String, String, List<Video>>(){
			@Override
			protected List<Video> doInBackground(String... params) {
				// TODO Auto-generated method stub
				try {
					String url=UrlFactory.getVideoUrl(startIndex);
					InputStream in=HttpUntils.getInputStream(url);
					String js=HttpUntils.intoString(in);
					List<Video> videos=JsonParser.getVideo(js);
					return videos;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
			@Override
			protected void onPostExecute(List<Video> result) {
				// TODO Auto-generated method stub
				if(result==null){
					result=new ArrayList<Video>();
				}
				Log.i("123", result.toString());
				videoCallback.showVideo(result);
			}
		};
		asy.execute();
	}
}
