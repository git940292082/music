package com.zyj.dao;

import android.content.ContentResolver;
import com.zyj.example.Music;
import com.zyj.example.Pictures;
public abstract class AudioFratory {
	public static IDao<Music> getMusics(ContentResolver cr){
		return new MusicsDao(cr);
	}
	public static IDao<Pictures> getPictures(ContentResolver cr){
		return new PicturesDao(cr);
		
	}
}
