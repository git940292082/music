package com.zyj.dao;

import java.util.List;

public abstract interface IDao<T> {
	 List<T> getMedias(String selection,String[] selectionArgs);
}
