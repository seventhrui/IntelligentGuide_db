package com.seventh.intelligentguide.util;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
/**
 * ����򿪵�Activity�����α����Ƴ�
 * ����ģʽ
 * @author rui
 *
 */
public class ApplictionManage extends Application {
	private List<Activity> activityList=new LinkedList<Activity>();
	private static ApplictionManage applictionmanage;
	private ApplictionManage(){
	}
	/**
	 * ����ģʽ����ApplictionManageʵ��
	 * @return
	 */
	public static ApplictionManage getApplictionManage(){
		if (null==applictionmanage) {
			applictionmanage = new ApplictionManage();
		}
		return applictionmanage;
	}
	/**
	 * ��activity���뵽List��
	 * @param activity
	 */
	public void addActivity(Activity activity){
		activityList.add(activity);
	}
	/**
	 * ����List�����Ƴ�Activity
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
