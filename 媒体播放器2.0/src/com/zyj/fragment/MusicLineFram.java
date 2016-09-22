package com.zyj.fragment;
import com.zyj.zyj.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
public class MusicLineFram extends Fragment {
	private View layout;
	private Button btKTV;
	private Button btZhong;
	private Button btRe;
	private Button btXin;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
		layout= inflater.inflate(R.layout.music_line_fram,container, false);
		loadView();
		loadListener();
		loadData();
		
		return layout;  
	} 
	private void loadView() {
		// 加载view
		btXin=(Button)layout.findViewById(R.id.music_line_bt_xin);
		btRe=(Button)layout.findViewById(R.id.music_line_bt_re);
		btZhong=(Button)layout.findViewById(R.id.music_line_bt_zhong);
		btKTV=(Button)layout.findViewById(R.id.music_line_bt_ktv);
	}
	private void loadListener() {
		// 加载监听
		Listener listener=new Listener();
		btXin.setOnClickListener(listener);
		btRe.setOnClickListener(listener);
		btZhong.setOnClickListener(listener);
		btKTV.setOnClickListener(listener);
	}
	private void loadData() {
		show(new MusicLineFramList1("新歌榜",1,0));
		// 加载数据
		Log.d("123","123321321333333333333333333333");
	}
	private void show(Fragment fms){
		FragmentManager fm = getFragmentManager();  
		// 开启Fragment事务  
		FragmentTransaction tr = fm.beginTransaction();
		tr.replace(R.id.music_line_fram, fms);
		tr.commit();
	}
	private void enabled(Button bt){
		btXin.setEnabled(true);
		btRe.setEnabled(true);
		btZhong.setEnabled(true);
		btKTV.setEnabled(true);
		bt.setEnabled(false);
	}
	public class Listener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			switch (v.getId()) {
			case R.id.music_line_bt_xin:
				show(new MusicLineFramList1("新歌榜",1,0));
				enabled(btXin);
				break;
			case R.id.music_line_bt_re:
				show(new MusicLineFramList1("热歌榜",2,0));
				enabled(btRe);
				break;
			case R.id.music_line_bt_zhong:
				show(new MusicLineFramList1("中文榜",18,0));
				enabled(btZhong);
				break;
			case R.id.music_line_bt_ktv:
				show(new MusicLineFramList1("KTV榜",6,0));
				enabled(btKTV);
				break;

			}
		}
	}
	
}
