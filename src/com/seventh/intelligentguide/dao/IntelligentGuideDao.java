package com.seventh.intelligentguide.dao;

import java.util.List;

import com.seventh.intelligentguide.beans.GPSparameter;
import com.seventh.intelligentguide.beans.ScenicSpotBean;

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
	 * 查询景点名列表
	 * @param Scenic_name 景区名
	 * @return
	 */
	public List<String> searchSpotsNameList(String Scenic_name);
	/**
	 * 查询景点类列表
	 * @param Scenic_name
	 * @return
	 */
	public List<ScenicSpotBean> searchSpotsList(String Scenic_name);
	/**
	 * 查询景点类通过景点名
	 * @param Scenic_name
	 * @return
	 */
	public ScenicSpotBean searchSpotsBySpots_name(String Spots_name);
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
	public String getSpotsNameByLoandLa(String Scenic_name,double lo,double la);
	/**
	 * 通过经纬度获取景点类
	 * @param Scenic_name
	 * @param lo
	 * @param la
	 * @return
	 */
	public ScenicSpotBean getSpotsByLoandLa(String Scenic_name,double lo,double la,GPSparameter gpsp);
	/**
	 * 通过x，y坐标获取景点名
	 * @param Spots_name 景区名
	 * @param x 景点X坐标
	 * @param y 景点Y坐标
	 * @return 景点名
	 */
	public String getSpotsNameByXandY(String Scenic_name,float x,float y);
	/**
	 * 通过x,y坐标获取景点类
	 * @param Scenic_name
	 * @param x
	 * @param y
	 * @return
	 */
	public ScenicSpotBean getSpotsByXandY(String Scenic_name,float x,float y);
}
