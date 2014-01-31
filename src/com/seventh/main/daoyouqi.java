package com.seventh.main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.seventh.R;
import com.seventh.tabhost.Layout1;
import com.seventh.tabhost.MyTabHostFive;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class daoyouqi extends Activity {

	private List<String> szone = new ArrayList<String>();
	public static String activityTitle = "";// 景点标题
	//public List<Correspod> NamePinyinJd=null;//存储景点名称及对应拼音
	public static String file;// 音频文件夹名
	private File logFile=null;
	private static List<String> LogList=new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//时间加密
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date nowDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = sdf.format(nowDate);
		//Log.v("路径", Environment.getExternalStorageDirectory() + "/dao");
		// //////////////////////写日志（记录时间）
		logFile = new File(Environment.getExternalStorageDirectory()
				+ "/dao/log.x");
		if(!logIsExist(logFile))
			alertDialog("资源文件缺失，请重新下载资源文件");
		else if(fileIsNull(logFile)){
			writeLog(logFile,str);
		}
		else {
			LogList=readLog(logFile);
			if(LogList.get(LogList.size()-1).compareTo(str)>=0)
				alertDialog("请调整手机时间，否则应用不能正常使用");
			else{
				writeLog(logFile,str);
				// 判断是否过期
				//Log.v("时间", str);
				try {
					// str//获取本次打开时间
					Date curDate = sdf.parse(LogList.get(0));
					long lo = 0;
					lo = (nowDate.getTime() - curDate.getTime()) / 1000 / 60 / 60 / 24;//计算使用天数
					//程序使用有效时间为4天
					if (lo >= 4 || lo <= -4) {
						//Log.v("是否过期", "过期" + lo);
						//alertDialog("您的应用已过期");
						alertLogin("您的应用已过期,请输入购买的序列号","提交");
					} else {
						//Log.v("是否过期", "没过期" + lo);
						///////////////////////////
						szone = getData();//获取景点列表
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
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 弹出输入框
	 * @param message
	 * @param action
	 */
	public void alertLogin(String message,String action){
		final EditText txt=new EditText(this);
		new AlertDialog.Builder(this)
		.setTitle(message)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setView(txt)
		.setPositiveButton(action, new OnClickListener(){
			//续费
			public void onClick(DialogInterface dialog, int which) {
				String password=txt.getText().toString();
				if(password.equals("9639")){
					File f = new File(Environment.getExternalStorageDirectory()
							+ "/dao/log.x");
					FileWriter fw;
					try {
						fw = new FileWriter(f);
						fw.write("");
						fw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					Toast.makeText(getApplicationContext(), "您已续费,请重新启动应用",
							Toast.LENGTH_SHORT).show();
				}
			}
		})
		.setNegativeButton("取消", null)
		.show();
		
	}
	/**
	 * 弹出对话框
	 * @param message
	 */
	public void alertDialog(String message){
		new AlertDialog.Builder(this)
		.setTitle("提示")
		.setMessage(message+"\n确定键退出")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						setResult(RESULT_OK);// 确定按钮事件
						finish();
					}
				})
		.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						// 取消按钮事件
					}
				}).show();
	}
	/**
	 * 检查资源完整性(判断文件是否存在)
	 * @param file
	 * @return boolean
	 */
	private boolean logIsExist(File file){
		return file.exists();
	}
	/**
	 * 判断文件是否为空
	 * @param file
	 * @return boolean
	 * @throws IOException
	 */
	private boolean fileIsNull(File file){
		FileInputStream fis=null;
		BufferedInputStream bis=null;
		try {
			fis=new FileInputStream(file);
			bis=new BufferedInputStream(fis);
			if(bis.read()==-1){
				return true;
			}
			else
				return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			bis.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	/**
	 * 写日志
	 */
	private void writeLog(File logFile,String str){
		//写日志
		FileWriter writer=null;
		try {
			writer = new FileWriter(logFile, true);
			writer.write(str + "\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 读日志文件
	 * @return List<String> 日志记录
	 */
	private List<String> readLog(File logFile){
		InputStream is = null;
		BufferedReader br = null;
		List<String> readList=new ArrayList<String>();
		String tmp;
		try {
			is = new BufferedInputStream(new FileInputStream(logFile));
			br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			while((tmp=br.readLine())!=null){
				readList.add(tmp);
			}
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//tmp = br.readLine();
		//Log.v("读取", readList.get(0));
		return readList;
	}
	/**
	 * 获取景区列表(dao文件夹中的文件夹列表)
	 * @return List<String> 景点列表
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
	/*
	private List<Correspod> getCorrespod() {
		String s = "";
		List<Correspod> list = new ArrayList<Correspod>();// 景点信息列表
		String[] temp = new String[2];
		try {
			InputStream in = getResources().getAssets().open("correspond.jdx");
			BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
			while ((s = bfr.readLine()) != null) {
				temp = s.split(",");
				list.add(new Correspod(temp[0],temp[1]));
			}
			bfr.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	*/
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
			Layout1.assetsList.clear();
			activityTitle = ((TextView) arg1).getText().toString();
			file = activityTitle;
			
			/*NamePinyinJd=getCorrespod();//filePinyin
			for(Correspod c:NamePinyinJd){
				if(c.getName().equals(file)){
					filePinyin=c.getCode();
					break;
				}
			}*/
			Intent in = new Intent(daoyouqi.this, MyTabHostFive.class);
			startActivity(in);
		}
	}
}
