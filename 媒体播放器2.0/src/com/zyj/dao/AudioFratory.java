package com.zyj.dao;

import android.content.ContentProvider;
import android.content.ContentResolver;

import com.zyj.example.Music;
import com.zyj.example.Pictures;
import com.zyj.example.Video;

public abstract class AudioFratory {
	public static IDao<Music> getMusics(ContentResolver cr){
		return new MusicsDao(cr);
	}
	public static IDao<Pictures> getPictures(ContentResolver cr){
		return new PicturesDao(cr);
		
	}
	public static IDao<Video> getVideos(ContentResolver cr){
		return new VideoDao(cr);
	}
}
