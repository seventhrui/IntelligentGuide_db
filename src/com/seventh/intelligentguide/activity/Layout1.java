package com.seventh.intelligentguide.activity;

import java.util.ArrayList;
import java.util.List;

import com.seventh.intelligentguide.R;
import com.seventh.intelligentguide.dao.impl.IntelligentGuideDaoImpl;
import com.seventh.intelligentguide.receiver.ActivityReceiver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Layout1 extends Activity {
	
	private static List<String> assetsList = new ArrayList<String>();
	private String filename="";
	private ProgressDialog pargressLoading = null;//资源加载进度条
	private Thread thread = null;
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
        
        IntelligentGuideDaoImpl igdi=new IntelligentGuideDaoImpl(getApplicationContext());
        assetsList=igdi.searchSpotsNameList(filename);
        
        Intent it = new Intent(this,ActivityReceiver.class);
        it.setAction("com.seventh.intelligentguide.tabhost.Layout1.assetslist");
        it.putStringArrayListExtra("assetslist", (ArrayList<String>) assetsList);
        sendBroadcast(it);//广播景点列表
		
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,assetsList));
        lv.setOnItemClickListener(new ItemClickListener());
    }

    public class ItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, final View arg1, int arg2,
				long arg3) {
			pargressLoading = new ProgressDialog(Layout1.this);
			pargressLoading.setMax(100);
			pargressLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);//progressBarStyleLarge

			pargressLoading.setTitle("资源加载中,请稍候");
			pargressLoading.setIndeterminate(true);
			pargressLoading.setCancelable(true);
			pargressLoading.setCanceledOnTouchOutside(false);
			pargressLoading.setCancelable(true);
			pargressLoading
					.setOnCancelListener(new DialogInterface.OnCancelListener() {
						public void onCancel(DialogInterface arg0) {
							
						}
					});

			pargressLoading.show();
			thread = new Thread() {
				@Override
				public void run() {
					pargressLoading.dismiss();
					
					String yinpin=((TextView)arg1).getText().toString();
					
					Intent in = new Intent(Layout1.this, Player.class);
					Bundle bundle=new Bundle();
					bundle.putCharSequence("yinpin", yinpin);
					in.putExtras(bundle);
					startActivity(in);
				}
			};
			thread.start();
		}
	}
}