package com.seventh.main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	private List<String> place = new ArrayList<String>();
	private static String placeTitle = "";// ��������
	public static String place_file = "";// �����ļ���

	private SimpleDateFormat sdf;// ʱ���ʽ
	private Date nowDate;// ��ǰʱ��
	private String timestr;// ��ǰʱ���ַ���

	private File logFile = null;// ��־�ļ�
	private static List<String> logList;// ��־�б�

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index);

		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		nowDate = new Date(System.currentTimeMillis());
		timestr = sdf.format(nowDate);
		logFile = new File(Environment.getExternalStorageDirectory()
				+ "/dao/log.x");
		if (!fileIsExist(logFile))
			alertDialog("��Դ�ļ�ȱʧ��������������Դ�ļ�");
		else if (fileIsNull(logFile)) {
			writeLog(logFile, timestr);
			goToPlace();
		} else {
			logList = readLog(logFile);
			if (logList.get(logList.size() - 1).compareTo(timestr) >= 0) {
				alertDialog("������ֻ�ʱ�䣬����Ӧ�ò�������ʹ��");
			} else {
				writeLog(logFile, timestr);
				try {
					Date curDate = sdf.parse(logList.get(0));
					long lo = 0;
					Log.v(null, "��ǰʱ�䣺" + nowDate + ",�Ƚ�ʱ��" + curDate);
					lo = (nowDate.getTime() - curDate.getTime()) / 1000 / 60
							/ 60 / 24;
					if (lo >= 4 || lo <= -4) {
						alertLogin("����Ӧ���ѹ��ڣ������빺������к�", "�ύ");
					} else {
						goToPlace();
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * ��ת���ص��б�
	 */
	private void goToPlace(){
		place = getPlace();
		Intent mainIntent = new Intent(
				"android.intent.action.SQUARE", null);
		mainIntent
				.addCategory("android.intent.category.SQUARE");
		ListView zonglist = (ListView) this
				.findViewById(R.id.listview_place);
		zonglist.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1,
				place));
		zonglist.setOnItemClickListener(new ItemClickListener());
	}
	
	/**
	 * �����Դ������(�ж��ļ��Ƿ����)
	 * @param file
	 * @return boolean
	 */
	private boolean fileIsExist(File file) {
		return file.exists();
	}

	/**
	 * �ж��ļ��Ƿ�Ϊ��
	 * @param file
	 * @return boolean
	 * @throws Exception
	 */
	private boolean fileIsNull(File file) {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		Boolean isNull=true;
		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			if (bis.read() == -1) {
				
				isNull=true;
			} else{
				isNull=false;
			}
			bis.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isNull;
	}

	/**
	 * д��־
	 * @param logFile
	 * @param str
	 */
	private void writeLog(File logFile, String str) {
		// д��־
		FileWriter writer = null;
		try {
			writer = new FileWriter(logFile, true);
			writer.write(str + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����־�ļ�
	 * @param logFile
	 * @return
	 */
	private List<String> readLog(File logFile) {
		InputStream is = null;
		BufferedReader br = null;
		List<String> readList = new ArrayList<String>();
		String tmp;
		try {
			is = new BufferedInputStream(new FileInputStream(logFile));
			br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			while ((tmp = br.readLine()) != null) {
				readList.add(tmp);
			}
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return readList;
	}

	/**
	 * ��ȡ�����б�
	 * 
	 * @return
	 */
	private List<String> getPlace() {
		List<String> folderlist = new ArrayList<String>();
		try {
			String filePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/dao/";
			File file = new File(filePath);
			File[] files = file.listFiles();
			Arrays.sort(files);

			for (File mCurrentFile : files) {
				if (mCurrentFile.isDirectory()) {
					folderlist.add(mCurrentFile.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return folderlist;
	}

	public class ItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Layout1.assetsList.clear();
			placeTitle = ((TextView) arg1).getText().toString();
			place_file = placeTitle;
			Intent in = new Intent(Index.this, daoyouqi.class);
			startActivity(in);
		}
	}

	// �˵���
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// int groupId, int itemId, int order, int titleRes
		menu.add(0, 1, 1, R.string.login);// ����˳�ѡ��
		menu.add(0, 2, 2, R.string.recommend);// ����Ƽ�ѡ��
		menu.add(0, 3, 3, R.string.advice);// �������
		menu.add(0, 4, 4, R.string.update);// ������
		menu.add(0, 5, 5, R.string.about);// ��ӹ���ѡ��(������δ���)
		menu.add(0, 6, 6, R.string.exit);// ����˳�ѡ��
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			alertLogin("���¼", "��½");
		} else if (item.getItemId() == 2) {
			alertLogin("�����������ֻ���", "�Ƽ�");
		} else if (item.getItemId() == 3) {
			alertLogin("����д�������", "�ύ");
		} else if (item.getItemId() == 4) {
			alertUpdate();
		} else if (item.getItemId() == 5) {
			Toast.makeText(getApplicationContext(), R.string.aboutstring, 10)
					.show();
		} else if (item.getItemId() == 6) {
			finish();
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * ������
	 * @param message
	 * @param action
	 */
	public void alertLogin(String message, String action) {
		new AlertDialog.Builder(this).setTitle(message)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(new EditText(this)).setPositiveButton(action, null)
				.setNegativeButton("ȡ��", null).show();
	}
	
	/**
	 * �����Ի���
	 * @param message
	 */
	public void alertDialog(String message){
		new AlertDialog.Builder(this)
		.setTitle("��ʾ")
		.setMessage(message+"\n(ȷ�����˳�)")
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
	 * ��ѡ��
	 */
	public void alertUpdate() {
		new AlertDialog.Builder(this)
				.setTitle("��ѡ��")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(
						new String[] { "�������", "�������о���", "���µ�������" }, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								if (which == 0 || which == 1)
									Toast.makeText(getApplicationContext(),
											R.string.updated, 10).show();
								else
									alertLogin("������Ҫ���µľ���", "�ύ");
							}
						}).setNegativeButton("ȡ��", null).show();
	}

	// �����ؼ��˳�Ӧ��
	private long exitTime = 0;// ��¼�����ؼ�����

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
