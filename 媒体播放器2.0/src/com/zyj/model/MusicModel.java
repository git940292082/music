package com.zyj.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.zyj.app.App;
import com.zyj.example.Lrc;
import com.zyj.example.Music;
import com.zyj.example.SongInfo;
import com.zyj.example.SongUrl;
import com.zyj.utils.HttpUntils;
import com.zyj.utils.JsonParser;
import com.zyj.utils.UrlFactory;
import com.zyj.utils.XmlParser;
import android.os.AsyncTask;
import android.util.Log;
public class MusicModel {
	AsyncTask<String , String, List<Music>> asy;
	public void loadNewMusic(final int type,final int offset,final int size,final IOnMusicLoaded onMusicLoaded){
		asy=App.asy;
		if(asy!=null){
			asy.cancel(true);
		}
		asy=new AsyncTask<String, String, List<Music>>(){
			@Override
			protected List<Music> doInBackground(String... params) {
				// TODO Auto-generated method stub
				
				String musicUrl=UrlFactory.getMusicUrl(type,offset, size);
				try {
					InputStream in=HttpUntils.getInputStream(musicUrl);
					List<Music> musics=XmlParser.parserMusics(in);
					return musics;
				} catch (Exception e) {
					// TODO Auto-generated catch block
				}
				return null;
			}
			@Override
			protected void onPostExecute(List<Music> result) {
				// TODO Auto-generated method stub
				onMusicLoaded.OnMusicLoaded(result);
			}
		};
		asy.execute();
		App.asy=asy;
	}
	
	public void getSongIdInfo(final String songId,final OnSongIdInfo songidInfo) {
		// TODO Auto-generated method stub
		new AsyncTask<String, String,Music>(){
			@Override
			protected Music doInBackground(String... params) {
				// TODO Auto-generated method stub
				String songUrl=UrlFactory.getSongIdUrl(songId);
				try {
					InputStream in=HttpUntils.getInputStream(songUrl);
					String jsons=HttpUntils.intoString(in);
					JSONObject josn=new JSONObject(jsons);
					JSONArray urlarr=josn.getJSONObject("songurl").getJSONArray("url");
					JSONObject jsonInfo=josn.getJSONObject("songinfo");
					List<SongUrl> songUrls=JsonParser.parserUrl(urlarr);
					SongInfo songInfo=JsonParser.ParserInfo(jsonInfo);
					Music music=new Music();
					music.setSongUrls(songUrls);
					music.setSongInfo(songInfo);
					return music;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
				
			}
			@Override
			protected void onPostExecute(Music result) {
				// TODO Auto-generated method stub
				songidInfo.onSongIdInfo(result);
			}
		}.execute();
	}
	public void loadLrc(final String lrclink, final LrcCallBack lrcCallBack)  {
		// ���ظ��
		new AsyncTask<String, String, List<Lrc>>(){
			@SuppressWarnings("resource")
			@Override
			protected List<Lrc> doInBackground(String... params) {
				// TODO Auto-generated method stub
				if(lrclink==null||lrclink.equals(""))return null;
				List<Lrc> lrc=new ArrayList<Lrc>();
				try {
					//������ʻ����ļ�����
					String filename = lrclink.substring(lrclink.lastIndexOf("/"));
					File file = new File(App.context.getCacheDir(), "lrc"+filename);
					PrintWriter out = null;
					if(!file.getParentFile().exists()){ //��Ŀ¼������
						file.getParentFile().mkdirs();
					}
					InputStream in = null;
					boolean isFromFile=false;
					if(file.exists()){ //����Ŀ¼���Ѿ����ع������
						in = new FileInputStream(file);
						isFromFile = true;
					}else{// ����Ŀ¼��û�������ظ��
						in = HttpUntils.getInputStream(lrclink);
						out = new PrintWriter(file);
						isFromFile = false;
					}
					BufferedReader bfr=new BufferedReader(new InputStreamReader(in));
					String line=null;
					while((line=bfr.readLine())!=null){
						
						if(!isFromFile){
							//��line ���浽��ʻ����ļ���
							out.println(line);
							out.flush();
						}
						if(!line.startsWith("[") || !line.contains(":") || !line.contains(".")){
							continue;
						}
						Log.i("213", line);
						String time = line.substring(1, line.indexOf("."));
						String content = line.substring(line.lastIndexOf("]")+1);
						lrc.add(new Lrc(time,content));
					}
					return lrc;
				}catch(Exception e){
				}
				return null;
			}
				protected void onPostExecute(java.util.List<Lrc> result) {
					lrcCallBack.lrcDownLoad(result);
				};
		}.execute();
	}
	//����
	public void loadNewMusic(final String search,final IOnMusicLoaded onMusicLoaded){
		asy=App.asy;
		if(asy!=null){
			asy.cancel(true);
		}
		asy=new AsyncTask<String, String, List<Music>>(){
			@Override
			protected List<Music> doInBackground(String... params) {
				// TODO Auto-generated method stub
				Log.i("123", search);
				String musicUrl=UrlFactory.getSerachUrl(search);
				try {
					InputStream in=HttpUntils.getInputStream(musicUrl);
					List<Music> musics=XmlParser.parserMusics(in);
					return musics;
				} catch (Exception e) {
					// TODO Auto-generated catch block
				}
				return null;
			}
			@Override
			protected void onPostExecute(List<Music> result) {
				// TODO Auto-generated method stub
				onMusicLoaded.OnMusicLoaded(result);
			}
		};
		asy.execute();
		App.asy=asy;
	}
}
