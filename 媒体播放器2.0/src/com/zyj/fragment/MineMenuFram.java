package com.zyj.fragment;


import com.zyj.main.LoginDialog;
import com.zyj.utils.Control;
import com.zyj.zyj.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;
public class MineMenuFram extends Fragment {
	private View layout;
	private ImageView imgIco;
	private TextView tvName;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
		if(layout==null){
			layout= inflater.inflate(R.layout.mine_menu_fram, container, false);
			loadView();
			loadListener();
		}else{
			  ((ViewGroup) layout.getParent()).removeView(layout); 
		}
		
		return layout;  
	} 
	private void loadView() {
		imgIco=(ImageView)layout.findViewById(R.id.mine_img_ico);
		tvName=(TextView)layout.findViewById(R.id.mine_tv_name);
		
	}
	private void loadListener(){
		imgIco.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!Control.IS_LOGIN){
					Intent intent=new Intent(getActivity(),LoginDialog.class);
					startActivityForResult(intent, Control.LOGIN_RES);
				}
			}
		});
	}
	
	@Override

	public void onActivityCreated(Bundle savedInstanceState) {  
		super.onActivityCreated(savedInstanceState);  

	}  
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==Control.LOGIN_RES){
			if(resultCode==Activity.RESULT_OK){
				tvName.setText(Control.share.getString("name", ""));
				Control.IS_LOGIN=true;
			}
		}
	}
}
