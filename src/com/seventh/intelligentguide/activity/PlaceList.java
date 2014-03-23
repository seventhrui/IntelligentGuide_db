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
	public static String activityTitle = "";// �������
	public static String file;// ��Ƶ�ļ�����
	private String placeName=null;
	private String scenicName=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.placelist);
		
		placeName=getIntent().getStringExtra("placename");
		
		IntelligentGuideDaoImpl igdi=new IntelligentGuideDaoImpl(getApplicationContext());
		szone=igdi.searchScenicList(placeName);
		
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
	 * �б����¼�
	 * �������뾰����Ӧ������
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
