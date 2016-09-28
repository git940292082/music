package com.zyj.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;



import com.zyj.app.App;

import android.annotation.TargetApi;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
public class BitmapUtils  {
	public static Bitmap loadBitmap(InputStream in,int width,int height,String path) throws Exception{
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		byte[] buff=new byte[1024*10];
		int length = 0;
		while((length=in.read(buff)) != -1){
			bos.write(buff,0,length);
			bos.flush();
		}
		byte[] buffs = bos.toByteArray();
		bos.close();
		Options opts=new Options();
		opts.inJustDecodeBounds=true;
		BitmapFactory.decodeByteArray(buffs, 0, buffs.length, opts);
		int w=opts.outWidth/width;
		int h=opts.outHeight/height;
		int min=w>h?w:h;
		opts.inSampleSize=min;
		opts.inJustDecodeBounds=false;
		Bitmap bm=BitmapFactory.decodeByteArray(buffs, 0, buffs.length,opts);
		return bm;
	}
	public static void saveBitmap(Bitmap b,File f) throws Exception{
		if(!f.getParentFile().exists()){
			f.getParentFile().mkdirs();
		}
		FileOutputStream out=new FileOutputStream(f);
		b.compress(CompressFormat.JPEG, 100, out);
		
	}
	public static Bitmap loadBitmap(File f) {
		if(!f.exists()){
			return null;
		}
		Bitmap b=BitmapFactory.decodeFile(f.getAbsolutePath());
		return b;
	}
	public static void loadBitmap(final File file,final String url,final BitmapCallback callback){
		AsyncTask<String, String, Bitmap> task = new AsyncTask<String, String, Bitmap>(){
			protected Bitmap doInBackground(String... params) {
				try {
					//先从文件缓存中找，是否已经下载过
					Bitmap b = loadBitmap(file);
					if(b!=null){ //文件里有
						return b;
					}
					InputStream is = HttpUntils.getInputStream(url);
					b=BitmapFactory.decodeStream(is);
					//把图片存入文件缓存
					saveBitmap(b, file);
					return b;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			protected void onPostExecute(Bitmap result) {
				callback.onBitmapLoaded(result);
			}
		};
		task.execute();
	}
	public static Map<ImageView, AsyncTask<String, String, Bitmap>> mapAsy=new HashMap<ImageView, AsyncTask<String,String,Bitmap>>();
	public static void loadBitmap(final String url,final ImageView imgBg){
		AsyncTask<String, String, Bitmap> task = mapAsy.get(imgBg);
		if(task!=null){
			Log.i("123", "取消取消取消取消取消取消取消取消取消取消取消取消取消取消取消取消取消");
			task.cancel(true);
		}
		task = new AsyncTask<String, String, Bitmap>(){
			protected Bitmap doInBackground(String... params) {
				try {
					//先从文件缓存中找，是否已经下载过
					String cacheName	 =url.substring(url.lastIndexOf("/"));
					File file = new File(App.context.getCacheDir(), "images"+cacheName);
					Bitmap b = loadBitmap(file);
					if(b!=null){ //文件里有
						Log.i("123", "从文件中读取");
						return b;
					}
					InputStream is = HttpUntils.getInputStream(url);
					b=BitmapFactory.decodeStream(is);
					//把图片存入文件缓存
					saveBitmap(b, file);
					Log.i("123", "下载的");
					return b;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
			protected void onPostExecute(Bitmap result) {
				if(result!=null){
					imgBg.setImageBitmap(result);
				}
			}
		};
		task.execute();
		mapAsy.put(imgBg, task);
	}
}
