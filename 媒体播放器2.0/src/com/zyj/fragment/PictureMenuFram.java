package com.zyj.fragment;

import com.zyj.adapter.PictureAdapter;
import com.zyj.app.App;
import com.zyj.dao.AudioFratory;
import com.zyj.dao.IDao;
import com.zyj.example.Pictures;
import com.zyj.main.PicShowActivity;
import com.zyj.zyj.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class PictureMenuFram extends Fragment implements OnItemClickListener {


	private View layout;
	private GridView gvPicture;
	private PictureAdapter pictureAdapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  

		if(layout==null){
			layout= inflater.inflate(R.layout.picture_menu_fram, container, false);
		}else{
			  ((ViewGroup) layout.getParent()).removeView(layout); 
		}
		IDao<Pictures> pictureDao=AudioFratory.getPictures(getActivity().getContentResolver());
		App.Pictures.addAll(pictureDao.getMedias(null, null));
		gvPicture=(GridView)layout.findViewById(R.id.picture_gv);
		pictureAdapter=new PictureAdapter(App.Pictures, getActivity());
		gvPicture.setAdapter(pictureAdapter);
		gvPicture.setOnItemClickListener(this);
		return layout;  
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent=new Intent(getActivity(),PicShowActivity.class);
		intent.putExtra("position", arg2);
		startActivity(intent);
	} 
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		pictureAdapter.close();
		super.onDestroy();
	}

}
