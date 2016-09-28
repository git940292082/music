package com.zyj.example;

public class Video {
	private String topicImg;    //头部图片
	private String videosource; //视频来源
	private String mp4Hd_url;	//高清视频
	private String topicDesc; 	
	private String cover; 		//图片
	private String title;		//标题
	private String mp4_url;    //普通视频来源
	private String ptime;      //时间
	private String topicName;	//头部图片名字
	public Video() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Video(String topicImg, String videosource, String mp4Hd_url, String topicDesc, String cover, String title,
			String mp4_url, String ptime, String topicName) {
		super();
		this.topicImg = topicImg;
		this.videosource = videosource;
		this.mp4Hd_url = mp4Hd_url;
		this.topicDesc = topicDesc;
		this.cover = cover;
		this.title = title;
		this.mp4_url = mp4_url;
		this.ptime = ptime;
		this.topicName = topicName;
	}
	public String getTopicImg() {
		return topicImg;
	}
	public void setTopicImg(String topicImg) {
		this.topicImg = topicImg;
	}
	public String getVideosource() {
		return videosource;
	}
	public void setVideosource(String videosource) {
		this.videosource = videosource;
	}
	public String getMp4Hd_url() {
		return mp4Hd_url;
	}
	public void setMp4Hd_url(String mp4Hd_url) {
		this.mp4Hd_url = mp4Hd_url;
	}
	public String getTopicDesc() {
		return topicDesc;
	}
	public void setTopicDesc(String topicDesc) {
		this.topicDesc = topicDesc;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMp4_url() {
		return mp4_url;
	}
	public void setMp4_url(String mp4_url) {
		this.mp4_url = mp4_url;
	}
	public String getPtime() {
		return ptime;
	}
	public void setPtime(String ptime) {
		this.ptime = ptime;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	@Override
	public String toString() {
		return "Video [topicImg=" + topicImg + ", videosource=" + videosource + ", mp4Hd_url=" + mp4Hd_url
				+ ", topicDesc=" + topicDesc + ", cover=" + cover + ", title=" + title + ", mp4_url=" + mp4_url
				+ ", ptime=" + ptime + ", topicName=" + topicName + "]";
	}
	
}
