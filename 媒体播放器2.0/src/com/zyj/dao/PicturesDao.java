package com.zyj.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.zyj.example.Pictures;

public class PicturesDao implements IDao<Pictures>{

	private ContentResolver cr;
	
	public PicturesDao(ContentResolver cr) {
		super();
		this.cr = cr;
	}
	@Override
	public List<Pictures> getMedias(String selection,String[] selectionArgs) {
		// TODO Auto-generated method stub
		List<Pictures> picures=new ArrayList<Pictures>();
		Uri uri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		String[] projection={"_data","title","width","height"};
		Cursor csdb = cr.query(uri, projection, null, null, null);
		if(csdb.moveToFirst()){
			do{
				if(csdb.getInt(2)>500&&csdb.getInt(3)>500){
					Pictures picture = new Pictures();
					picture.setPath(csdb.getString(0));
					picture.setTitle(csdb.getString(1));
					picture.setWidth(csdb.getInt(2));
					picture.setHeight(csdb.getInt(3));
					picures.add(picture);
				}
			}while(csdb.moveToNext());
		}
		return picures;}
}
