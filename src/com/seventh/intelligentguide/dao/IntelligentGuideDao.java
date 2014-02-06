package com.seventh.intelligentguide.dao;

import java.util.List;

import com.seventh.intelligentguide.vo.ScenicSpot;

public interface IntelligentGuideDao {
	/**
	 * ��ѯ������
	 * @param num
	 * @return
	 */
	public String findScenicNumber(String num);
	/**
	 * ��ѯ�����б�
	 * @return
	 */
	public List<String> searchPlaceList();
	/**
	 * ��ѯ�����б�
	 * @return
	 */
	public List<String> searchScenicList();
	/**
	 * ͨ����Ż�ȡ����
	 * @param num
	 * @return
	 */
	public ScenicSpot getScenicSpotByNumber(String num);
	/**
	 * ͨ����γ�Ȼ�ȡ����
	 * @param lo
	 * @param la
	 * @return
	 */
	public ScenicSpot getScenicSpotByLoandLa(double lo,double la);
	/**
	 * ͨ��x��y�����ȡ����
	 * @param x
	 * @param y
	 * @return
	 */
	public ScenicSpot getScenicSpotByXandY(double x,double y);
}
