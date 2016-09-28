package com.zyj.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlFactory {
	public static String getMusicUrl(int type,int offset,int size){
		String url ="http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.billboard.billList&format=xml&type="+type+"&offset="+offset+"&size="+size;
		return url;
	}
	public static String getSongIdUrl(String songID){
		String url="http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.song.getInfos&format=json&songid="+songID+"&ts=1408284347323&e=JoN56kTXnnbEpd9MVczkYJCSx%2FE1mkLx%2BPMIkTcOEu4%3D&nw=2&ucf=1&res=1";
		return url;
	}
	public static String getSerachUrl(String searchEd){
		try {
			searchEd=URLEncoder.encode(searchEd,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url="http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.search.common&format=xml&query="+searchEd+"&page_no=1&page_size=30";
		return url;
	}
	public static String getVideoUrl(int fromIndex){
		String url="http://c.m.163.com/nc/video/list/V9LG4B3A0/y/"+fromIndex+"-"+(fromIndex+20)+".html";
		return url;
	}
	
}
