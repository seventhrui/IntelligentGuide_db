package com.seventh.vo;

public class Meohe {
	private int x;//x坐标
	private int y;//y坐标
	private double longitude;//经度
	private double latitude;//纬度
	private String n;//景点名(数字)
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
	public String getN() {
		return n;
	}
	public void setN(String n) {
		this.n = n;
	}
	
	public Meohe(String x, String y, String longitude, String latitude, String n){
		this.x=Integer.parseInt(x);
		this.y=Integer.parseInt(y);
		this.longitude=Double.parseDouble(longitude);
		this.latitude=Double.parseDouble(latitude);
		this.n=n;
	}
}
