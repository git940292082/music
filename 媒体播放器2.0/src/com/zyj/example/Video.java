package com.zyj.example;

public class Video {
	private String path;
	private String name;
	private long Durrction;
	private int width;
	private int height;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getDurrction() {
		return Durrction;
	}
	public void setDurrction(long durrction) {
		Durrction = durrction;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public Video(String path, String name, long durrction, int width, int height) {
		super();
		this.path = path;
		this.name = name;
		Durrction = durrction;
		this.width = width;
		this.height = height;
	}
	public Video() {
		super();
	}
	
}
