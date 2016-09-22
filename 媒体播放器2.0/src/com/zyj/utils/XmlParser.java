package com.zyj.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

import com.zyj.example.Music;

public class XmlParser  {
	public static List<Music> parserMusics(InputStream in) throws Exception{ 
		List<Music> musics=new ArrayList<Music>();
		XmlPullParser xpp=Xml.newPullParser();
		xpp.setInput(in,"utf-8");
		int type=xpp.getEventType();
		Music music=null;
		while(type!=XmlPullParser.END_DOCUMENT){
			switch(type){
			case XmlPullParser.START_TAG:
				String name=xpp.getName();
				if(name.equals("song")){
					music=new Music();
					musics.add(music);
				}else if(name.equals("artist_id")){
					music.setArtist_id(xpp.nextText());
				}else if(name.equals("language")){
					music.setLanguage(xpp.nextText());
				}else if(name.equals("pic_big")){
					music.setPic_big(xpp.nextText());
				}else if(name.equals("pic_small")){
					music.setPic_small(xpp.nextText());
				}else if(name.equals("lrclink")){
					music.setLrclink(xpp.nextText());
				}else if(name.equals("hot")){
					music.setHot(xpp.nextText());
				}else if(name.equals("all_artist_id")){
					music.setAll_artist_id(xpp.nextText());
				}else if(name.equals("style")){
					music.setStyle(xpp.nextText());
				}else if(name.equals("song_id")){
					music.setSong_id(xpp.nextText());
				}else if(name.equals("title")){
					music.setTitle(xpp.nextText());
				}else if(name.equals("ting_uid")){
					music.setTing_uid(xpp.nextText());
				}else if(name.equals("author")){
					music.setAuthor(xpp.nextText());
				}else if(name.equals("album_id")){
					music.setAlbum_id(xpp.nextText());
				}else if(name.equals("album_title")){
					music.setAlbum_title(xpp.nextText());
				}else if(name.equals("artist_name")){
					music.setArtist_name(xpp.nextText());
				}else if(name.equals("file_duration")){
					music.setFile_duration(Integer.parseInt(xpp.nextText())*1000);
				}
			}
			type=xpp.next();
		}
	
		return musics;
	}
}
