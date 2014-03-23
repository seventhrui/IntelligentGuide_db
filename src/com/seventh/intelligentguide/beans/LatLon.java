package com.seventh.intelligentguide.beans;
/**
 * ��γ��
 * lat γ��
 * lon ����
 * @author rui
 *
 */
public class LatLon {

	// ����뾶 earth radius
	public static double EARTH_RADIUS = 6378137;
	// ���뾶 polar radius
	public static double POLAR_RADIUS = 6356725;
	private double lat;// γ��
	private double lon;// ����

	public LatLon() {

	}

	/**
	 * 
	 * @param lat
	 *            ά��
	 * @param lon
	 *            ����
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
	 * γ�Ȼ���
	 * 
	 * @return
	 */
	public double RadLat() {
		return lat * Math.PI / 180;
	}

	/**
	 * ���Ȼ���
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
		return "���ȣ�"+lon+"��ά�ȣ�"+lat;
	}
	
}
