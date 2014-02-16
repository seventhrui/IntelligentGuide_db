package com.seventh.intelligentguide.activity;

import java.util.ArrayList;
import java.util.List;

import com.seventh.intelligentguide.R;
import com.seventh.intelligentguide.dao.impl.IntelligentGuideDaoImpl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PlaceList extends Activity {

	private List<String> szone = new ArrayList<String>();
	public static String activityTitle = "";// 景点标题
	public static String file;// 音频文件夹名
	private String placeName=null;
	private String scenicName=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.placelist);
		
		placeName=getIntent().getStringExtra("placename");
		
		IntelligentGuideDaoImpl igdi=new IntelligentGuideDaoImpl(getApplicationContext());
		szone=igdi.searchScenicList(placeName);
		
		//组织界面跳转
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
	 * 列表点击事件
	 * 点击后进入景区对应景点中
	 * @author GaoRui
	 *
	 */
	public class ItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			//Layout1.assetsList.clear();
			activityTitle = ((TextView) arg1).getText().toString();
			file = activityTitle;
			Intent in = new Intent(PlaceList.this, MyTabHostFive.class);
			
			scenicName = activityTitle;
			Bundle bundle = new Bundle();  
			bundle.putCharSequence( "scenicname", scenicName );
			in.putExtras(bundle);
			
			startActivity(in);
		}
	}
}
