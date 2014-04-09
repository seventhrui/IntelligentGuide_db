package com.seventh.intelligentguide.activity;

import com.seventh.intelligentguide.R;
import com.seventh.intelligentguide.dao.impl.IntelligentGuideDaoImpl;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Layout2 extends Activity {

	private TextView keyinput;
	private String keyString="";//������ַ�������ѡ�е��ļ�����
	private String scenicName=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout2);
		keyinput = (TextView) this.findViewById(R.id.keypadview_word);
	}
	
	//��������
	public void keyinput(View v) {
		if(keyString.equals("�����뾰���Ӧ����,��ȷ������"))
			keyString="";
		switch (v.getId()) {
			case R.id.keypad_num_0:
				keyString+="0";
				break;
			case R.id.keypad_num_1:
				keyString+="1";
				break;
			case R.id.keypad_num_2:
				keyString+="2";
				break;
			case R.id.keypad_num_3:
				keyString+="3";
				break;
			case R.id.keypad_num_4:
				keyString+="4";
				break;
			case R.id.keypad_num_5:
				keyString+="5";
				break;
			case R.id.keypad_num_6:
				keyString+="6";
				break;
			case R.id.keypad_num_7:
				keyString+="7";
				break;
			case R.id.keypad_num_8:
				keyString+="8";
				break;
			case R.id.keypad_num_9:
				keyString+="9";
				break;
			case R.id.keypadview_ritht_btn:
				Toast.makeText(getApplicationContext(), R.string.waiting, 1).show();
				Intent mainIntent = new Intent("android.intent.action.SQUARE", null);
		        mainIntent.addCategory("android.intent.category.SQUARE");
		        Intent in = new Intent(Layout2.this, Player.class);
		        
		        scenicName=getIntent().getStringExtra("scenicname");
		        Log.v("scenicName_Layout2", scenicName);
		        Log.v("PlaceList.file_Layout2", PlaceList.file);
		        
		        IntelligentGuideDaoImpl igdi=new IntelligentGuideDaoImpl(getApplicationContext());
		        String spots_name=igdi.getSpotsByNumber(scenicName, keyString);
		        Bundle bundle=new Bundle();
		        Log.v("��Ƶ��_Layout2", spots_name);
		        bundle.putCharSequence("yinpin", spots_name);
		        in.putExtras(bundle);
		        
		        startActivity(in);

		        keyString="�����뾰���Ӧ����,��ȷ������";
				break;
			case R.id.keypad_num_backspace:
				keyString="";
				break;
		}
		keyinput.setText(keyString);
	}
}