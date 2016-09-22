package com.zyj.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.zyj.example.Video;

public class VideoDao  implements IDao<Video>{
	ContentResolver cr;
	
	public VideoDao(ContentResolver cr) {
		super();
		this.cr = cr;
	}

	@Override
	public List<Video> getMedias(String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		List<Video> videos=new ArrayList<Video>();
		Uri uri=MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
		String[] projection={"_data","title","duration","width","height"};
		Cursor csdb = cr.query(uri, projection, selection, selectionArgs, null);
		if(csdb.moveToFirst()){
			do{
				Log.i("123", csdb.getString(0));
				videos.add(new Video(csdb.getString(0),
						csdb.getString(1),
						csdb.getInt(2),
						csdb.getInt(3), 
						csdb.getInt(4)));
			}while(csdb.moveToNext());
		}
		return videos;
	}

}
