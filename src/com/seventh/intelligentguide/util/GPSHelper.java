package com.seventh.intelligentguide.util;

import com.seventh.intelligentguide.beans.GPSparameter;
import com.seventh.intelligentguide.beans.LatLon;
/**
 * 经纬度与距离转换帮助类
 * 获取当地GPS参数
 * @author rui
 *
 */
public class GPSHelper {
	private static GPSHelper gpshelper;
	private GPSHelper(){
		
	}
	public static GPSHelper getGPSHelper(){
		if(null==gpshelper){
			gpshelper=new GPSHelper();
		}
		return gpshelper;
	}
	/**
	 * 根据两点经纬度计算距离
	 * 
	 * @param src
	 *            A点纬度
	 * @param dest
	 *            B点经度
	 * @return
	 */
	public static double GetDistance(LatLon src, LatLon dest) {
		double latDis = src.RadLat() - dest.RadLat();
		double lonDis = src.RadLon() - dest.RadLon();

		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(latDis / 2), 2)
				+ Math.cos(src.getLat()) * Math.cos(dest.getLat())
				* Math.pow(Math.sin(lonDis / 2), 2)));
		s = s * LatLon.EARTH_RADIUS;
		//s = Math.round(s * 10000) / 10000;

		return s;
	}

	/**
	 * 根据两点经纬度计算距离
	 * 
	 * @param lat1
	 *            A点维度
	 * @param lon1
	 *            A点经度
	 * @param lat2
	 *            B点维度
	 * @param lon2
	 *            B点经度
	 * @return
	 */
	public static double GetDistance(double lat1, double lon1, double lat2,
			double lon2) {
		LatLon src = new LatLon(lat1, lon1);
		LatLon dest = new LatLon(lat2, lon2);
		return GetDistance(src, dest);
	}

	/**
	 * 已知点A经纬度，根据B点据A点的距离，和方位，求B点的经纬度
	 * 
	 * @param a A点经纬度
	 * @param distance B点到A点距离
	 * @param angle B点相对A点方位，12点钟为0度，顺时针增加
	 * @return B点经纬度
	 */
	public static LatLon GetLatLon(LatLon a, double distance, double angle) {
		double dx = distance * 1000 * Math.sin(angle * Math.PI / 180);
		double dy = distance * 1000 * Math.cos(angle * Math.PI / 180);

		double lon = (dx / a.Ed() + a.RadLon()) * 180 / Math.PI;
		double lat = (dy / a.Ec() + a.RadLat()) * 180 / Math.PI;

		LatLon b = new LatLon(lat, lon);
		return b;
	}

	/**
	 * 已知点A经纬度，根据B点据A点的距离，和方位，求B点的经纬度
	 * @param longitude A点经度
	 * @param latitude A点维度
	 * @param distance B点到A点距离
	 * @param angle B点相对A点方位，12点钟为0度，顺时针增加
	 * @return B点经纬度
	 */
	public static LatLon GetLatLon(double longitude, double latitude,
			double distance, double angle) {
		LatLon a = new LatLon(latitude, longitude);
		return GetLatLon(a, distance, angle);
	}
	/**
     * 经纬度参数
     * @param lalo
     * @return
     */
    public GPSparameter getCanshu(LatLon lalo){
		GPSHelper gr=new GPSHelper();
		double lan=0.0;
		double lab=0.0;
		double lod=0.0;
		double lox=0.0;
		double jl=20*0.001;
		lab=(gr.GetLatLon(lalo, jl, 45).getLat()-lalo.getLat());
		lan=(gr.GetLatLon(lalo, jl, 225).getLat()-lalo.getLat());
		lod=(gr.GetLatLon(lalo, jl, 45).getLon()-lalo.getLon());
		lox=(gr.GetLatLon(lalo, jl, 225).getLon()-lalo.getLon());
		GPSparameter gpsp=new GPSparameter(lan,lab,lod,lox);
		return gpsp;
	}
}
