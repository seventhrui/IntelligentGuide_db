package com.seventh.intelligentguide.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

import com.seventh.intelligentguide.R;
import com.seventh.intelligentguide.beans.GPSparameter;
import com.seventh.intelligentguide.beans.LatLon;
import com.seventh.intelligentguide.beans.ScenicSpotBean;
import com.seventh.intelligentguide.dao.impl.IntelligentGuideDaoImpl;
import com.seventh.intelligentguide.util.GPSHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

public class Layout5 extends Activity {
	private TextView tv_Lon;//����
	private TextView tv_lat;//γ��
	private TextView TxtViewScenicName;//������
	public static String nowVoiceName = "";
	private int sum = 0;//���ǿ���
	private LocationManager lm;
	private GPSparameter gpsp;//����GPS����
	private LatLon lalo;//���㾭γ��
	private static final String TAG = "GpsActivity";
	private String scenicName=null;//��������
	private String path = null;// �ļ�·��
	public static MediaPlayer mediaPlayer = null;// ��ý�岥��
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		lm.removeUpdates(locationListener);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout5);

		mediaPlayer = new MediaPlayer();

		scenicName=getIntent().getStringExtra("scenicname");

		tv_Lon = (TextView) findViewById(R.id.jingdu);
		tv_lat = (TextView) findViewById(R.id.weidu);
		TxtViewScenicName = (TextView) findViewById(R.id.scenicName);
		try {
			lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		} catch (Exception e) {

		}

		// �ж�GPS�Ƿ���������
		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Toast.makeText(this, "�뿪��GPS...", Toast.LENGTH_SHORT).show();
			// ���ؿ���GPS�������ý���
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(intent, 0);
			// return;
		}
		
		// Ϊ��ȡ����λ����Ϣʱ���ò�ѯ����
		String bestProvider = lm.getBestProvider(getCriteria(), true);
		// ��ȡλ����Ϣ
		// ��������ò�ѯҪ��getLastKnownLocation�������˵Ĳ���ΪLocationManager.GPS_PROVIDER
		Location location = lm.getLastKnownLocation(bestProvider);
		
		//��ȡ��ǰ��γ�Ȳ���
		if (location!= null) {
			lalo=new LatLon(location.getLatitude(),location.getLongitude());
			gpsp=GPSHelper.getGPSHelper().getCanshu(lalo);
			Log.v("���Ծ�γ��", lalo.toString());
			Log.v("GPS������", GPSHelper.getGPSHelper().getCanshu(lalo).toString());
		}
		updateView(location);
		// ����״̬
		lm.addGpsStatusListener(listener);
		// �󶨼�������4������
		// ����1���豸����GPS_PROVIDER��NETWORK_PROVIDER����
		// ����2��λ����Ϣ�������ڣ���λ����
		// ����3��λ�ñ仯��С���룺��λ�þ���仯������ֵʱ��������λ����Ϣ
		// ����4������
		// ��ע������2��3���������3��Ϊ0�����Բ���3Ϊ׼������3Ϊ0����ͨ��ʱ������ʱ���£�����Ϊ0������ʱˢ��
		// 1�����һ�Σ�����Сλ�Ʊ仯����1�׸���һ�Σ�
		// ע�⣺�˴�����׼ȷ�ȷǳ��ͣ��Ƽ���service��������һ��Thread����run��sleep(10000);Ȼ��ִ��handler.sendMessage(),����λ��
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1,locationListener);
		//��������״̬
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(new MyPhoneListener(),
				PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	// λ�ü���
	private LocationListener locationListener = new LocationListener() {
		/**
		 * λ����Ϣ�仯ʱ����
		 */
		public void onLocationChanged(Location location) {
			updateView(location);
			// Log.i(TAG, "ʱ�䣺"+location.getTime());
			// Log.i(TAG, "���ȣ�"+location.getLongitude());
			// Log.i(TAG, "γ�ȣ�"+location.getLatitude());
			// Log.i(TAG, "���Σ�"+location.getAltitude());
			// Log.i(TAG, "���ȣ�"+location.getAccuracy());
			// Log.i(TAG, "�ص㣺"+getAddressbyGeoPoint(location));
		}

		// GPS״̬�仯ʱ����
		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
			case LocationProvider.AVAILABLE:
				Log.i(TAG, "��ǰGPS״̬Ϊ�ɼ�״̬");
				break;
			case LocationProvider.OUT_OF_SERVICE:
				Log.i(TAG, "��ǰGPS״̬Ϊ��������״̬");
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				Log.i(TAG, "��ǰGPS״̬Ϊ��ͣ����״̬");
				break;
			}
		}

		// GPS����ʱ����
		public void onProviderEnabled(String provider) {
			Location location = lm.getLastKnownLocation(provider);
			updateView(location);
		}

		// GPS����ʱ����
		public void onProviderDisabled(String provider) {
			updateView(null);
		}
	};

	// ״̬����
	GpsStatus.Listener listener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) {
			switch (event) {
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				Log.i(TAG, "��һ�ζ�λ");
				break;
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				Log.i(TAG, "����״̬�ı�");
				// ��ȡ��ǰ״̬
				GpsStatus gpsStatus = lm.getGpsStatus(null);
				// ��ȡ���ǿ�����Ĭ�����ֵ
				int maxSatellites = gpsStatus.getMaxSatellites();
				// ����һ��������������������
				Iterator<GpsSatellite> iters = gpsStatus.getSatellites()
						.iterator();
				int count = 0;
				while (iters.hasNext() && count <= maxSatellites) {
					GpsSatellite s = iters.next();
					count++;
				}
				Log.i(TAG, "��������" + count + "������");
				sum = count;
				break;
			case GpsStatus.GPS_EVENT_STARTED:
				Log.i(TAG, "��λ����");
				break;
			case GpsStatus.GPS_EVENT_STOPPED:
				Log.i(TAG, "��λ����");
				break;
			}
		};
	};

	/**
	 * ʵʱ�����ı�����
	 * 
	 * @param location
	 */
	private void updateView(Location location) {
		if (location != null) {
			
			/*lalo=new LatLon(location.getLatitude(),location.getLongitude());
			Log.v("���Ծ�γ��", lalo.toString());
			Log.v("GPS������", GPSHelper.getGPSHelper().getCanshu(lalo).toString());*/
			
			
			tv_Lon.setText("����:" + String.valueOf(location.getLongitude())
					+ "," + "γ��:" + String.valueOf(location.getLatitude()));
			tv_lat.setText("������:" + sum + "������");
			String ScenicName = null;

			IntelligentGuideDaoImpl igdi = new IntelligentGuideDaoImpl(getApplicationContext());
			ScenicSpotBean ss = igdi.getSpotsByLoandLa(scenicName,location.getLongitude(), location.getLatitude(), gpsp);
			if (ss != null)
				ScenicName = ss.getSpots_name();
			if (ScenicName != null) {
				if (!nowVoiceName.equals(ScenicName)) {
					nowVoiceName = ScenicName;
					TxtViewScenicName.setText("��ǰ:" + nowVoiceName);
					// ����
					play2(nowVoiceName);
				}
			} else if (mediaPlayer.isPlaying())
				TxtViewScenicName.setText("��ǰ:" + nowVoiceName);
			else
				TxtViewScenicName.setText("�˴�δ̽�⵽����");
		} else {
			// ���Txtview1����
			tv_Lon.setText("");
			tv_lat.setText("");
			TxtViewScenicName.setText("������������......");
		}
	}

	/**
	 * ���ز�ѯ����
	 * 
	 * @return
	 */
	private Criteria getCriteria() {
		Criteria criteria = new Criteria();
		// ���ö�λ��ȷ�� Criteria.ACCURACY_COARSE�Ƚϴ��ԣ�Criteria.ACCURACY_FINE��ȽϾ�ϸ
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// �����Ƿ�Ҫ���ٶ�
		criteria.setSpeedRequired(false);
		// �����Ƿ�������Ӫ���շ�
		criteria.setCostAllowed(true);
		// �����Ƿ���Ҫ��λ��Ϣ
		criteria.setBearingRequired(false);
		// �����Ƿ���Ҫ������Ϣ
		criteria.setAltitudeRequired(false);
		// ���öԵ�Դ������
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		return criteria;
	}

	private void play(int position) {
		try {
			mediaPlayer.reset();// �Ѹ��������λ
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();// �������ݻ���
			mediaPlayer.setOnPreparedListener(new PrepareListener(position));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void play2(String file) {
		IntelligentGuideDaoImpl igdi = new IntelligentGuideDaoImpl(
				getApplicationContext());
		ScenicSpotBean ss = igdi.searchSpotsBySpots_name(file);
		String filname = ss.getFile_name();

		File src = new File(Environment.getExternalStorageDirectory() + filname);
		File dest = new File(Environment.getExternalStorageDirectory()
				+ "/dao/play.mp3");
		try {
			xorEn(src, dest);
		} catch (Exception e) {
		}
		File audio = dest;
		if (audio.exists()) {
			path = audio.getPath();
			play(0);// ��ͷ��ʼ����
			int i = 0;
			for (i = 0; !dest.delete() && i <= 20; i++)
				;
		} else {
			path = null;
			Toast.makeText(getApplicationContext(), R.string.filenoexist, 1)
					.show();
		}
	}

	// �����ܽ���
	public static void xorEn(File src, File dest) throws Exception {
		// �ļ������ڻ�Ϊ�ļ��оͲ��ж���
		String kk = R.string.keykey + "";
		String k = kk.charAt(0) + "";
		final int a = Integer.parseInt(k) * 9;

		class test {
			int[] i = { a, a + 1, a - 1, a + 2 };
		}
		test t = new test();
		FileInputStream fis = new FileInputStream(src);
		FileOutputStream fos = new FileOutputStream(dest);
		byte[] bs = new byte[1024];
		int len = 0;
		while ((len = fis.read(bs)) != -1) {
			for (int i = 0; i < len; i++) {
				bs[i] ^= t.i[i % 4];
			}
			fos.write(bs, 0, len);
		}
		fos.close();
		fis.close();
	}

	private final class PrepareListener implements OnPreparedListener {
		private int position;

		public PrepareListener(int position) {
			this.position = position;
		}

		public void onPrepared(MediaPlayer mp) {
			mediaPlayer.start();// ��ʼ����
			if (position > 0)
				mediaPlayer.seekTo(position);
		}
	}

	private int postion;// ��¼����λ��

	private final class MyPhoneListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				if (mediaPlayer.isPlaying()) {
					postion = mediaPlayer.getCurrentPosition();
					mediaPlayer.stop();
				}
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				if (postion > 0 && path != null) {
					play(postion);
					postion = 0;
				}
				break;
			}
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//�����ؼ�ʱ����ֹͣ
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mediaPlayer.stop();
			finish();
		}
		return false;
	}
}