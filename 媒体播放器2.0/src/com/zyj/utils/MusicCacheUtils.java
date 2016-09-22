package com.zyj.utils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import android.os.AsyncTask;

public class MusicCacheUtils {
	public static void loadMusic(final File file,final String url){
		AsyncTask<String, String, InputStream> task = new AsyncTask<String, String, InputStream>(){
			protected InputStream doInBackground(String... params) {
				try {
					//先从文件缓存中找，是否已经下载过
					if(file.exists()){
						FileInputStream fin=new FileInputStream(file);
						return fin;
					}
					
					InputStream in=HttpUntils.getInputStream(url);
					FileOutputStream fout=new FileOutputStream(file);
					BufferedInputStream read=new BufferedInputStream(in);
					byte [] byt=new byte[1024*8];
					int length;
					while((length=read.read(byt))!=-1){
						fout.write(byt, 0, length);
						fout.flush();
					}
					//把图片存入文件缓存
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			protected void onPostExecute(InputStream result) {
			}
		};
		task.execute();
	}
}
