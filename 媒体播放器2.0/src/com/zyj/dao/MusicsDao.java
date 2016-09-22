package com.zyj.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.zyj.example.Music;
public class MusicsDao implements IDao<Music>{
	ContentResolver cr;

	public MusicsDao(ContentResolver cr) {
		super();
		this.cr = cr;
	}
	public List<Music> getMedias(String selection,String[] selectionArgs) {
		List<Music> listMusic=new ArrayList<Music>();
		Music music;
		Uri uri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		String[] projection={
				"_data",//歌曲路径
				"title", //歌曲标题
				"duration",//歌曲总时常
				"artist",//歌手
				"album",//专辑
				"album_key"};
		Cursor csdb = cr.query(uri, projection, selection, selectionArgs,null);
		if(csdb.moveToFirst()){
			do{
				if(csdb.getInt(2)>60000){
					Log.i("123", csdb.getString(0));
					music=new Music();
					music.setPath(csdb.getString(0));
					music.setTitle(csdb.getString(1));
					music.setFile_duration(csdb.getInt(2));
					music.setAuthor(csdb.getString(3));
					music.setAlbum_title(csdb.getString(4));
					music.setAlbumKey(csdb.getString(5));
					music.setAlbumPicture(getAlbumArt(music.getAlbumKey()));
					Log.i("", music.getTitle());
					listMusic.add(music);
				}
			
			}while(csdb.moveToNext());
		}
		
		csdb.close();
		return listMusic;
		
	}
	private String getAlbumArt(String album_key){
		String s=null;
		if(album_key!=null){
			Uri uri=MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
			String[] projection={"album_art"};
			String selection="album_key=?";
			String[] selectionArgs={album_key};
			
			String sortOrder=null;
			Cursor csdb = cr.query(uri, projection, selection, selectionArgs, sortOrder);
			if(csdb.moveToFirst()){
				s=csdb.getString(0);
			}
			Log.i("123", s+"");
			csdb.close();
		}
		return s;
	}

}
