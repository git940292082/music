package com.zyj.main;
import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import com.zyj.utils.DateTimeUtils;
import com.zyj.zyj.R;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.VideoView;
public class VideoPlayActivity extends Activity {
	Intent intent;
	@ViewInject(R.id.video_vv)
	VideoView video;
	@ViewInject(R.id.video_fangda)
	ImageView fangda;
	@ViewInject(R.id.video_nowtime)
	TextView tvNowTime;
	@ViewInject(R.id.video_sumtime)
	TextView tvSumTime;
	@ViewInject(R.id.video_seekbar)
	SeekBar skVideo;
	@ViewInject(R.id.video_rl_visi)
	Button button;
	@ViewInject(R.id.video_play)
	ImageView btnPlay;
	@ViewInject(R.id.video_rl_bar)
	RelativeLayout rlBar;
	boolean isPlay;
	private Uri uri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_play);
		x.view().inject(this);
		intent=getIntent();
		String url = intent.getStringExtra("url");
		int time = intent.getIntExtra("time", 0);
		uri=Uri.parse(url);
		// TODO Auto-generated method stub
		video.setVideoURI(uri);
		video.requestFocus(); 
		video.seekTo(time);
		video.start();
		setListener();
	}
	private void setListener() {
		// TODO Auto-generated method stub
		video.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				isPlay=true;
				tvSumTime.setText(DateTimeUtils.getDateFormat(video.getDuration()));
				skVideo.setMax(video.getDuration());
				btnPlay.setImageResource(R.drawable.ic_pause_normal);
				// TODO Auto-generated method stub
				mp.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
					int currentPosition, duration;
					@Override
					public void onBufferingUpdate(MediaPlayer mp, int percent) {
						// TODO Auto-generated method stub
						// 获得当前播放时间和当前视频的长度
						currentPosition = video.getCurrentPosition();
						duration = video.getDuration(); 
						// 设置进度条的主要进度，表示当前的播放时间
						tvNowTime.setText(DateTimeUtils.getDateFormat(currentPosition));
						skVideo.setProgress(currentPosition);
						// 设置进度条的次要进度，表示视频的缓冲进度
						percent=percent*duration/100;
						skVideo.setSecondaryProgress(percent);
					}
				});
			}
		});
		fangda.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		skVideo.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				video.setVideoURI(uri);
				video.requestFocus(); 
				video.seekTo(seekBar.getProgress());
				video.start();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				tvNowTime.setText(DateTimeUtils.getDateFormat(progress));
			}
		});
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if( rlBar.getVisibility()==View.GONE){
					rlBar.setVisibility(View.VISIBLE);
					btnPlay.setVisibility(View.VISIBLE);

				}else{
					rlBar.setVisibility(View.GONE);
					btnPlay.setVisibility(View.GONE);
				}

			}
		});
		btnPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isPlay){
					if(video.isPlaying()){
						video.pause();
						btnPlay.setImageResource(R.drawable.ic_play_normal);
					}else{
						video.start();
						btnPlay.setImageResource(R.drawable.ic_pause_normal);
					}
				}
			}
		});
	}
}
