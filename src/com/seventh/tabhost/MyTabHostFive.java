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
 * ��tabHostʵ����ѡ�
 */
public class MyTabHostFive extends TabActivity {
	
	private TabHost mTabHost;
	private TabWidget mTabWidget;
	public static String strText;//���ڷ�����Ƶ��
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost);
		setTitle(R.string.app_name);
		
		mTabHost = this.getTabHost();
		/* ȥ����ǩ�·��İ��� */
		mTabHost.setPadding(mTabHost.getPaddingLeft(),
				mTabHost.getPaddingTop(), mTabHost.getPaddingRight(),
				mTabHost.getPaddingBottom() - 5);
		Resources rs = getResources();

		// ѡ��һ  �����б�
		Intent layout1intent = new Intent();
		layout1intent.setClass(this, Layout1.class);
		TabHost.TabSpec layout1spec = mTabHost.newTabSpec("layout1");
		layout1spec.setIndicator("", rs.getDrawable(android.R.drawable.ic_menu_agenda));
		layout1spec.setContent(layout1intent);
		mTabHost.addTab(layout1spec);

		// ѡ���  �ֶ�����
		Intent layout2intent = new Intent();
		layout2intent.setClass(this, Layout2.class);
		TabHost.TabSpec layout2spec = mTabHost.newTabSpec("layout2");
		layout2spec.setIndicator("", rs.getDrawable(android.R.drawable.ic_menu_search));
		layout2spec.setContent(layout2intent);
		mTabHost.addTab(layout2spec);

		// ѡ����  ��ͼ
		Intent layout3intent = new Intent();
		layout3intent.setClass(this, Layout3.class);
		TabHost.TabSpec layout3spec = mTabHost.newTabSpec("layout3");
		layout3spec.setIndicator("", rs.getDrawable(android.R.drawable.ic_menu_mapmode));
		layout3spec.setContent(layout3intent);
		mTabHost.addTab(layout3spec);

		// ѡ����  ����ʶ��
		Intent layout4intent = new Intent();
		layout4intent.setClass(this, Layout4.class);
		TabHost.TabSpec layout4spec = mTabHost.newTabSpec("layout4");
		layout4spec.setIndicator("", rs.getDrawable(android.R.drawable.ic_btn_speak_now));
		layout4spec.setContent(layout4intent);
		mTabHost.addTab(layout4spec);
		
		// ѡ����  GPS
		Intent layout5intent = new Intent();
		layout5intent.setClass(this, Layout5.class);
		TabHost.TabSpec layout5spec = mTabHost.newTabSpec("layout5");
		layout5spec.setIndicator("", rs.getDrawable(android.R.drawable.ic_menu_compass));
		layout5spec.setContent(layout5intent);
		mTabHost.addTab(layout5spec);

		/* ��Tab��ǩ�Ķ��� */
		mTabWidget = mTabHost.getTabWidget();
		for (int i = 0; i < mTabWidget.getChildCount(); i++) {
			/* �õ�ÿ����ǩ����ͼ */
			View view = mTabWidget.getChildAt(i);
			/* ����ÿ����ǩ�ı��� */
			if (mTabHost.getCurrentTab() == i) {
				view.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.number_bg_pressed));
			} else {
				view.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.number_bg));
			}
			/* ����tab�ĸ߶� */
			mTabWidget.getChildAt(i).getLayoutParams().height = 50;
			TextView tv = (TextView) mTabWidget.getChildAt(i).findViewById(
					android.R.id.title);
			/* ����tab���������ɫ */
			tv.setTextColor(Color.rgb(49, 116, 171));
		}
		/* �����Tabѡ���ʱ�򣬸��ĵ�ǰTab��ǩ�ı��� */
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