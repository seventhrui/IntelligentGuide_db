package com.seventh.tabhost;

import com.seventh.R;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

/**
 * 用tabHost实现下选项卡
 */
public class MyTabHostFive extends TabActivity {
	
	private TabHost mTabHost;
	private TabWidget mTabWidget;
	public static String strText;//用于返回音频名
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost);
		setTitle(R.string.app_name);
		
		mTabHost = this.getTabHost();
		/* 去除标签下方的白线 */
		mTabHost.setPadding(mTabHost.getPaddingLeft(),
				mTabHost.getPaddingTop(), mTabHost.getPaddingRight(),
				mTabHost.getPaddingBottom() - 5);
		Resources rs = getResources();

		// 选项一  景点列表
		Intent layout1intent = new Intent();
		layout1intent.setClass(this, Layout1.class);
		TabHost.TabSpec layout1spec = mTabHost.newTabSpec("layout1");
		layout1spec.setIndicator("", rs.getDrawable(android.R.drawable.ic_menu_agenda));
		layout1spec.setContent(layout1intent);
		mTabHost.addTab(layout1spec);

		// 选项二  手动输入
		Intent layout2intent = new Intent();
		layout2intent.setClass(this, Layout2.class);
		TabHost.TabSpec layout2spec = mTabHost.newTabSpec("layout2");
		layout2spec.setIndicator("", rs.getDrawable(android.R.drawable.ic_menu_search));
		layout2spec.setContent(layout2intent);
		mTabHost.addTab(layout2spec);

		// 选项三  地图
		Intent layout3intent = new Intent();
		layout3intent.setClass(this, Layout3.class);
		TabHost.TabSpec layout3spec = mTabHost.newTabSpec("layout3");
		layout3spec.setIndicator("", rs.getDrawable(android.R.drawable.ic_menu_mapmode));
		layout3spec.setContent(layout3intent);
		mTabHost.addTab(layout3spec);

		// 选项四  语音识别
		Intent layout4intent = new Intent();
		layout4intent.setClass(this, Layout4.class);
		TabHost.TabSpec layout4spec = mTabHost.newTabSpec("layout4");
		layout4spec.setIndicator("", rs.getDrawable(android.R.drawable.ic_btn_speak_now));
		layout4spec.setContent(layout4intent);
		mTabHost.addTab(layout4spec);
		
		// 选项五  GPS
		Intent layout5intent = new Intent();
		layout5intent.setClass(this, Layout5.class);
		TabHost.TabSpec layout5spec = mTabHost.newTabSpec("layout5");
		layout5spec.setIndicator("", rs.getDrawable(android.R.drawable.ic_menu_compass));
		layout5spec.setContent(layout5intent);
		mTabHost.addTab(layout5spec);

		/* 对Tab标签的定制 */
		mTabWidget = mTabHost.getTabWidget();
		for (int i = 0; i < mTabWidget.getChildCount(); i++) {
			/* 得到每个标签的视图 */
			View view = mTabWidget.getChildAt(i);
			/* 设置每个标签的背景 */
			if (mTabHost.getCurrentTab() == i) {
				view.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.number_bg_pressed));
			} else {
				view.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.number_bg));
			}
			/* 设置tab的高度 */
			mTabWidget.getChildAt(i).getLayoutParams().height = 50;
			TextView tv = (TextView) mTabWidget.getChildAt(i).findViewById(
					android.R.id.title);
			/* 设置tab内字体的颜色 */
			tv.setTextColor(Color.rgb(49, 116, 171));
		}
		/* 当点击Tab选项卡的时候，更改当前Tab标签的背景 */
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				for (int i = 0; i < mTabWidget.getChildCount(); i++) {
					View view = mTabWidget.getChildAt(i);
					if (mTabHost.getCurrentTab() == i) {
						view.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.number_bg_pressed));
						if(mTabHost.getCurrentTab() != 4){
							Layout5.nowVoiceName="";
						}
					}
					else {
						view.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.number_bg));
					}
				}
			}
		});
	}
	
}