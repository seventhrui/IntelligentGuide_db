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
        
        //判断GPS是否正常启动
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "请开启GPS...", Toast.LENGTH_SHORT).show();
            //返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);   
            startActivityForResult(intent,0); 
            return;
        }
        //为获取地理位置信息时设置查询条件
        String bestProvider = lm.getBestProvider(getCriteria(), true);
        //获取位置信息
        //如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER
        Location location= lm.getLastKnownLocation(bestProvider);    
        updateView(location);
        //监听状态
        lm.addGpsStatusListener(listener);
        //绑定监听，有4个参数    
        //参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
        //参数2，位置信息更新周期，单位毫秒    
        //参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息    
        //参数4，监听    
        //备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新   
        // 1秒更新一次，或最小位移变化超过1米更新一次；
        //注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
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

	//读取景点信息列表
  	private ArrayList<ScenicSpot> mlist() {
  		String s = "";
  		ArrayList<ScenicSpot> aList = new ArrayList<ScenicSpot>();// 景点信息列表
  		String[] temp = new String[3];
  		InputStream in=null;
  		try {
  			if(PlaceList.file.equals("01泰山")||PlaceList.file.equals("01taishan"))//电脑测试
  			//if(zhongmeng.file.equals("泰山"))//手机测试
  				in = getResources().getAssets().open("taishan.sce");
  			else if(PlaceList.file.equals("02岱庙")||PlaceList.file.equals("02daimiao"))//电脑测试
  			//else if(zhongmeng.file.equals("岱庙"))//手机测试
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
    
    //位置监听
    private LocationListener locationListener=new LocationListener() {
        /**
         * 位置信息变化时触发
         */
        public void onLocationChanged(Location location) {
            updateView(location);
            //Log.i(TAG, "时间："+location.getTime()); 
            //Log.i(TAG, "经度："+location.getLongitude()); 
            //Log.i(TAG, "纬度："+location.getLatitude()); 
            //Log.i(TAG, "海拔："+location.getAltitude()); 
            //Log.i(TAG, "精度："+location.getAccuracy());
            //Log.i(TAG, "地点："+getAddressbyGeoPoint(location));
        }
        
        // GPS状态变化时触发
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
            //GPS状态为可见时
            case LocationProvider.AVAILABLE:
                Log.i(TAG, "当前GPS状态为可见状态");
                break;
            //GPS状态为服务区外时
            case LocationProvider.OUT_OF_SERVICE:
                Log.i(TAG, "当前GPS状态为服务区外状态");
                break;
            //GPS状态为暂停服务时
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.i(TAG, "当前GPS状态为暂停服务状态");
                break;
            }
        }
        // GPS开启时触发
        public void onProviderEnabled(String provider) {
            Location location=lm.getLastKnownLocation(provider);
            updateView(location);
        }
        // GPS禁用时触发
        public void onProviderDisabled(String provider) {
            updateView(null);
        }
    };
    
    //状态监听
    GpsStatus.Listener listener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
        	switch (event) {
            //第一次定位
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                Log.i(TAG, "第一次定位");
                break;
            //卫星状态改变
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                Log.i(TAG, "卫星状态改变");
                //获取当前状态
                GpsStatus gpsStatus=lm.getGpsStatus(null);
                //获取卫星颗数的默认最大值
                int maxSatellites = gpsStatus.getMaxSatellites();
                //创建一个迭代器保存所有卫星 
                Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                int count = 0;     
                while (iters.hasNext() && count <= maxSatellites) {     
                    GpsSatellite s = iters.next();     
                    count++;
                }   
                //System.out.println("搜索到："+count+"颗卫星");
                Log.i(TAG,"搜索到："+count+"颗卫星");
                sum=count;
                break;
            //定位启动
            case GpsStatus.GPS_EVENT_STARTED:
                Log.i(TAG, "定位启动");
                break;
            //定位结束
            case GpsStatus.GPS_EVENT_STOPPED:
                Log.i(TAG, "定位结束");
                break;
            }
        };
    };
    
    /**
     * 实时更新文本内容
     * 
     * @param location
     */
    private void updateView(Location location){
        if(location!=null){
            Txtview1.setText("经度:"+String.valueOf(location.getLongitude())+","+"纬度:"+String.valueOf(location.getLatitude()));
            Txtview2.setText("搜索到:"+sum+"颗卫星");
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
            		TxtViewScenicName.setText("当前:"+nowVoiceName);
	            	//播放
	            	play2(nowVoiceName);
            	}
            }
            else if(mediaPlayer.isPlaying())
            	TxtViewScenicName.setText("当前:"+nowVoiceName);
            else
            	TxtViewScenicName.setText("此处未探测到景点");
        }else{
            //清空Txtview1对象
        	Txtview1.setText("");
        	Txtview2.setText("");
        	TxtViewScenicName.setText("正在搜索景点......");
        }
    }
    /**
     * 返回查询条件
     * @return
     */
    private Criteria getCriteria(){
        Criteria criteria=new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细 
        criteria.setAccuracy(Criteria.ACCURACY_FINE);    
        //设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费  
        criteria.setCostAllowed(true);
        //设置是否需要方位信息
        criteria.setBearingRequired(false);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求  
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        //Log.i(TAG, "定位方式："+criteria.toString());
        return criteria;
    }
    /*
    private List<Address> getAddressbyGeoPoint(Location location) {
    	List<Address> result = null;
    	// 先将Location转换为GeoPoint
    	// GeoPoint gp=getGeoByLocation(location);  
    	try {
    		if (location != null) {
    			// 获取Geocoder，通过Geocoder就可以拿到地址信息
    			Geocoder gc = new Geocoder(this, Locale.getDefault());
    			result= gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return result;
	}
    */
    private String path=null;//文件路径
	public static MediaPlayer mediaPlayer=null;//多媒体播放
    private void play(int position) {
		try {
			mediaPlayer.reset();//把各项参数复位
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();//进行数据缓冲
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
			play(0);//从头开始播放
			int i = 0;
			for(i = 0; !dest.delete() && i <= 20; i++);
		}
		else{
			path=null;
			Toast.makeText(getApplicationContext(), R.string.filenoexist, 1).show();
		}
	}
	// 异或加密解密
	public static void xorEn(File src, File dest) throws Exception {
		// 文件不存在或为文件夹就不判断了
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
			mediaPlayer.start();//开始播放
			if(position>0)
				mediaPlayer.seekTo(position);
		}
	}
	private int postion;//记录播放位置
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