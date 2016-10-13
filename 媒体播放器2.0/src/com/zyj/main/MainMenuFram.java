package com.zyj.main;

import com.zyj.fragment.FramVideo;
import com.zyj.fragment.MineMenuFram;
import com.zyj.fragment.MusicMenuFram;
import com.zyj.fragment.PictureMenuFram;
import com.zyj.zyj.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainMenuFram extends FragmentActivity implements OnPageChangeListener{
	private RadioGroup rgMenu;
	private MusicMenuFram musicframe=new MusicMenuFram();
	private PictureMenuFram pictureframe=new PictureMenuFram();
	private MineMenuFram mineframe=new MineMenuFram();
	private FramVideo videoFrame=new FramVideo();
	private Intent i;
	private ViewPager fragmentPage;
	private FragmentAdapter fragmentAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		loadView();
		loadListner();
		loadData();
		//首次加载页面
	}
	private void loadData() {
		// TODO Auto-generated method stub
		i=new Intent();
		i.setAction("music.zyj.action.musicservice2.0");
		startService(i);
		fragmentAdapter=new FragmentAdapter(getSupportFragmentManager());
		fragmentPage.offsetLeftAndRight(4);
		fragmentPage.setAdapter(fragmentAdapter);
		fragmentPage.setOnPageChangeListener(this);
		Log.i("123", "2");
		
	}

	private void loadView() {
		// TODO Auto-generated method stub
		rgMenu=(RadioGroup)findViewById(R.id.radio);
		fragmentPage=(ViewPager)findViewById(R.id.fram);
	}
	private void loadListner() {
		// TODO Auto-generated method stub
		ListenerMenu menu=new ListenerMenu();
		rgMenu.setOnCheckedChangeListener(menu);
	}
	private class ListenerMenu implements OnCheckedChangeListener{
		@Override

		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.rd_music:
				fragmentPage.setCurrentItem(0,false);
				break;
			case R.id.rd_video:
				fragmentPage.setCurrentItem(1,false);
				break;
			case R.id.rd_picture:	
				fragmentPage.setCurrentItem(2,false);
				break;
			case R.id.rd_mine:
				fragmentPage.setCurrentItem(3,false);
				break;
			}
		}
	}
	public class FragmentAdapter extends FragmentPagerAdapter{

		public FragmentAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment fragment=null;
			switch(arg0){
			case 0:
				fragment=musicframe;
				break;
			case 1:
				fragment=videoFrame;
				break;
			case 2:
				fragment=pictureframe;
				break;
			case 3:
				fragment=mineframe;
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 4;
		}

	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==event.KEYCODE_BACK){

			AlertDialog dialog;
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setTitle("提示！！！");
			builder.setMessage("亲，您真的要退出吗?");
			builder.setNeutralButton("确定", new AlertDialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					stopService(i);
					finish();
				}
			});
			builder.setPositiveButton("舍不得", null);
			dialog=builder.create();
			dialog.show();
		}
		return false;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onPageSelected(int arg0) {
		switch(arg0){
		case 0:
			rgMenu.check(R.id.rd_music);
			break;
		case 1:
			rgMenu.check(R.id.rd_video);
			break;
		case 2:
			rgMenu.check(R.id.rd_picture);
			break;
		case 3:
			rgMenu.check(R.id.rd_mine);
			break;
		}
	}
}
