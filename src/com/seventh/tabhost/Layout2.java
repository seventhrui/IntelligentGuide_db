package com.seventh.tabhost;

import com.seventh.R;
import com.seventh.util.Player;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Layout2 extends Activity {

	private TextView keyinput;
	private String keyString="";//输入的字符串（即选中的文件名）
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout2);
		keyinput = (TextView) this.findViewById(R.id.keypadview_word);
	}
	
	//按键操作
	public void keyinput(View v) {
		if(keyString.equals("请输入景点对应数字,按确定收听"))
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
		        int length=keyString.length();
		        for(int i=0;i<3-length;i++)
		        	keyString="0"+keyString;
		        for(Object o:Layout1.assetsList){
		        	if(o.toString().startsWith(keyString+".")){
		        		keyString=o.toString();
		        	}
		        }
		        if(keyString.indexOf(".")>0){
		        	Toast.makeText(getApplicationContext(), R.string.waiting, 1).show();
			        MyTabHostFive.strText=keyString;
			        startActivity(in);
		        }
		        else {
		        	Toast.makeText(getApplicationContext(), R.string.filenoexist, 1).show();
				}
		        keyString="请输入景点对应数字,按确定收听";
				break;
			case R.id.keypad_num_backspace:
				keyString="";
				break;
		}
		keyinput.setText(keyString);
	}
}