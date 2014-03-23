package com.seventh.intelligentguide.util;

import com.seventh.intelligentguide.beans.GPSparameter;
import com.seventh.intelligentguide.beans.LatLon;
/**
 * ��γ�������ת��������
 * ��ȡ����GPS����
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
	 * �������㾭γ�ȼ������
	 * 
	 * @param src
	 *            A��γ��
	 * @param dest
	 *            B�㾭��
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
	 * �������㾭γ�ȼ������
	 * 
	 * @param lat1
	 *            A��ά��
	 * @param lon1
	 *            A�㾭��
	 * @param lat2
	 *            B��ά��
	 * @param lon2
	 *            B�㾭��
	 * @return
	 */
	public static double GetDistance(double lat1, double lon1, double lat2,
			double lon2) {
		LatLon src = new LatLon(lat1, lon1);
		LatLon dest = new LatLon(lat2, lon2);
		return GetDistance(src, dest);
	}

	/**
	 * ��֪��A��γ�ȣ�����B���A��ľ��룬�ͷ�λ����B��ľ�γ��
	 * 
	 * @param a A�㾭γ��
	 * @param distance B�㵽A�����
	 * @param angle B�����A�㷽λ��12����Ϊ0�ȣ�˳ʱ������
	 * @return B�㾭γ��
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
	 * ��֪��A��γ�ȣ�����B���A��ľ��룬�ͷ�λ����B��ľ�γ��
	 * @param longitude A�㾭��
	 * @param latitude A��ά��
	 * @param distance B�㵽A�����
	 * @param angle B�����A�㷽λ��12����Ϊ0�ȣ�˳ʱ������
	 * @return B�㾭γ��
	 */
	public static LatLon GetLatLon(double longitude, double latitude,
			double distance, double angle) {
		LatLon a = new LatLon(latitude, longitude);
		return GetLatLon(a, distance, angle);
	}
	/**
     * ��γ�Ȳ���
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
