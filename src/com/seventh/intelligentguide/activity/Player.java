package com.seventh.intelligentguide.activity;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.seventh.intelligentguide.R;
import com.seventh.intelligentguide.beans.ScenicSpotBean;
import com.seventh.intelligentguide.dao.impl.IntelligentGuideDaoImpl;
import com.seventh.intelligentguide.receiver.ActivityReceiver;
import com.seventh.intelligentguide.util.DeciphermentFile;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Player extends Activity {
	public static TextView nameText;
	public static TextView lastnameText;
	public static TextView nextnameText;
	private String path;//文件路径
	private MediaPlayer mediaPlayer;//多媒体播放
	private boolean pause;//暂停标志
	private int postion;//记录播放位置
	private String nowfile="";//当前播放文件名
	private String lastfilen="";
	private String nextfilen="";
	
	private ScenicSpotBean ss=null;
	
	private List<String> ssslist=new ArrayList<String>();//景点列表
	
	ImageView imagev;
	Bitmap bmp;
	int hw;//图片尺寸
	int x=2545;
	int y=3226;
	String pnumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        
        ActivityReceiver br=new ActivityReceiver();
        ssslist=br.getAssetsList();
        
        if(PlaceList.file.equals("泰山")||PlaceList.file.equals("01taishan"))
        	bmp=readBitMap(this,R.drawable.ts);
		else if(PlaceList.file.equals("岱庙")||PlaceList.file.equals("02daimiao"))
			bmp=readBitMap(this,R.drawable.dm);
		else
			bmp=readBitMap(this,R.drawable.icon);
        mediaPlayer =new MediaPlayer();
        nameText = (TextView)this.findViewById(R.id.filename);//当前播放文件
        nameText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        lastnameText = (TextView)this.findViewById(R.id.lastfilename);//当前播放上一个文件
        nextnameText = (TextView)this.findViewById(R.id.nextfilename);//当前播放下一个文件
        
        nowfile=getIntent().getStringExtra("yinpin");
        
        dealfile(nowfile);//设置播放文件名
        
        imagev=(ImageView) this.findViewById(R.id.imageView_player);
        
        DisplayMetrics dm = getResources().getDisplayMetrics();
        final int screeWidth=dm.widthPixels;
        final int screeHeight=dm.heightPixels;
        hw=Math.min(screeWidth, screeHeight);//取屏幕宽高中的最小值
        drawBmp();
		play2(nowfile);//播放
        //2设置电话监听
        TelephonyManager telephonyManager=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new MyPhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
        //2
    }
    /**
     * 2
     * 监听电话
     * 接听电话时播放暂停
     * 挂掉电话时播放继续
     */
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
	@Override
	//销毁
	protected void onDestroy() {
		mediaPlayer.release();
		mediaPlayer=null;
		super.onDestroy();
	}
	/**
	 * 采用switch
	 * 对不同的按钮采取不同响应
	 */
	public void mediaplay(View v){
    	switch (v.getId()){
    	case R.id.playbutton://播放按钮
    		play2(nowfile);
    		break;
    	case R.id.pausebutton://暂停继续按钮
    		if(mediaPlayer.isPlaying()){
    			mediaPlayer.pause();//暂停
    			pause=true;
    			((Button)v).setText(R.string.continues);
    		}
    		else if (pause) {
				mediaPlayer.start();//继续播放
				pause=false;
				((Button)v).setText(R.string.pausebutton);
			}
    		break;
    	case R.id.lastbutton://上一个按钮
    		nowfile=lastfilen;
    		dealfile(nowfile);
    		play2(nowfile);
    		break;
    	case R.id.nextbutton://下一个按钮
    		nowfile=nextfilen;
    		dealfile(nowfile);
    		play2(nowfile);
    		break;
    	}
    }
	private void dealfile(String file){
		if((nowfile=file)!=""){
	        nameText.setText("当前："+nowfile);
	        int fileindex=ssslist.indexOf(nowfile);//获取当前播放文件在列表中的索引 
	        try{
		        if((fileindex-1)>=0)
		        	lastfilen=ssslist.get(fileindex-1);
		        else
		        	lastfilen="";
		        if((fileindex+1)<ssslist.size())
		        	nextfilen=ssslist.get(fileindex+1);
		        else
		        	nextfilen="";
	        }catch(Exception e){
	        }
	        if(lastfilen=="")
	        	lastnameText.setText("");
	        else
	        	lastnameText.setText("上一个："+lastfilen);
	        if(nextfilen=="")
	        	nextnameText.setText("");
	        else
	        	nextnameText.setText("下一个："+nextfilen);
		}
	}
	
	private void drawBmp(){
		Bitmap bitmap=bmp;
		if(ss!=null){
			x=ss.getX();
			y=ss.getY();
			if((x-hw)<(hw/2))
				x=hw/2-5;
			if(x+hw>bmp.getWidth())
				x=bmp.getWidth()-hw/2-5;
			if((y-hw)<(hw/2))
				y=hw/2-5;
			if(y+hw>bmp.getHeight())
				y=bmp.getHeight()-hw/2-5;
			if(x-(hw/2)<0)
				x=hw;
			if(y-(hw/2)<0)
				y=hw;
			if(x-(hw/2)+hw<=bmp.getWidth()&&y-(hw/2)+hw<=bmp.getHeight())
				bitmap=Bitmap.createBitmap(bitmap, x-(hw/2), y-(hw/2), hw, hw);
		}
        imagev.setImageBitmap(bitmap);
	}
	//获取图片
	public static Bitmap readBitMap(Context context, int resId){  
		BitmapFactory.Options opt = new BitmapFactory.Options();  
		opt.inPreferredConfig = Bitmap.Config.RGB_565;   
		opt.inPurgeable = true;  
		opt.inInputShareable = true;  
		//获取资源图片  
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is,null,opt);
	}

	/**
	 * 播放方法
	 */
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
		IntelligentGuideDaoImpl igdi=new IntelligentGuideDaoImpl(getApplicationContext());
		ss=igdi.searchSpotsBySpots_name(nowfile);
		if(ss!=null){
			String filname=ss.getFile_name();
			drawBmp();
			File src = new File(Environment.getExternalStorageDirectory()+filname);
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
		else {
			Toast.makeText(getApplicationContext(), R.string.placereturn, 1).show();
		}
	}
	
	// 异或解密
	public static void xorEn(File src, File dest) throws Exception {
		// 文件不存在或为文件夹就不判断了
		/*String kk=R.string.keykey+"";
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
		fis.close();*/
		int blockCount=2;
	    //依次分块进行文件COPY
	    for(int i=0;i<blockCount;i++)
	    {
	        //实例化文件复制对象
	        DeciphermentFile dealFile=new DeciphermentFile(src.getPath(),dest.getPath(),blockCount,i);
	        //实例化线程
	        Thread thread=new Thread(dealFile);
	        //开始线程
	        thread.start();
	        try
	        {
	            //加入线程
	            thread.join();
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
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
}
