package com.seventh.intelligentguide.dao.impl;

import java.util.ArrayList;
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
	private Cursor cursor = null;

	public IntelligentGuideDaoImpl(Context context) {
		dbhelper = new GuideSQLiteOpenHelper(context);
		db = dbhelper.getReadableDatabase();
	}

	@Override
	public List<String> searchCityList() {
		List<String> placelist = new  ArrayList<String>();
		cursor = db.rawQuery("SELECT City_name FROM City", null);
		while (cursor.moveToNext()) {
			String place = cursor.getString(0);
			placelist.add(place);
		}
		cursor.close();
		db.close();
		for(String s:placelist){
			Log.v("城市", s);
		}
		return placelist;
	}

	@Override
	public List<String> searchScenicList(String City_name) {
		List<String> sceniclist = new ArrayList<String>();
		cursor = db.rawQuery("SELECT Scenic_name FROM Scenic,City WHERE City_name=? AND Scenic.City_id=City.City_id ORDER BY Scenic_id", new String[]{City_name});
		while (cursor.moveToNext()) {
			String scenic=cursor.getString(0);
			sceniclist.add(scenic);
		}
		cursor.close();
		db.close();
		return sceniclist;
	}
	
	@Override
	public List<String> searchSpotsNameList(String Scenic_name) {
		List<String> spotsnamelist = new ArrayList<String>(); 
		cursor = db.rawQuery("SELECT Spots_name FROM Spots,Scenic WHERE Scenic_name=? AND Spots.Scenic_id=Scenic.Scenic_id ORDER BY Spots_id", new String[]{Scenic_name});
		while (cursor.moveToNext()) {
			String spots = cursor.getString(0);
			spotsnamelist.add(spots);
		}
		cursor.close();
		db.close();
		return spotsnamelist;
	}
	
	@Override
	public List<ScenicSpot> searchSpotsList(String Scenic_name){
		List<ScenicSpot> spotslist = new ArrayList<ScenicSpot>(); 
		cursor = db.rawQuery("SELECT Spots.Spots_id,Spots.Spots_name,Spots.Spots_X,Spots.Spots_Y,Spots.Spots_lo,Spots.Spots_la,Spots.Scenic_id,Spots.File_name FROM Spots,Scenic WHERE Scenic_name=? AND Spots.Scenic_id=Scenic.Scenic_id ORDER BY Spots_id", new String[]{Scenic_name});
		while (cursor.moveToNext()) {
			String s0 = cursor.getString(0);
			String s1 = cursor.getString(1);
			String s2 = cursor.getString(2);
			String s3 = cursor.getString(3);
			String s4 = cursor.getString(4);
			String s5 = cursor.getString(5);
			String s6 = cursor.getString(6);
			String s7 = cursor.getString(7);
			spotslist.add(new ScenicSpot(s0, s1, s2, s3, s4, s5, s6, s7));
		}
		cursor.close();
		db.close();
		return spotslist;
	}
	@Override
	public ScenicSpot searchSpotsBySpots_name(String Spots_name){
		ScenicSpot ss=null;
		cursor = db.rawQuery("SELECT * FROM Spots WHERE Spots_name=?", new String[]{Spots_name});
		while (cursor.moveToNext()) {
			String s0 = cursor.getString(0);
			String s1 = cursor.getString(1);
			String s2 = cursor.getString(2);
			String s3 = cursor.getString(3);
			String s4 = cursor.getString(4);
			String s5 = cursor.getString(5);
			String s6 = cursor.getString(6);
			String s7 = cursor.getString(7);
			ss=new ScenicSpot(s0, s1, s2, s3, s4, s5, s6, s7);
		}
		return ss;
	}
	@Override
	public String getSpotsByNumber(String Scenic_name, String num) {
		String spotsname = null;
		cursor = db.rawQuery("SELECT Spots_name FROM Spots,Scenic WHERE Scenic_name=? AND Spots.Scenic_id=Scenic.Scenic_id AND Spots.Spots_id=?", new String[]{Scenic_name,num});
		while (cursor.moveToNext()) {
			spotsname=cursor.getString(0);
		}
		cursor.close();
		db.close();
		return spotsname;
	}

	@Override
	public String getSpotsNameByLoandLa(String Scenic_name, double lo, double la) {
		String spotsname = null;
		cursor = db.rawQuery("SELECT Spots_name FROM Spots,Scenic WHERE Scenic_name=? AND Spots.Scenic_id=Scenic.Scenic_id AND Spots.Spots_lo>? AND Spots.Spots_lo<? AND Spots.Spots_la>? AND Spots.Spots_la<?",new String[]{Scenic_name,String.valueOf(lo-0.0002),String.valueOf(lo+0.0002),String.valueOf(la-0.0002),String.valueOf(la+0.0002)});
		//Log.v("查询语句", cursor.toString());
		while(cursor.moveToNext()){
			spotsname=cursor.getString(0);
		}
		cursor.close();
		db.close();
		//Log.v("查询到的", spotsname);
		return spotsname;
	}
	
	@Override
	public ScenicSpot getSpotsByLoandLa(String Scenic_name, double lo, double la) {
		ScenicSpot ss=null;
		cursor = db.rawQuery("SELECT Spots.Spots_id,Spots.Spots_name,Spots.Spots_X,Spots.Spots_Y,Spots.Spots_lo,Spots.Spots_la,Spots.Scenic_id,Spots.File_name FROM Spots,Scenic WHERE Scenic_name=? AND Spots.Scenic_id=Scenic.Scenic_id AND Spots.Spots_lo>? AND Spots.Spots_lo<? AND Spots.Spots_la>? AND Spots.Spots_la<?", new String[]{Scenic_name,String.valueOf(lo-0.0002),String.valueOf(lo+0.0002),String.valueOf(la-0.0002),String.valueOf(la+0.0002)});
		while (cursor.moveToNext()) {
			String s0 = cursor.getString(0);
			String s1 = cursor.getString(1);
			String s2 = cursor.getString(2);
			String s3 = cursor.getString(3);
			String s4 = cursor.getString(4);
			String s5 = cursor.getString(5);
			String s6 = cursor.getString(6);
			String s7 = cursor.getString(7);
			ss=new ScenicSpot(s0, s1, s2, s3, s4, s5, s6, s7);
		}
		cursor.close();
		db.close();
		return ss;
	}
	
	@Override
	public String getSpotsNameByXandY(String Scenic_name, float x, float y) {
		String spotsname = null;
		cursor = db.rawQuery("SELECT Spots_name FROM Spots,Scenic WHERE Scenic_name=? AND Spots.Scenic_id=Scenic.Scenic_id AND Spots.Spots_x>?-15 AND Spots.Spots_x<?+15 AND Spots.Spots_y>?-15 AND Spots.Spots_y<?+15", new String[]{Scenic_name,String.valueOf(x-15),String.valueOf(x+15),String.valueOf(y-15),String.valueOf(y+15)});
		Log.v("查询语句", cursor.toString());
		while(cursor.moveToNext()){
			spotsname=cursor.getString(0);
		}
		cursor.close();
		db.close();
		Log.v("查询结果", spotsname+"");
		return spotsname;
	}

	@Override
	public ScenicSpot getSpotsByXandY(String Scenic_name, float x, float y) {
		ScenicSpot ss=null;
		cursor = db.rawQuery("SELECT Spots.Spots_id,Spots.Spots_name,Spots.Spots_X,Spots.Spots_Y,Spots.Spots_lo,Spots.Spots_la,Spots.Scenic_id,Spots.File_name FROM Spots,Scenic WHERE Scenic_name=? AND Spots.Scenic_id=Scenic.Scenic_id AND Spots.Spots_x>?-15 AND Spots.Spots_x<?+15 AND Spots.Spots_y>?-15 AND Spots.Spots_y<?+15", new String[]{Scenic_name,String.valueOf(x-15),String.valueOf(x+15),String.valueOf(y-15),String.valueOf(y+15)});
		while (cursor.moveToNext()) {
			String s0 = cursor.getString(0);
			String s1 = cursor.getString(1);
			String s2 = cursor.getString(2);
			String s3 = cursor.getString(3);
			String s4 = cursor.getString(4);
			String s5 = cursor.getString(5);
			String s6 = cursor.getString(6);
			String s7 = cursor.getString(7);
			ss=new ScenicSpot(s0, s1, s2, s3, s4, s5, s6, s7);
		}
		cursor.close();
		db.close();
		return ss;
	}
}
