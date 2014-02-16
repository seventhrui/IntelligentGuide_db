package com.seventh.intelligentguide.beans;
/**
 * 经纬度
 * lat 纬度
 * lon 经度
 * @author rui
 *
 */
public class LatLon {

	// 赤道半径 earth radius
	public static double EARTH_RADIUS = 6378137;
	// 极半径 polar radius
	public static double POLAR_RADIUS = 6356725;
	private double lat;// 纬度
	private double lon;// 经度

	public LatLon() {

	}

	/**
	 * 
	 * @param lat
	 *            维度
	 * @param lon
	 *            经度
	 */
	public LatLon(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	/**
	 * 纬度弧度
	 * 
	 * @return
	 */
	public double RadLat() {
		return lat * Math.PI / 180;
	}

	/**
	 * 经度弧度
	 * 
	 * @return
	 */
	public double RadLon() {
		return lon * Math.PI / 180;
	}

	public double Ec() {
		return POLAR_RADIUS + (EARTH_RADIUS - POLAR_RADIUS) * (90 - lat) / 90;
	}

	public double Ed() {
		return Ec() * Math.cos(RadLat());
	}

	@Override
	public String toString() {
		return "经度："+lon+"，维度："+lat;
	}
	
}
