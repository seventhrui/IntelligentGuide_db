package com.seventh.intelligentguide.util;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
/**
 * 管理打开的Activity，依次遍历推出
 * 单例模式
 * @author rui
 *
 */
public class ApplictionManage extends Application {
	private List<Activity> activityList=new LinkedList<Activity>();
	private static ApplictionManage applictionmanage;
	private ApplictionManage(){
	}
	/**
	 * 单例模式返回ApplictionManage实例
	 * @return
	 */
	public static ApplictionManage getApplictionManage(){
		if (null==applictionmanage) {
			applictionmanage = new ApplictionManage();
		}
		return applictionmanage;
	}
	/**
	 * 将activity加入到List中
	 * @param activity
	 */
	public void addActivity(Activity activity){
		activityList.add(activity);
	}
	/**
	 * 遍历List依次推出Activity
	 */
	public void exitApp(){
		if (activityList!=null) {
			for (Activity activity : activityList) {
				activity.finish();
			}
		}
		System.exit(0);
	}
}
