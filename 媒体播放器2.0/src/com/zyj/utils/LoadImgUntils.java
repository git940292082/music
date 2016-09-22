package com.zyj.utils;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import com.zyj.app.App;
import com.zyj.zyj.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class LoadImgUntils {
	private static Map<View, AsyncTask<String,Void,Bitmap>> mapLoadImage=new HashMap<View, AsyncTask<String,Void,Bitmap>>();
	public static  void loadImg(final ImageView img , final String imgPath,final Context context){
		SoftReference<Bitmap> ref=App.mapPic.get(imgPath);
		if(ref!=null){
			Bitmap b=ref.get();
			if(b!=null){
				img.setImageBitmap(b);
				Log.i("info", "图片是从内存缓存中读取的...");
				return;
			}
		}
		String path = imgPath.substring(imgPath.lastIndexOf("/"));
		File file = new File(context.getCacheDir(), "images"+path);
		Bitmap b=BitmapUtils.loadBitmap(file);
		if(b!=null){
			Log.i("info", "图片是从文件缓存中读取的...");
			App.mapPic.put(imgPath, new SoftReference<Bitmap>(b));
			img.setImageBitmap(b);
			return;
		}
		AsyncTask<String,Void,Bitmap> PictureThread = mapLoadImage.get(img);
		if(PictureThread!=null){
			PictureThread.cancel(true);
		}
		PictureThread=new AsyncTask<String, Void, Bitmap>(){
			@Override
			protected Bitmap doInBackground(String... path) {
				// TODO Auto-generated method stub
				Bitmap bm = getbm(imgPath,context);
				if(bm==null){
					img.setImageResource(R.drawable.ic_ico);
				}
				return bm;
			}
			@Override
			protected void onPostExecute(Bitmap result) {
				// TODO Auto-generated method stub
				img.setImageBitmap(result);
			}
		};
		PictureThread.execute();
		mapLoadImage.put(img,PictureThread);
	}
	class LoadImage{
		String path;
		Bitmap bm;
		ImageView img;
	}
	public static Bitmap getbm(String path,Context context){
		try {
			InputStream in=HttpUntils.getInputStream(path);
			Bitmap b = BitmapUtils.loadBitmap(in,50, 50,path);
			App.mapPic.put(path, new SoftReference<Bitmap>(b));
			String f = path.substring(path.lastIndexOf("/"));
			File file = new File(context.getCacheDir(), "images"+f);
			BitmapUtils.saveBitmap(b, file);
			return b;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
