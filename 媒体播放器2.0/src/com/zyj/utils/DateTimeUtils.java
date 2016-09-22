package com.zyj.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {
	private static SimpleDateFormat sdf=new SimpleDateFormat("mm:ss");
	private static Date date=new Date();
	 public static String getDateFormat(long mills){
		date.setTime(mills);
		return sdf.format(date);
	}
}
