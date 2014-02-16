package com.seventh.intelligentguide.beans;
/**
 * 景点类
 * @author rui
 * 在图片上的x坐标
 * 在图片上的y坐标
 * 经度
 * 纬度
 * 景点名
 */
public class ScenicSpotBean {
	private String n;//景点编号
	private String spots_name;//景点名
	private int x;//x坐标
	private int y;//y坐标
	private double longitude;//经度
	private double latitude;//纬度
	private int scenic_id;//所属景区编号
	private String file_name;//音频文件名
	
	public String getN() {
		return n;
	}
	public void setN(String n) {
		this.n = n;
	}
	public String getSpots_name() {
		return spots_name;
	}
	public void setSpots_name(String spots_name) {
		this.spots_name = spots_name;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public int getScenic_id() {
		return scenic_id;
	}
	public void setScenic_id(int scenic_id) {
		this.scenic_id = scenic_id;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	
	public ScenicSpotBean(String n,String spots_name,String x, String y, String longitude, String latitude,String scenic_id,String file_name){
		this.n=n;
		this.spots_name=spots_name;
		this.x=Integer.parseInt(x);
		this.y=Integer.parseInt(y);
		this.longitude=Double.parseDouble(longitude);
		this.latitude=Double.parseDouble(latitude);
		this.scenic_id=Integer.parseInt(scenic_id);
		this.file_name=file_name;
	}
}
