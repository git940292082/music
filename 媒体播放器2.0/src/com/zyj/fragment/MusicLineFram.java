package com.zyj.fragment;
import java.util.ArrayList;
import java.util.List;

import com.zyj.main.CircleMenuLayout;
import com.zyj.main.CircleMenuLayout.OnMenuItemClickListener;
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
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
public class MusicLineFram extends Fragment {
	private View layout;
	private ImageView btnMenu;
	private CircleMenuLayout menuLayout;
	private int durrcution=0;
	private List<Fragment> fragments=new ArrayList<Fragment>();
	private String[] mItemTexts = new String[] { "�¸�� ", "�ȸ��", "���İ�",
			"KTV" };
	private int[] mItemImgs = new int[] { R.drawable.ic_music_0,
			R.drawable.ic_music_1, R.drawable.ic_music_2,
			R.drawable.ic_music_3};

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  

		if(layout==null){
			layout= inflater.inflate(R.layout.music_line_fram,container, false);
			loadView();
			loadListener();
			loadData();
		}else{
			((ViewPager) layout.getParent()).removeView(layout);
		}
		return layout;  
	} 
	private void loadView() {
		// ����view
		btnMenu=(ImageView)layout.findViewById(R.id.line_menu);
		menuLayout=(CircleMenuLayout) layout.findViewById(R.id.circle_menu);
	}
	private void loadListener() {
		// ���ؼ���
		Listener listener=new Listener();
		btnMenu.setOnClickListener(listener);
		menuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
		menuLayout.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public void itemClick(View view, int pos) {
				// TODO Auto-generated method stub
				if(durrcution!=pos){
					show(pos);
					durrcution=pos;
				}
			}
			@Override
			public void itemCenterClick(View view) {
				// TODO Auto-generated method stub
				menuLayout.setVisibility(View.GONE);
				
				btnMenu.setVisibility(View.VISIBLE);
			}
		});
	}
	private void loadData() {
		fragments.add(new MusicLineFramList1("�¸��",1,0));
		fragments.add(new MusicLineFramList1("�ȸ��",2,0));
		fragments.add(new MusicLineFramList1("���İ�",18,0));
		fragments.add(new MusicLineFramList1("KTV��",16,0));
		show(0);
		// ��������
		Log.d("123","123321321333333333333333333333");
	}
	public void show(int pos){
		Fragment fms=null;
		switch (pos) {
		case 0:
			fms=new MusicLineFramList1("�¸��",1,0);
			break;
		case 1:
			fms=new MusicLineFramList1("�ȸ��",2,0);
			break;
		case 2:
			fms=new MusicLineFramList1("���İ�",18,0);
			break;
		case 3:
			fms=new MusicLineFramList1("KTV��",16,0);
			break;
		}
		FragmentManager fm = getFragmentManager();  
		// ����Fragment����  
		FragmentTransaction tr = fm.beginTransaction();
		
		tr.replace(R.id.music_line_fram, fms);
		tr.commit();
	}
	public class Listener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			switch (v.getId()) {
			case R.id.line_menu:
				if(menuLayout.getVisibility()==View.GONE){
					menuLayout.setVisibility(View.VISIBLE);
					btnMenu.setVisibility(View.GONE);
				}else{
					menuLayout.setVisibility(View.GONE);
					btnMenu.setVisibility(View.VISIBLE);
				}
				
			}
		}
	}
}
