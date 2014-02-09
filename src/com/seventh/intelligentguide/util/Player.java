package com.seventh.intelligentguide.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import com.seventh.intelligentguide.R;
import com.seventh.intelligentguide.dao.impl.IntelligentGuideDaoImpl;
import com.seventh.intelligentguide.tabhost.Layout1;
import com.seventh.intelligentguide.tabhost.MyTabHostFive;
import com.seventh.intelligentguide.tabhost.PlaceList;
import com.seventh.intelligentguide.vo.ScenicSpot;

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
import android.util.Log;
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
	private String path;//�ļ�·��
	private MediaPlayer mediaPlayer;//��ý�岥��
	private boolean pause;//��ͣ��־
	private int postion;//��¼����λ��
	private String nowfile="";//��ǰ�����ļ���
	private String lastfilen="";
	private String nextfilen="";
	ArrayList<ScenicSpot> plist;//�����б�
	
	private ScenicSpot ss=null;
	
	ImageView imagev;
	Bitmap bmp;
	int hw;//ͼƬ�ߴ�
	int x=2545;
	int y=3226;
	String pnumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        
        IntelligentGuideDaoImpl igdi=new IntelligentGuideDaoImpl(getApplicationContext());
        plist=(ArrayList<ScenicSpot>) igdi.searchSpotsList(PlaceList.file);
        
        
        if(PlaceList.file.equals("̩ɽ")||PlaceList.file.equals("01taishan"))
        	bmp=readBitMap(this,R.drawable.ts);
		else if(PlaceList.file.equals("���")||PlaceList.file.equals("02daimiao"))
			bmp=readBitMap(this,R.drawable.dm);
		else
			bmp=readBitMap(this,R.drawable.icon);
        mediaPlayer =new MediaPlayer();
        nameText = (TextView)this.findViewById(R.id.filename);//��ǰ�����ļ�
        nameText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        lastnameText = (TextView)this.findViewById(R.id.lastfilename);//��ǰ������һ���ļ�
        nextnameText = (TextView)this.findViewById(R.id.nextfilename);//��ǰ������һ���ļ�
        nowfile=MyTabHostFive.strText;
        
        Log.v("nowfile:", nowfile);
        
        dealfile(nowfile);//���ò����ļ���
        
        imagev=(ImageView) this.findViewById(R.id.imageView_player);
        
        DisplayMetrics dm = getResources().getDisplayMetrics();
        final int screeWidth=dm.widthPixels;
        final int screeHeight=dm.heightPixels;
        hw=Math.min(screeWidth, screeHeight);//ȡ��Ļ����е���Сֵ
        drawbmp();
		play2(nowfile);//����
        //2���õ绰����
        TelephonyManager telephonyManager=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new MyPhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
        //2
    }
    /**
     * 2
     * �����绰
     * �����绰ʱ������ͣ
     * �ҵ��绰ʱ���ż���
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
	//����
	protected void onDestroy() {
		mediaPlayer.release();
		mediaPlayer=null;
		super.onDestroy();
	}
	/**
	 * ����switch
	 * �Բ�ͬ�İ�ť��ȡ��ͬ��Ӧ
	 */
	public void mediaplay(View v){
    	switch (v.getId()){
    	case R.id.playbutton://���Ű�ť
    		play2(nowfile);
    		break;
    	case R.id.pausebutton://��ͣ������ť
    		if(mediaPlayer.isPlaying()){
    			mediaPlayer.pause();//��ͣ
    			pause=true;
    			((Button)v).setText(R.string.continues);
    		}
    		else if (pause) {
				mediaPlayer.start();//��������
				pause=false;
				((Button)v).setText(R.string.pausebutton);
			}
    		break;
    	case R.id.lastbutton://��һ����ť
    		nowfile=lastfilen;
    		dealfile(nowfile);
    		drawbmp();
    		play2(nowfile);
    		break;
    	case R.id.nextbutton://��һ����ť
    		nowfile=nextfilen;
    		dealfile(nowfile);
    		drawbmp();
    		play2(nowfile);
    		break;
    	}
    }
	private void dealfile(String file){
		if((nowfile=file)!=""){
	        nameText.setText("��ǰ��"+nowfile);
	        int fileindex=Layout1.assetsList.indexOf(nowfile);//��ȡ��ǰ�����ļ����б��е����� 
	        try{
		        if((fileindex-1)>=0)
		        	lastfilen=Layout1.assetsList.get(fileindex-1);
		        else
		        	lastfilen="";
		        if((fileindex+1)<Layout1.assetsList.size())
		        	nextfilen=Layout1.assetsList.get(fileindex+1);
		        else
		        	nextfilen="";
	        }catch(Exception e){
	        }
	        if(lastfilen=="")
	        	lastnameText.setText("");
	        else
	        	lastnameText.setText("��һ����"+lastfilen);
	        if(nextfilen=="")
	        	nextnameText.setText("");
	        else
	        	nextnameText.setText("��һ����"+nextfilen);
	        MyTabHostFive.strText=nowfile;
		}
	}
	
	private void drawbmp(){
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
			bitmap=Bitmap.createBitmap(bitmap, x-(hw/2), y-(hw/2), hw, hw);
		}
        imagev.setImageBitmap(bitmap);
	}
	//��ȡͼƬ
	public static Bitmap readBitMap(Context context, int resId){  
		BitmapFactory.Options opt = new BitmapFactory.Options();  
		opt.inPreferredConfig = Bitmap.Config.RGB_565;   
		opt.inPurgeable = true;  
		opt.inInputShareable = true;  
		//��ȡ��ԴͼƬ  
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is,null,opt);
	}

	/**
	 * ���ŷ���
	 */
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
		////////////////////////////////////////////2014.2.8
		IntelligentGuideDaoImpl igdi=new IntelligentGuideDaoImpl(getApplicationContext());
		ss=igdi.searchSpotsBySpots_name(nowfile);
		String filname=ss.getFile_name();
		
		Log.v("��ǰ����", nowfile);
		
		drawbmp();
		File src = new File(Environment.getExternalStorageDirectory()+filname);
		File dest = new File(Environment.getExternalStorageDirectory()+"/dao/play.mp3");
		
		Log.v("��Ƶ�ļ�", src.getName());
		
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
}
