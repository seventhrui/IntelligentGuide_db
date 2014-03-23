package com.seventh.intelligentguide.beans;
/**
 * ������
 * @author rui
 * ��ͼƬ�ϵ�x����
 * ��ͼƬ�ϵ�y����
 * ����
 * γ��
 * ������
 */
public class ScenicSpotBean {
	private String n;//������
	private String spots_name;//������
	private int x;//x����
	private int y;//y����
	private double longitude;//����
	private double latitude;//γ��
	private int scenic_id;//�����������
	private String file_name;//��Ƶ�ļ���
	
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
