package com.seventh.intelligentguide.asynctask;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipException;

import org.apache.http.HttpStatus;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

/**
 * 异步下载任务 params共有三个参数:远程文件url、本地下载目录、存储文件名
 * 
 * 
 */
public class DownloadAsyncTask extends AsyncTask<String, Integer, Boolean> {

	private int per = 0;
	private ProgressDialog perDialog = null;
	private String fullPath = null;
	private String filepath = null;
	private Context context;

	public DownloadAsyncTask(Context c) {
		this.context = c;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		filepath = Environment.getExternalStorageDirectory() + "";
		fullPath = params[1] + params[2];
		try {
			URL url = new URL(params[0]);
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();
			huc.setConnectTimeout(10 * 1000);
			huc.connect();
			if (huc.getResponseCode() == HttpStatus.SC_OK) {
				perDialog.setMax(huc.getContentLength());
				File path = new File(params[1]);
				if (!path.exists()) {
					path.mkdirs();
				}
				File apkFile = new File(path, params[2]);
				if (!apkFile.exists()) {
					apkFile.createNewFile();
				}
				InputStream is = huc.getInputStream();
				FileOutputStream fos = new FileOutputStream(apkFile);
				byte[] buf = new byte[1024];
				int readSize;
				while (true) {
					readSize = is.read(buf);
					if (readSize <= 0) {
						break;
					}
					per += readSize;
					this.publishProgress(per);
					fos.write(buf, 0, readSize);
				}
				fos.close();
				is.close();
				return true;
			} else {
				return false;
			}
		} catch (MalformedURLException e) {
			return false;
		} catch (IOException e) {
			return false;

		}
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		perDialog.dismiss();
		if (result) {
			Log.v("---下载-->", "下载完成");
			try {
				unZipFile(fullPath, filepath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ZipException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Log.v("---下载-->", "下载失败");
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		perDialog = new ProgressDialog(context);
		perDialog.setTitle("资源下载中...");
		perDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		perDialog.setCancelable(false);
		perDialog.show();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		perDialog.setProgress(values[0]);
		Log.v("---下载进度---->", values[0] + "");
	}

	/**
	 * 解压文件
	 * 
	 * @param archive
	 * @param decompressDir
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ZipException
	 */
	private void unZipFile(String archive, String decompressDir)
			throws IOException, FileNotFoundException, ZipException {
		BufferedInputStream bi;
		ZipFile zf = new ZipFile(archive, "GBK");
		File file = new File(archive);
		Enumeration e = zf.getEntries();
		while (e.hasMoreElements()) {
			ZipEntry ze2 = (ZipEntry) e.nextElement();
			String entryName = ze2.getName();
			String path = decompressDir + "/" + entryName;
			if (ze2.isDirectory()) {
				System.out.println("正在创建解压目录 - " + entryName);
				File decompressDirFile = new File(path);
				if (!decompressDirFile.exists()) {
					decompressDirFile.mkdirs();
				}
			} else {
				System.out.println("正在创建解压文件 - " + entryName);
				String fileDir = path.substring(0, path.lastIndexOf("/"));
				File fileDirFile = new File(fileDir);
				if (!fileDirFile.exists()) {
					fileDirFile.mkdirs();
				}
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(decompressDir + "/" + entryName));
				bi = new BufferedInputStream(zf.getInputStream(ze2));
				byte[] readContent = new byte[1024];
				int readCount = bi.read(readContent);
				while (readCount != -1) {
					bos.write(readContent, 0, readCount);
					readCount = bi.read(readContent);
				}
				bos.close();
			}
		}
		zf.close();
		for (int i = 0; !file.delete() && i <= 20; i++);
	}
}
