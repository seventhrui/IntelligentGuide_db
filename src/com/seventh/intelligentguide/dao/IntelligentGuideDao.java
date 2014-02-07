package com.seventh.intelligentguide.dao;

import java.util.List;

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
	 * ��ѯ�����б�
	 * @param Scenic_name ������
	 * @return
	 */
	public List<String> searchSpotsList(String Scenic_name);
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
	public String getSpotsByLoandLa(String Scenic_name,double lo,double la);
	/**
	 * ͨ��x��y�����ȡ������
	 * @param Spots_name ������
	 * @param x ����X����
	 * @param y ����Y����
	 * @return ������
	 */
	public String getSpotsByXandY(String Scenic_name,double x,double y);
}
