package com.zyj.main;
import com.zyj.zyj.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MusicPlayingMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_playing_menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.music_playing_menu, menu);
		return true;
	}

}
