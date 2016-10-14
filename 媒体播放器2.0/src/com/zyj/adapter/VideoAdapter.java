package com.zyj.adapter;

import java.util.List;

import com.zyj.example.Video;
import com.zyj.main.VideoPlayActivity;
import com.zyj.utils.BitmapUtils;
import com.zyj.utils.Control;
import com.zyj.utils.DateTimeUtils;
import com.zyj.zyj.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoAdapter  extends MyAdapter<Video> {

	public VideoAdapter(Context context, List<Video> data) {
		super(context, data);
		// TODO Auto-generated constructor stub
	}
	
	private Holder holders;
	@Override
	public View getView(int position, View layout, ViewGroup parent) {
		// TODO Auto-generated method stub
		Video video = getData().get(position);
		Holder holder=null;
		if(layout==null){
			layout=getInflater().inflate(R.layout.video_item, null);
			holder=new Holder(layout);
			layout.setTag(holder);
		}else{
			holder=(Holder) layout.getTag();
		}
		load(holder,video);
		return layout;
	}
	
	private void load(final Holder holder, final Video video) {
		// TODO Auto-generated method stub
		holder.tvTitle.setText(video.getTitle());
		holder.imgBg.setImageBitmap(null);
		holder.imPlay.setVisibility(View.VISIBLE);
		holder.pvCache.setVisibility(View.GONE); 
		holder.imgBg.setVisibility(View.VISIBLE);
		holder.imPlay.setImageResource(R.drawable.ic_play_normal);
		try {
			BitmapUtils.loadBitmap(video.getCover(), holder);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		holder.imPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!holder.isplay){
					if(holders!=null){
						holders.imPlay.setVisibility(View.VISIBLE);
						holders.imPlay.setImageResource(R.drawable.ic_play_normal);
						holders.pvCache.setVisibility(View.GONE); 
						holders.imgBg.setVisibility(View.VISIBLE);
						holders.vvVideo.stopPlayback();
						holders.isplay=false;
						holders.isload=false;
					}
					holder.imPlay.setImageResource(R.drawable.ic_pause_normal);
					holder.imPlay.setVisibility(View.GONE); 
					holder.pvCache.setVisibility(View.VISIBLE); 
					String url=video.getMp4Hd_url();
					if(url==null||url.equals("")||url.equals("null")){
						url=video.getMp4_url();
					}
					holder.isplay=true;
					Uri uri=Uri.parse(url);
					// TODO Auto-generated method stub
					holder.vvVideo.setVideoURI(uri);
					holder.vvVideo.requestFocus(); 
					holder.vvVideo.start();
					holders=holder;
					Intent intent=new Intent();
					intent.setAction(Control.PAUSE);
					getContext().sendBroadcast(intent);
				}else{
					if(holder.vvVideo.isPlaying()){
						holder.vvVideo.pause();
						holder.imPlay.setImageResource(R.drawable.ic_play_normal);
					}else{
						holder.vvVideo.start();
						holder.imPlay.setImageResource(R.drawable.ic_pause_normal);

					}

				}
			}
		});
		holder.btRlVisi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(holder.isload){
					if(holder.rlBar.getVisibility()==View.GONE){
						holder.rlBar.setVisibility(View.VISIBLE);
						holder.imPlay.setVisibility(View.VISIBLE);

					}else{
						holder.rlBar.setVisibility(View.GONE);
						holder.imPlay.setVisibility(View.GONE);
					}
				}
			}
		});
		holder.vvVideo.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				holder.pvCache.setVisibility(View.GONE); 
				holder.imgBg.setVisibility(View.GONE);
				holder.isload=true;
				holder.tvSumTime.setText(DateTimeUtils.getDateFormat(holder.vvVideo.getDuration()));
				holder.skVideo.setMax(holder.vvVideo.getDuration());
				// TODO Auto-generated method stub
				mp.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
					int currentPosition, duration;
					@Override
					public void onBufferingUpdate(MediaPlayer mp, int percent) {
						// TODO Auto-generated method stub
						// 获得当前播放时间和当前视频的长度
						currentPosition = holder.vvVideo.getCurrentPosition();
						duration = holder.vvVideo.getDuration(); 
						// 设置进度条的主要进度，表示当前的播放时间
						holder.tvNowTime.setText(DateTimeUtils.getDateFormat(currentPosition));
						holder.skVideo.setProgress(currentPosition);
						// 设置进度条的次要进度，表示视频的缓冲进度
						percent=percent*duration/100;
						holder.skVideo.setSecondaryProgress(percent);
					}
				});
			}
		});
		holder.skVideo.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				holder.vvVideo.seekTo(seekBar.getProgress());
				holder.vvVideo.start();
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				holder.tvNowTime.setText(DateTimeUtils.getDateFormat(progress));
			}
		});
		holder.fangda.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new  Intent(getContext(),VideoPlayActivity.class);
				String url=video.getMp4Hd_url();
				if(url==null||url.equals("")||url.equals("null")){
					url=video.getMp4_url();
				}
				intent.putExtra("url", url);
				intent.putExtra("time",holder.vvVideo.getCurrentPosition());
				holder.vvVideo.pause();
				holder.imPlay.setImageResource(R.drawable.ic_play_normal);
				holder.imgBg.setVisibility(View.VISIBLE);
				getContext().startActivity(intent);
			}
		});
	}
	public class Holder{
		public Button btRlVisi;
		public TextView tvTitle;
		public VideoView vvVideo;
		public ImageView imPlay;
		public TextView tvNowTime;
		public TextView tvSumTime;
		public SeekBar skVideo;
		public ProgressBar pvCache;
		public RelativeLayout rlBar;
		public ImageView imgBg;
		public ImageView fangda;
		boolean isload;
		boolean isplay;
		public Holder(View layout) {
			tvTitle=(TextView) layout.findViewById(R.id.video_news_tv_title);
			tvNowTime=(TextView) layout.findViewById(R.id.video_nowtime);
			tvSumTime=(TextView) layout.findViewById(R.id.video_sumtime);
			imPlay=(ImageView) layout.findViewById(R.id.video_play);
			vvVideo=(VideoView) layout.findViewById(R.id.video_vv);
			pvCache=(ProgressBar)layout.findViewById(R.id.video_cache);
			rlBar=(RelativeLayout) layout.findViewById(R.id.video_rl_bar);
			skVideo=(SeekBar) layout.findViewById(R.id.video_seekbar);
			imgBg=(ImageView) layout.findViewById(R.id.video_bg);
			btRlVisi=(Button) layout.findViewById(R.id.video_rl_visi);
			fangda=(ImageView)layout.findViewById(R.id.video_fangda);
		}
	}
}
