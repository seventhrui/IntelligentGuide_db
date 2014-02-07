package com.seventh.intelligentguide.dao;

import java.util.List;

public interface IntelligentGuideDao {
	
	/**
	 * 查询地区（城市）列表
	 * @return
	 */
	public List<String> searchCityList();
	/**
	 * 查询景区列表
	 * @param City_name 景点名
	 * @return
	 */
	public List<String> searchScenicList(String City_name);
	/**
	 * 查询景点列表
	 * @param Scenic_name 景区名
	 * @return
	 */
	public List<String> searchSpotsList(String Scenic_name);
	/**
	 * 通过编号获取景点名
	 * @param Spots_name 景区名
	 * @param num 景点编号
	 * @return 景点名
	 */
	public String getSpotsByNumber(String Scenic_name,String num);
	/**
	 * 通过经纬度获取景点名
	 * @param Spots_name 景区名
	 * @param lo 经典经度
	 * @param la 景点维度
	 * @return 景点名
	 */
	public String getSpotsByLoandLa(String Scenic_name,double lo,double la);
	/**
	 * 通过x，y坐标获取景点名
	 * @param Spots_name 景区名
	 * @param x 景点X坐标
	 * @param y 景点Y坐标
	 * @return 景点名
	 */
	public String getSpotsByXandY(String Scenic_name,double x,double y);
}
