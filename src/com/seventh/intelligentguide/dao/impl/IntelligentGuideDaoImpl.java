package com.seventh.intelligentguide.dao.impl;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.seventh.intelligentguide.dao.IntelligentGuideDao;
import com.seventh.intelligentguide.db.GuideSQLiteOpenHelper;
import com.seventh.intelligentguide.vo.ScenicSpot;

public class IntelligentGuideDaoImpl implements IntelligentGuideDao {
	private GuideSQLiteOpenHelper dbhelper;
	private SQLiteDatabase db;
	public IntelligentGuideDaoImpl(Context context){
		dbhelper=new GuideSQLiteOpenHelper(context);
	}
	@Override
	public String findScenicNumber(String num) {
		db=dbhelper.getReadableDatabase();
		Log.v(null, db.toString());
		Cursor cursor=db.rawQuery("SELECT * FROM '泰山' WHERE Scenic_id=3", null);
		boolean rs=cursor.moveToNext();
		String s2=cursor.getString(1);
		cursor.close();
		db.close();
		Log.v(null, "查找到的："+s2);
		return s2;
	}

	@Override
	public List<String> searchPlaceList() {
		
		return null;
	}

	@Override
	public List<String> searchScenicList() {
		
		return null;
	}

	@Override
	public ScenicSpot getScenicSpotByNumber(String num) {
		
		return null;
	}

	@Override
	public ScenicSpot getScenicSpotByLoandLa(double lo, double la) {
		
		return null;
	}

	@Override
	public ScenicSpot getScenicSpotByXandY(double x, double y) {
		
		return null;
	}

}
