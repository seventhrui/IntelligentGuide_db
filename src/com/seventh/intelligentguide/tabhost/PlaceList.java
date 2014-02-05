package com.seventh.intelligentguide.tabhost;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.seventh.intelligentguide.Index;
import com.seventh.intelligentguide.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PlaceList extends Activity {

	private List<String> szone = new ArrayList<String>();
	public static String activityTitle = "";// �������
	public static String file;// ��Ƶ�ļ�����
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		szone = getData();//��ȡ�����б�
		//��֯������ת
		Intent mainIntent = new Intent("android.intent.action.SQUARE",
				null);
		mainIntent.addCategory("android.intent.category.SQUARE");
		ListView zonglist = (ListView) this
				.findViewById(R.id.listview_sceniczone);
		zonglist.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, szone));
		zonglist.setOnItemClickListener(new ItemClickListener());
	}
	
	/**
	 * ��ȡ�����б�(dao�ļ����е��ļ����б�)
	 * @return List<String> �����б�
	 */
	private List<String> getData() {
		List<String> folderlist = new ArrayList<String>();
		try {
			String filePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/dao/"+Index.place_file+"/";
			File file = new File(filePath);
			File[] files = file.listFiles();
			Arrays.sort(files);

			for (File mCurrentFile : files) {
				if (mCurrentFile.isDirectory()) {
					folderlist.add(mCurrentFile.getName());
					//Log.v(null, mCurrentFile.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return folderlist;
	}
	/**
	 * �б����¼�
	 * �������뾰����Ӧ������
	 * @author GaoRui
	 *
	 */
	public class ItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Layout1.assetsList.clear();
			activityTitle = ((TextView) arg1).getText().toString();
			file = activityTitle;
			Intent in = new Intent(PlaceList.this, MyTabHostFive.class);
			startActivity(in);
		}
	}
}
