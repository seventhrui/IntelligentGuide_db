package com.seventh.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.seventh.R;
import com.seventh.tabhost.Layout1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Index extends Activity {
	private List<String> place =new ArrayList<String>();
	private static String placeTitle="";//地区标题
	public static String place_file="";//地区文件夹
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index);
		place=getPlace();
		Intent mainIntent = new Intent("android.intent.action.SQUARE", null);
        mainIntent.addCategory("android.intent.category.SQUARE");
		ListView zonglist = (ListView)this.findViewById(R.id.listview_place);
		zonglist.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,place));
		zonglist.setOnItemClickListener(new ItemClickListener());
	}

	private List<String> getPlace() {
		List<String> folderlist=new ArrayList<String>();
		try {
			String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/dao/";
			File file=new File(filePath);
			File[] files = file.listFiles();
			Arrays.sort(files);
			
			for(File mCurrentFile:files){
				if(mCurrentFile.isDirectory()){
					folderlist.add(mCurrentFile.getName());
					Log.v(null, mCurrentFile.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return folderlist;
	}
	public class ItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Layout1.assetsList.clear();
			placeTitle=((TextView)arg1).getText().toString();
			place_file=placeTitle;
			Intent in = new Intent(Index.this, daoyouqi.class);
			startActivity(in);
		}
    }

	// 菜单栏
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// int groupId, int itemId, int order, int titleRes
		menu.add(0, 1, 1, R.string.login);// 添加退出选项
		menu.add(0, 2, 2, R.string.recommend);// 添加推荐选项
		menu.add(0, 3, 3, R.string.advice);// 意见反馈
		menu.add(0, 4, 4, R.string.update);// 检查更新
		menu.add(0, 5, 5, R.string.about);// 添加关于选项(功能暂未添加)
		menu.add(0, 6, 6, R.string.exit);// 添加退出选项
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == 1){
			alertLogin("请登录","登陆");
		}
		else if (item.getItemId() == 2) {
			alertLogin("请输入朋友手机号","推荐");
		}
		else if (item.getItemId() == 3) {
			alertLogin("请填写您的意见","提交");
		}
		else if (item.getItemId() == 4) {
			alertUpdate();
		}
		else if (item.getItemId() == 5) {
			Toast.makeText(getApplicationContext(), R.string.aboutstring, 10)
					.show();
		}
		else if (item.getItemId() == 6) {
			finish();
		} 
		
		return super.onOptionsItemSelected(item);
	}
	/**
	 * 弹出框
	 * @param message
	 * @param action
	 */
	public void alertLogin(String message,String action){
		new AlertDialog.Builder(this)
		.setTitle(message)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setView(new EditText(this))
		.setPositiveButton(action, null)
		.setNegativeButton("取消", null)
		.show();
	}
	/**
	 * 单选框
	 */
	public void alertUpdate(){
		new AlertDialog.Builder(this)
		.setTitle("请选择")
		.setIcon(android.R.drawable.ic_dialog_info)                
		.setSingleChoiceItems(new String[] {"更新软件","更新所有景点","更新单个景点"}, 0, 
		  new DialogInterface.OnClickListener() {
		     public void onClick(DialogInterface dialog, int which) {
		        dialog.dismiss();
		        if(which==0||which==1)
			        Toast.makeText(getApplicationContext(), R.string.updated, 10)
					.show();
		        else
		        	alertLogin("请输入要更新的景点","提交");
		     }
		  }
		)
		.setNegativeButton("取消", null)
		.show();
	}
	// 按返回键退出应用
	private long exitTime = 0;//记录按返回键次数
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), R.string.returnexit,
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
