package com.seventh.intelligentguide.tabhost;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.seventh.intelligentguide.R;
import com.seventh.intelligentguide.Index;
import com.seventh.intelligentguide.dao.impl.IntelligentGuideDaoImpl;
import com.seventh.intelligentguide.util.Player;

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
import android.widget.Toast;

public class Layout1 extends Activity {
    /** Called when the activity is first created. */
	
	public static List<String> assetsList = new ArrayList<String>();
	private String filename="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout1);
        
        filename=PlaceList.file;
        
        Intent mainIntent = new Intent("android.intent.action.SQUARE", null);
        mainIntent.addCategory("android.intent.category.SQUARE");
        
        TextView titleTextView=(TextView) this.findViewById(R.id.text_layout1);
        titleTextView.setText(PlaceList.activityTitle);
        ListView lv = (ListView)this.findViewById(R.id.lv);
        assetsList=getData(filename);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,assetsList));
        lv.setOnItemClickListener(new ItemClickListener());
    }
    //获取文件中的音频文件列表
    private List<String> getData(String path){
		try {
			String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/dao/"+Index.place_file+"/"+path+"/";
			File file=new File(filePath);
			File[] files = file.listFiles();
			Arrays.sort(files);
			for(File mCurrentFile:files){
				if(mCurrentFile.getName().endsWith(".grv")){
					assetsList.add(mCurrentFile.getName().substring(0, mCurrentFile.getName().length()-4));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return assetsList;
	}
    
    public class ItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Toast.makeText(getApplicationContext(), R.string.waiting, 1).show();
			
			IntelligentGuideDaoImpl igdi=new IntelligentGuideDaoImpl(getApplicationContext());
			//String string=igdi.findScenicNumber("0");
			igdi.searchCityList();
			//Log.v(null, "输出的："+string);
			
			MyTabHostFive.strText = ((TextView)arg1).getText().toString();
			Intent in = new Intent(Layout1.this, Player.class);
			startActivity(in);
		}
    }
}