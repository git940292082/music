package com.zyj.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.zyj.example.SongInfo;
import com.zyj.example.SongUrl;
import com.zyj.example.Video;
public class JsonParser {

	public static List<SongUrl> parserUrl(JSONArray urlarr) throws Exception {
		// TODO Auto-generated method stub
		List<SongUrl> urls=new ArrayList<SongUrl>();
		for(int i=0;i<urlarr.length();i++){
			JSONObject json=urlarr.getJSONObject(i);
			SongUrl url=new SongUrl(
					json.getInt("song_file_id"),
					json.getInt("file_size"),
					json.getInt("file_duration"),
					json.getInt("file_bitrate"),
					json.getString("show_link"), 
					json.getString("file_extension"), 
					json.getString("file_link"));
			urls.add(url);
		}
		return urls;
	}
	public static List<Video> getVideo(String js) throws Exception{
		List<Video> videos=new ArrayList<Video>();
		JSONObject json=new JSONObject(js);
		JSONArray jsr=json.getJSONArray("V9LG4B3A0");
		for(int i=0;i<jsr.length();i++){
			JSONObject jo=jsr.getJSONObject(i);
			Video video=new Video(
					jo.getString("topicImg"),
					jo.getString("videosource"), 
					jo.getString("mp4Hd_url"), 
					jo.getString("topicDesc"),
					jo.getString("cover"), 
					jo.getString("title"),
					jo.getString("mp4_url"), 
					jo.getString("ptime"), 
					jo.getString("topicName"));
			videos.add(video);
		}
		return videos;

	}
	public static SongInfo ParserInfo(JSONObject jsonInfo) throws Exception{
		// TODO Auto-generated method stub

		SongInfo info=new SongInfo(
				jsonInfo.getString("pic_huge"),
				jsonInfo.getString("album_1000_1000"), 
				jsonInfo.getString("album_500_500"), 
				jsonInfo.getString("compose"),
				jsonInfo.getString("artist_500_500"), 
				jsonInfo.getString("file_duration"),
				jsonInfo.getString("album_title"),
				jsonInfo.getString("title"), 
				jsonInfo.getString("pic_radio"),
				jsonInfo.getString("language"),
				jsonInfo.getString("lrclink"), 
				jsonInfo.getString("pic_big"), 
				jsonInfo.getString("pic_premium"),
				jsonInfo.getString("artist_480_800"), 
				jsonInfo.getString("artist_id"), 
				jsonInfo.getString("album_id"), 
				jsonInfo.getString("artist_1000_1000"), 
				jsonInfo.getString("all_artist_id"), 
				jsonInfo.getString("artist_640_1136"), 
				jsonInfo.getString("publishtime"), 
				jsonInfo.getString("share_url"), 
				jsonInfo.getString("author"), 
				jsonInfo.getString("pic_small"), 
				jsonInfo.getString("song_id"));

		return info;
	}

}
