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
	public static String activityTitle = "";// �������
	//public List<Correspod> NamePinyinJd=null;//�洢�������Ƽ���Ӧƴ��
	public static String file;// ��Ƶ�ļ�����
	private File logFile=null;
	private static List<String> LogList=new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//ʱ�����
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date nowDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		String str = sdf.format(nowDate);
		//Log.v("·��", Environment.getExternalStorageDirectory() + "/dao");
		// //////////////////////д��־����¼ʱ�䣩
		logFile = new File(Environment.getExternalStorageDirectory()
				+ "/dao/log.x");
		if(!logIsExist(logFile))
			alertDialog("��Դ�ļ�ȱʧ��������������Դ�ļ�");
		else if(fileIsNull(logFile)){
			writeLog(logFile,str);
		}
		else {
			LogList=readLog(logFile);
			if(LogList.get(LogList.size()-1).compareTo(str)>=0)
				alertDialog("������ֻ�ʱ�䣬����Ӧ�ò�������ʹ��");
			else{
				writeLog(logFile,str);
				// �ж��Ƿ����
				//Log.v("ʱ��", str);
				try {
					// str//��ȡ���δ�ʱ��
					Date curDate = sdf.parse(LogList.get(0));
					long lo = 0;
					lo = (nowDate.getTime() - curDate.getTime()) / 1000 / 60 / 60 / 24;//����ʹ������
					//����ʹ����Чʱ��Ϊ4��
					if (lo >= 4 || lo <= -4) {
						//Log.v("�Ƿ����", "����" + lo);
						//alertDialog("����Ӧ���ѹ���");
						alertLogin("����Ӧ���ѹ���,�����빺������к�","�ύ");
					} else {
						//Log.v("�Ƿ����", "û����" + lo);
						///////////////////////////
						szone = getData();//��ȡ�����б�
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
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * ���������
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
			//����
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
					Toast.makeText(getApplicationContext(), "��������,����������Ӧ��",
							Toast.LENGTH_SHORT).show();
				}
			}
		})
		.setNegativeButton("ȡ��", null)
		.show();
		
	}
	/**
	 * �����Ի���
	 * @param message
	 */
	public void alertDialog(String message){
		new AlertDialog.Builder(this)
		.setTitle("��ʾ")
		.setMessage(message+"\nȷ�����˳�")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setPositiveButton("ȷ��",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						setResult(RESULT_OK);// ȷ����ť�¼�
						finish();
					}
				})
		.setNegativeButton("ȡ��",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						// ȡ����ť�¼�
					}
				}).show();
	}
	/**
	 * �����Դ������(�ж��ļ��Ƿ����)
	 * @param file
	 * @return boolean
	 */
	private boolean logIsExist(File file){
		return file.exists();
	}
	/**
	 * �ж��ļ��Ƿ�Ϊ��
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
	 * д��־
	 */
	private void writeLog(File logFile,String str){
		//д��־
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
	 * ����־�ļ�
	 * @return List<String> ��־��¼
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
		//Log.v("��ȡ", readList.get(0));
		return readList;
	}
	/**
	 * ��ȡ�����б�(dao�ļ����е��ļ����б�)
	 * @return List<String> �����б�
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
		List<Correspod> list = new ArrayList<Correspod>();// ������Ϣ�б�
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
	 * �б����¼�
	 * �������뾰����Ӧ������
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
