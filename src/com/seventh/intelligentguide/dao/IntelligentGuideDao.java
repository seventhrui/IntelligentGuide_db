package com.seventh.intelligentguide.dao;

import java.util.List;

import com.seventh.intelligentguide.vo.ScenicSpot;

public interface IntelligentGuideDao {
	
	/**
	 * ��ѯ���������У��б�
	 * @return
	 */
	public List<String> searchCityList();
	/**
	 * ��ѯ�����б�
	 * @param City_name ������
	 * @return
	 */
	public List<String> searchScenicList(String City_name);
	/**
	 * ��ѯ�������б�
	 * @param Scenic_name ������
	 * @return
	 */
	public List<String> searchSpotsNameList(String Scenic_name);
	/**
	 * ��ѯ�������б�
	 * @param Scenic_name
	 * @return
	 */
	public List<ScenicSpot> searchSpotsList(String Scenic_name);
	/**
	 * ��ѯ������ͨ��������
	 * @param Scenic_name
	 * @return
	 */
	public ScenicSpot searchSpotsBySpots_name(String Spots_name);
	/**
	 * ͨ����Ż�ȡ������
	 * @param Spots_name ������
	 * @param num ������
	 * @return ������
	 */
	public String getSpotsByNumber(String Scenic_name,String num);
	/**
	 * ͨ����γ�Ȼ�ȡ������
	 * @param Spots_name ������
	 * @param lo ���侭��
	 * @param la ����ά��
	 * @return ������
	 */
	public String getSpotsNameByLoandLa(String Scenic_name,double lo,double la);
	/**
	 * ͨ����γ�Ȼ�ȡ������
	 * @param Scenic_name
	 * @param lo
	 * @param la
	 * @return
	 */
	public ScenicSpot getSpotsByLoandLa(String Scenic_name,double lo,double la);
	/**
	 * ͨ��x��y�����ȡ������
	 * @param Spots_name ������
	 * @param x ����X����
	 * @param y ����Y����
	 * @return ������
	 */
	public String getSpotsNameByXandY(String Scenic_name,float x,float y);
	/**
	 * ͨ��x,y�����ȡ������
	 * @param Scenic_name
	 * @param x
	 * @param y
	 * @return
	 */
	public ScenicSpot getSpotsByXandY(String Scenic_name,float x,float y);
}
