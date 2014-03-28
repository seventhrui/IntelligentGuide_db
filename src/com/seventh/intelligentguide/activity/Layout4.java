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
 * android����ʶ����
 * �ر�ע�⣺����ֻ�����������ܣ��뿪�����磬��Ϊϵͳ���������������ݵ�google�ƶ˻�ȡ��������
 */
public class Layout4 extends Activity implements OnClickListener{
	private Button btn ;
	private ListView speacklistView;
	private static final int REQUEST_CODE = 1;
	private String scenic="";//����ʶ�� ��һ��������ʱֱ�Ӳ���ʱʹ��
	private ActivityReceiver areceiver;
	
	List<String> slist=new ArrayList<String>();//ʶ����˺��б�
	List<String> ssslist=new ArrayList<String>();//�����б�
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout4);
        btn = (Button) this.findViewById(R.id.btn);
        speacklistView = (ListView) this.findViewById(R.id.listview);
        areceiver=new ActivityReceiver();
        ssslist=areceiver.getAssetsList();
        /**
         * �������жϵ�ǰ�ֻ��Ƿ�֧������ʶ����
         */
        PackageManager pm = getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
         
        if(list.size()!=0)
        {
        	btn.setOnClickListener(this);
        }else{
        	btn.setEnabled(false);
        	btn.setText("��ǰ����ʶ���豸������...");
        }
    }

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn)
		{
			/**
			 * �����ֻ����õ�����ʶ����
			 */
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);  //����Ϊ��ǰ�ֻ�����������
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "��˵������ʶ��");//��������ʶ�����������Ҫ��ʾ����ʾ
			startActivityForResult(intent,REQUEST_CODE);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/**
		 * �ص���ȡ�ӹȸ�õ�������
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
			speacklistView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,slist)); //��������ʾ��listview��
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