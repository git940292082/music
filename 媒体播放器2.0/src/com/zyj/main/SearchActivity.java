package com.zyj.main;

import java.util.List;

import com.zyj.adapter.MusicAdapter;
import com.zyj.app.App;
import com.zyj.example.Music;
import com.zyj.model.IOnMusicLoaded;
import com.zyj.model.MusicModel;
import com.zyj.utils.Control;
import com.zyj.zyj.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
public class SearchActivity extends Activity {

	private ListView lvSearch;
	private EditText edSearch;
	private Button btSearch;
	private MusicAdapter sAdapter;
	public static final int Type=15641564;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		loadView();
		loadListener();
		loadData();
	}

	private void loadView() {
		// TODO Auto-generated method stub
		btSearch=(Button)findViewById(R.id.search_bt_back);
		edSearch=(EditText)findViewById(R.id.search_ed_search);
		lvSearch=(ListView)findViewById(R.id.search_lv_search);
		
	}

	private void loadListener() {
		// ÎÄ±¾ËÑË÷
		edSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				final String search=edSearch.getText().toString().trim();
				if("".equals(search)){
					return;
				}
				MusicModel model=new MusicModel();
				model.loadNewMusic(search, new IOnMusicLoaded() {
					private List<Music> musics;

					@Override
					public void OnMusicLoaded(List<Music> music) {
						// TODO Auto-generated method stub
						Log.i("123", "ËÑË÷"+search);
						if(music!=null){
							sAdapter=new MusicAdapter(getApplicationContext(),music);
							lvSearch.setAdapter(sAdapter);
							musics=music;
							App.mapMusics.put(Type,musics);
							Log.i("123", "ËÑË÷"+musics.size());
						}
					}
				});
			}
		});
		btSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		lvSearch.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				// TODO Auto-generated method stub
				Intent i=new Intent();
				i.setAction(Control.PLAY_POSITION);
				i.putExtra("play_position", arg2);
				i.putExtra("lien_name",Type);
				sendBroadcast(i);
				finish();
			}
		});
	}
	private void loadData() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

}
