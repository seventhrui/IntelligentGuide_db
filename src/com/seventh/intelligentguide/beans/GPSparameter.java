package com.seventh.intelligentguide.beans;
/**
 * GPS����
 * lan γ����
 * lab γ�ȱ�
 * lod ���ȶ�
 * lox ������
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
		return "γ�ȱ�:"+lab+",γ����:"+lan+",���ȶ�:"+lod+",������:"+lox;
	}
	
}
