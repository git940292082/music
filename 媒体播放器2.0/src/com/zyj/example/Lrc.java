package com.zyj.example;

public class Lrc {
	private String date;
	private String context;
	private boolean isPlaying;
	
	public boolean isPlaying() {
		return isPlaying;
	}
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public Lrc(String date, String context) {
		super();
		this.date = date;
		this.context = context;
	}
	public Lrc() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return context;
	}
	
	
}
