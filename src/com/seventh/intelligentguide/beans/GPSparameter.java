package com.seventh.intelligentguide.beans;
/**
 * GPS参数
 * lan 纬度南
 * lab 纬度北
 * lod 经度东
 * lox 经度西
 * @author rui
 *
 */
public class GPSparameter {
	private double lan;
	private double lab;
	private double lod;
	private double lox;
	public GPSparameter(){
		
	}
	public GPSparameter(double lan,double lab,double lod,double lox){
		this.lan=lan;
		this.lab=lab;
		this.lod=lod;
		this.lox=lox;
	}
	
	public double getLan() {
		return lan;
	}
	public void setLan(double lan) {
		this.lan = lan;
	}
	public double getLab() {
		return lab;
	}
	public void setLab(double lab) {
		this.lab = lab;
	}
	public double getLod() {
		return lod;
	}
	public void setLod(double lod) {
		this.lod = lod;
	}
	public double getLox() {
		return lox;
	}
	public void setLox(double lox) {
		this.lox = lox;
	}
	@Override
	public String toString() {
		return "纬度北:"+lab+",纬度南:"+lan+",经度东:"+lod+",经度西:"+lox;
	}
	
}
