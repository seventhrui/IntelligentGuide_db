package com.seventh.intelligentguide.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.seventh.intelligentguide.R;
import com.seventh.intelligentguide.receiver.ActivityReceiver;
import com.seventh.intelligentguide.util.Player;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/**
 * android语言识别功能
 * 特别注意：如果手机有语言设别功能，请开启网络，因为系统会根据你的声音数据到google云端获取声音数据
 */
public class Layout4 extends Activity implements OnClickListener{
	private Button btn ;
	private ListView speacklistView;
	private static final int REQUEST_CODE = 1;
	private String scenic="";//语音识别 到一个景点名时直接播放时使用
	private ActivityReceiver areceiver;
	
	List<String> slist=new ArrayList<String>();//识别过滤后列表
	List<String> ssslist=new ArrayList<String>();//景点列表
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout4);
        btn = (Button) this.findViewById(R.id.btn);
        speacklistView = (ListView) this.findViewById(R.id.listview);
        areceiver=new ActivityReceiver();
        ssslist=areceiver.getAssetsList();
        /**
         * 下面是判断当前手机是否支持语音识别功能
         */
        PackageManager pm = getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
         
        if(list.size()!=0)
        {
        	btn.setOnClickListener(this);
        }else{
        	btn.setEnabled(false);
        	btn.setText("当前语音识别设备不可用...");
        }
    }

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn)
		{
			/**
			 * 启动手机内置的语言识别功能
			 */
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);  //设置为当前手机的语言类型
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "请说话，我识别");//出现语言识别界面上面需要显示的提示
			startActivityForResult(intent,REQUEST_CODE);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/**
		 * 回调获取从谷歌得到的数据
		 */
		if(requestCode==REQUEST_CODE&&resultCode==RESULT_OK)
		{
			List<String> list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			HashSet<String> h  =   new  HashSet<String>(list);     
			list.clear();     
			list.addAll(h);
			slist.clear();
			for(String s:list){
				for(String s1:ssslist)
					if(s1.contains(s))
						slist.add(s1);
			}
		}
		if(slist.size()==1){
			for(String s:slist)
				scenic=s;
			//MyTabHostFive.strText = scenic;
			Intent in = new Intent(Layout4.this, Player.class);
			
			Bundle bundle=new Bundle();
	        bundle.putCharSequence("yinpin", scenic);
	        in.putExtras(bundle);
			
			startActivity(in);
		}
		else if(slist.size()>1){
			speacklistView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,slist)); //把数据显示在listview中
			speacklistView.setOnItemClickListener(new ItemClickListener());
		}
		else
			Toast.makeText(getApplicationContext(), R.string.speakagain, 1).show();
		super.onActivityResult(requestCode, resultCode, data);
	}
	public class ItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Toast.makeText(getApplicationContext(), R.string.waiting, 1).show();
			//MyTabHostFive.strText = ((TextView)arg1).getText().toString();
			Intent in = new Intent(Layout4.this, Player.class);
			
			String scenic=((TextView)arg1).getText().toString();
			Bundle bundle=new Bundle();
	        bundle.putCharSequence("yinpin", scenic);
	        in.putExtras(bundle);
			
			startActivity(in);
		}
    }
}