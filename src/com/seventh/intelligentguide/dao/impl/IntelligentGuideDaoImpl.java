package com.seventh.intelligentguide.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.seventh.intelligentguide.dao.IntelligentGuideDao;
import com.seventh.intelligentguide.db.GuideSQLiteOpenHelper;

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
		return placelist;
	}

	@Override
	public List<String> searchScenicList(String City_name) {
		List<String> sceniclist = new ArrayList<String>();
		cursor = db.rawQuery("SELECT Scenic_name FROM Scenic,City WHERE City_name=? AND Scenic.City_id=City.City_id", new String[]{City_name});
		while (cursor.moveToNext()) {
			String scenic=cursor.getString(0);
			sceniclist.add(scenic);
		}
		cursor.close();
		db.close();
		return sceniclist;
	}
	
	@Override
	public List<String> searchSpotsList(String Scenic_name) {
		List<String> spotslist = new ArrayList<String>(); 
		cursor = db.rawQuery("SELECT Spots_name FROM Spots,Scenic WHERE Scenic_name=? AND Spots.Scenic_id=Scenic.Scenic_id ORDER BY Spots_id", new String[]{Scenic_name});
		while (cursor.moveToNext()) {
			String spots = cursor.getString(0);
			spotslist.add(spots);
		}
		cursor.close();
		db.close();
		return spotslist;
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
	public String getSpotsByLoandLa(String Scenic_name, double lo, double la) {
		String spotsname = null;
		String s_lo = lo + "";
		String s_la = la + "";
		cursor = db.rawQuery("SELECT Spots_name FROM Spots,Scenic WHERE Scenic_name=? AND Spots.Scenic_id=Scenic.Scenic_id AND Spots.Spots_lo>?-0.0002 AND Spots.Spots_lo<?+0.0002 AND Spots.Spots_la>?-0.0002 AND Spots.Spots_la<?+0.0002",new String[]{Scenic_name,s_lo,s_lo,s_la,s_la});
		while(cursor.moveToNext()){
			spotsname=cursor.getString(0);
		}
		cursor.close();
		db.close();
		return spotsname;
	}

	@Override
	public String getSpotsByXandY(String Scenic_name, double x, double y) {
		String spotsname = null;
		String s_x = x + "";
		String s_y = y + "";
		cursor = db.rawQuery("SELECT Spots_name FROM Spots,Scenic WHERE Scenic_name=? AND Spots.Scenic_id=Scenic.Scenic_id AND Spots.Spots_x>?-15 AND Spots.Spots_x<?+15 AND Spots.Spots_y>?-15 AND Spots.Spots_y<?+15", new String[]{Scenic_name,s_x,s_x,s_y,s_y});
		while(cursor.moveToNext()){
			spotsname=cursor.getString(0);
		}
		cursor.close();
		db.close();
		return spotsname;
	}
}
