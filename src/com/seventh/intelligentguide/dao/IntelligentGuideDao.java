package com.seventh.intelligentguide.dao;

import java.util.List;

import com.seventh.intelligentguide.vo.ScenicSpot;

public interface IntelligentGuideDao {
	/**
	 * 查询景点编号
	 * @param num
	 * @return
	 */
	public String findScenicNumber(String num);
	/**
	 * 查询地区列表
	 * @return
	 */
	public List<String> searchPlaceList();
	/**
	 * 查询景点列表
	 * @return
	 */
	public List<String> searchScenicList();
	/**
	 * 通过编号获取景点
	 * @param num
	 * @return
	 */
	public ScenicSpot getScenicSpotByNumber(String num);
	/**
	 * 通过经纬度获取景点
	 * @param lo
	 * @param la
	 * @return
	 */
	public ScenicSpot getScenicSpotByLoandLa(double lo,double la);
	/**
	 * 通过x，y坐标获取景点
	 * @param x
	 * @param y
	 * @return
	 */
	public ScenicSpot getScenicSpotByXandY(double x,double y);
}
