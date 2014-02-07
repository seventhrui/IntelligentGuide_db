package com.seventh.intelligentguide.tabhost;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.seventh.intelligentguide.R;
import com.seventh.intelligentguide.Index;
import com.seventh.intelligentguide.vo.ScenicSpot;

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
    private TextView Txtview1;
    private TextView Txtview2;
    private TextView TxtViewScenicName;
    public static String nowVoiceName="";
    int sum=0;
    private LocationManager lm;
    private static final String TAG="GpsActivity";

	List<ScenicSpot> list=new ArrayList<ScenicSpot>();
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		lm.removeUpdates(locationListener);
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout5);
        
        mediaPlayer =new MediaPlayer();
        list=mlist();
        
        Txtview1=(TextView)findViewById(R.id.jingdu);
        Txtview2=(TextView)findViewById(R.id.weidu);
        TxtViewScenicName=(TextView) findViewById(R.id.scenicName);
        
        lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        //�ж�GPS�Ƿ���������
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "�뿪��GPS...", Toast.LENGTH_SHORT).show();
            //���ؿ���GPS�������ý���
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);   
            startActivityForResult(intent,0); 
            return;
        }
        //Ϊ��ȡ����λ����Ϣʱ���ò�ѯ����
        String bestProvider = lm.getBestProvider(getCriteria(), true);
        //��ȡλ����Ϣ
        //��������ò�ѯҪ��getLastKnownLocation�������˵Ĳ���ΪLocationManager.GPS_PROVIDER
        Location location= lm.getLastKnownLocation(bestProvider);    
        updateView(location);
        //����״̬
        lm.addGpsStatusListener(listener);
        //�󶨼�������4������    
        //����1���豸����GPS_PROVIDER��NETWORK_PROVIDER����
        //����2��λ����Ϣ�������ڣ���λ����    
        //����3��λ�ñ仯��С���룺��λ�þ���仯������ֵʱ��������λ����Ϣ    
        //����4������    
        //��ע������2��3���������3��Ϊ0�����Բ���3Ϊ׼������3Ϊ0����ͨ��ʱ������ʱ���£�����Ϊ0������ʱˢ��   
        // 1�����һ�Σ�����Сλ�Ʊ仯����1�׸���һ�Σ�
        //ע�⣺�˴�����׼ȷ�ȷǳ��ͣ��Ƽ���service��������һ��Thread����run��sleep(10000);Ȼ��ִ��handler.sendMessage(),����λ��
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        TelephonyManager telephonyManager=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new MyPhoneListener(), PhoneStateListener.LISTEN_CALL_STATE); 
    }
    
    /*
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(mediaPlayer.isPlaying())
			mediaPlayer.stop();
		else
			mediaPlayer.start();
		return super.onTouchEvent(event);
	}
	*/

	//��ȡ������Ϣ�б�
  	private ArrayList<ScenicSpot> mlist() {
  		String s = "";
  		ArrayList<ScenicSpot> aList = new ArrayList<ScenicSpot>();// ������Ϣ�б�
  		String[] temp = new String[3];
  		InputStream in=null;
  		try {
  			if(PlaceList.file.equals("01̩ɽ")||PlaceList.file.equals("01taishan"))//���Բ���
  			//if(zhongmeng.file.equals("̩ɽ"))//�ֻ�����
  				in = getResources().getAssets().open("taishan.sce");
  			else if(PlaceList.file.equals("02���")||PlaceList.file.equals("02daimiao"))//���Բ���
  			//else if(zhongmeng.file.equals("���"))//�ֻ�����
  				in = getResources().getAssets().open("daimiao.sce");
  			BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
  			while ((s = bfr.readLine()) != null) {
  				temp = s.split(",");
  				aList.add(new ScenicSpot(temp[0], temp[1], temp[2],temp[3],temp[4]));
  			}
  			bfr.close();
  			in.close();
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  		return aList;
  	}
    
    //λ�ü���
    private LocationListener locationListener=new LocationListener() {
        /**
         * λ����Ϣ�仯ʱ����
         */
        public void onLocationChanged(Location location) {
            updateView(location);
            //Log.i(TAG, "ʱ�䣺"+location.getTime()); 
            //Log.i(TAG, "���ȣ�"+location.getLongitude()); 
            //Log.i(TAG, "γ�ȣ�"+location.getLatitude()); 
            //Log.i(TAG, "���Σ�"+location.getAltitude()); 
            //Log.i(TAG, "���ȣ�"+location.getAccuracy());
            //Log.i(TAG, "�ص㣺"+getAddressbyGeoPoint(location));
        }
        
        // GPS״̬�仯ʱ����
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
            //GPS״̬Ϊ�ɼ�ʱ
            case LocationProvider.AVAILABLE:
                Log.i(TAG, "��ǰGPS״̬Ϊ�ɼ�״̬");
                break;
            //GPS״̬Ϊ��������ʱ
            case LocationProvider.OUT_OF_SERVICE:
                Log.i(TAG, "��ǰGPS״̬Ϊ��������״̬");
                break;
            //GPS״̬Ϊ��ͣ����ʱ
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.i(TAG, "��ǰGPS״̬Ϊ��ͣ����״̬");
                break;
            }
        }
        // GPS����ʱ����
        public void onProviderEnabled(String provider) {
            Location location=lm.getLastKnownLocation(provider);
            updateView(location);
        }
        // GPS����ʱ����
        public void onProviderDisabled(String provider) {
            updateView(null);
        }
    };
    
    //״̬����
    GpsStatus.Listener listener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
        	switch (event) {
            //��һ�ζ�λ
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                Log.i(TAG, "��һ�ζ�λ");
                break;
            //����״̬�ı�
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                Log.i(TAG, "����״̬�ı�");
                //��ȡ��ǰ״̬
                GpsStatus gpsStatus=lm.getGpsStatus(null);
                //��ȡ���ǿ�����Ĭ�����ֵ
                int maxSatellites = gpsStatus.getMaxSatellites();
                //����һ�������������������� 
                Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                int count = 0;     
                while (iters.hasNext() && count <= maxSatellites) {     
                    GpsSatellite s = iters.next();     
                    count++;
                }   
                //System.out.println("��������"+count+"������");
                Log.i(TAG,"��������"+count+"������");
                sum=count;
                break;
            //��λ����
            case GpsStatus.GPS_EVENT_STARTED:
                Log.i(TAG, "��λ����");
                break;
            //��λ����
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
    private void updateView(Location location){
        if(location!=null){
            Txtview1.setText("����:"+String.valueOf(location.getLongitude())+","+"γ��:"+String.valueOf(location.getLatitude()));
            Txtview2.setText("������:"+sum+"������");
            String Scenicnum=null;
            String ScenicName=null;
            for (ScenicSpot m : list) {
    			if(location.getLongitude()+0.0002>m.getLongitude()&&location.getLongitude()-0.0002<m.getLongitude()&&location.getLatitude()+0.0002>m.getLatitude()&&location.getLatitude()-0.0002<m.getLatitude()){
    				Scenicnum=m.getN();
    			}
    		}
            for(String s:Layout1.assetsList){
            	if(s.startsWith(Scenicnum+"."))
            		ScenicName=s;
            }
            if(ScenicName!=null){
            	if(!nowVoiceName.equals(ScenicName)){
            		nowVoiceName=ScenicName;
            		TxtViewScenicName.setText("��ǰ:"+nowVoiceName);
	            	//����
	            	play2(nowVoiceName);
            	}
            }
            else if(mediaPlayer.isPlaying())
            	TxtViewScenicName.setText("��ǰ:"+nowVoiceName);
            else
            	TxtViewScenicName.setText("�˴�δ̽�⵽����");
        }else{
            //���Txtview1����
        	Txtview1.setText("");
        	Txtview2.setText("");
        	TxtViewScenicName.setText("������������......");
        }
    }
    /**
     * ���ز�ѯ����
     * @return
     */
    private Criteria getCriteria(){
        Criteria criteria=new Criteria();
        //���ö�λ��ȷ�� Criteria.ACCURACY_COARSE�Ƚϴ��ԣ�Criteria.ACCURACY_FINE��ȽϾ�ϸ 
        criteria.setAccuracy(Criteria.ACCURACY_FINE);    
        //�����Ƿ�Ҫ���ٶ�
        criteria.setSpeedRequired(false);
        // �����Ƿ�������Ӫ���շ�  
        criteria.setCostAllowed(true);
        //�����Ƿ���Ҫ��λ��Ϣ
        criteria.setBearingRequired(false);
        //�����Ƿ���Ҫ������Ϣ
        criteria.setAltitudeRequired(false);
        // ���öԵ�Դ������  
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        //Log.i(TAG, "��λ��ʽ��"+criteria.toString());
        return criteria;
    }
    /*
    private List<Address> getAddressbyGeoPoint(Location location) {
    	List<Address> result = null;
    	// �Ƚ�Locationת��ΪGeoPoint
    	// GeoPoint gp=getGeoByLocation(location);  
    	try {
    		if (location != null) {
    			// ��ȡGeocoder��ͨ��Geocoder�Ϳ����õ���ַ��Ϣ
    			Geocoder gc = new Geocoder(this, Locale.getDefault());
    			result= gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return result;
	}
    */
    private String path=null;//�ļ�·��
	public static MediaPlayer mediaPlayer=null;//��ý�岥��
    private void play(int position) {
		try {
			mediaPlayer.reset();//�Ѹ��������λ
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();//�������ݻ���
			mediaPlayer.setOnPreparedListener(new PrepareListener(position));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void play2(String file){
		String filename=file+".grv";
		File src = new File(Environment.getExternalStorageDirectory()+"/dao/"+Index.place_file+"/"+PlaceList.file+"/"+filename);
		File dest = new File(Environment.getExternalStorageDirectory()+"/dao/play.mp3");
		try {
			xorEn(src, dest);
		} catch (Exception e) {
		}
		File audio=dest;
		if(audio.exists()){
			path=audio.getPath();
			play(0);//��ͷ��ʼ����
			int i = 0;
			for(i = 0; !dest.delete() && i <= 20; i++);
		}
		else{
			path=null;
			Toast.makeText(getApplicationContext(), R.string.filenoexist, 1).show();
		}
	}
	// �����ܽ���
	public static void xorEn(File src, File dest) throws Exception {
		// �ļ������ڻ�Ϊ�ļ��оͲ��ж���
		String kk=R.string.keykey+"";
        String k=kk.charAt(0)+"";
        final int a=Integer.parseInt(k)*9;
		class test {
			int[] i = {a,a+1,a-1,a+2};
		}
		test t = new test();
		FileInputStream fis = new FileInputStream(src);
		FileOutputStream fos = new FileOutputStream(dest);
		byte[] bs = new byte[1024];
		int len = 0;
		while ((len = fis.read(bs)) != -1) {
			for (int i = 0; i < len; i++) {
				bs[i] ^= t.i[i%4];
			}
			fos.write(bs, 0, len);
		}
		fos.close();
		fis.close();
	}
	private final class PrepareListener implements OnPreparedListener {
		private int position;
		public PrepareListener(int position) {
			this.position=position;
		}
		public void onPrepared(MediaPlayer mp){
			mediaPlayer.start();//��ʼ����
			if(position>0)
				mediaPlayer.seekTo(position);
		}
	}
	private int postion;//��¼����λ��
	private final class MyPhoneListener extends PhoneStateListener{
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				if(mediaPlayer.isPlaying()){
					postion=mediaPlayer.getCurrentPosition();
					mediaPlayer.stop();
				}
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				if(postion>0&&path!=null){
					play(postion);
					postion=0;
				}
				break;
			}
		}
    }
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mediaPlayer.stop();
		}
		return false;
	}
}