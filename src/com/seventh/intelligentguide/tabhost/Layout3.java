package com.seventh.intelligentguide.tabhost;

import java.io.InputStream;
import java.util.ArrayList;

import com.seventh.intelligentguide.R;
import com.seventh.intelligentguide.dao.impl.IntelligentGuideDaoImpl;
import com.seventh.intelligentguide.util.Player;
import com.seventh.intelligentguide.vo.ScenicSpot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * ͼƬ��������š��϶����Զ�����
 */
public class Layout3 extends Activity implements OnTouchListener {

	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();
	DisplayMetrics dm;
	ImageView imgView;
	Bitmap map;
	
	ArrayList<ScenicSpot> plist;//������Ϣ�б�

	float minScaleR;// ��С���ű���
	float wmin, hmin;
	static final float MAX_SCALE = 4f;// ������ű���

	static final int NONE = 0;// ��ʼ״̬
	static final int DRAG = 1;// �϶�
	static final int ZOOM = 2;// ����
	
	static final int SPACE = 15; // ��ȷ��
	int mode = NONE;

	PointF prev = new PointF();
	PointF mid = new PointF();

	float dist = 1f;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout3);
		
		IntelligentGuideDaoImpl igdi=new IntelligentGuideDaoImpl(getApplicationContext());
		plist=(ArrayList<ScenicSpot>) igdi.searchSpotsList(PlaceList.file);
		
		imgView = (ImageView) findViewById(R.id.imag);// ��ȡ�ؼ�
		if(PlaceList.file.equals("̩ɽ")||PlaceList.file.equals("01taishan"))
			map=readBitMap(this,R.drawable.ts);
		else if(PlaceList.file.equals("���")||PlaceList.file.equals("02daimiao"))
			map=readBitMap(this,R.drawable.dm);
		else
			map=readBitMap(this,R.drawable.icon);
		imgView.setImageBitmap(map);// ���ؼ�
		imgView.setOnTouchListener(this);// ���ô�������
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);// ��ȡ�ֱ���
		minZoom();
		center();
		imgView.setImageMatrix(matrix);
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
	 * ��������
	 */
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// ��һ����ָ����
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			prev.set(event.getX(), event.getY());
			mode = DRAG;
			break;
		// ��ָ�϶�
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - prev.x, event.getY()
						- prev.y);
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float tScale = newDist / dist;// ����
					matrix.postScale(tScale, tScale, mid.x, mid.y);
				}
			}
			break;
		// ���һ����ָ�뿪
		case MotionEvent.ACTION_UP:
			mode = NONE;
			checkPoint(event);
			break;
		// ��ָ�뿪��Ļ����������ָ������Ļ
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		// ����һ����ָ���£�����һ����ָ����
		case MotionEvent.ACTION_POINTER_DOWN:
			mode = ZOOM;
			dist = spacing(event);
			// �����������������10�����ж�Ϊ���ģʽ
			if (spacing(event) > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
			}
			break;
		}
		imgView.setImageMatrix(matrix);
		CheckView();
		return true;
	}

	/**
	 * �����λ��
	 * @param event
	 */
	private void checkPoint(MotionEvent event){
		String temp=null;
		
		Intent mainIntent = new Intent("android.intent.action.SQUARE", null);
		mainIntent.addCategory("android.intent.category.SQUARE");
		Intent in = new Intent(Layout3.this, Player.class);
		temp=getScenicName(event);
		if(temp!=null){
			String SpotsName=getScenicName(event);
			MyTabHostFive.strText = SpotsName;
			Toast.makeText(getApplicationContext(), R.string.waiting, 1).show();
			startActivity(in);
		}
	}
	/**
	 * ��ȡ��ǰ��ľ�����
	 * @param event
	 * @return
	 */
	private String getScenicName(MotionEvent event){
		String SpotsName=null;
		float[] coords = new float[] { event.getX(), event.getY() };
		Matrix matrix2 = new Matrix();
		this.imgView.getImageMatrix().invert(matrix2);
		matrix2.postTranslate(this.imgView.getScrollX(),
				this.imgView.getScrollY());
		matrix2.mapPoints(coords);
		
		IntelligentGuideDaoImpl igdi=new IntelligentGuideDaoImpl(getApplicationContext());
		SpotsName=igdi.getSpotsNameByXandY(PlaceList.file, coords[0], coords[1]);
		return SpotsName;
	}
	/**
	 * ���������С���ű������Զ�����
	 */
	private void CheckView() {
		float p[] = new float[9];
		matrix.getValues(p);
		if (mode == ZOOM) {
			if (p[0] < minScaleR) {
				matrix.setScale(minScaleR, minScaleR);
			}
			if (p[0] > MAX_SCALE) {
				matrix.set(savedMatrix);
			}
		}
		center();
	}

	/**
	 * ��С���ű��������Ϊ100%
	 */
	private void minZoom() {
		wmin = (float) dm.widthPixels / (float) map.getWidth();
		hmin = (float) dm.heightPixels / (float) map.getHeight();
		minScaleR = Math.min(wmin, hmin);
		if (minScaleR < 1.0) {
			matrix.postScale(minScaleR, minScaleR);
		}
		// matrix.postScale(wmin, hmin);
	}

	private void center() {
		center(true, true);
	}

	/**
	 * �����������
	 */
	protected void center(boolean horizontal, boolean vertical) {
		Matrix m = new Matrix();
		m.set(matrix);
		RectF rect = new RectF(0, 0, map.getWidth(), map.getHeight());
		m.mapRect(rect);

		float height = rect.height();
		float width = rect.width();

		float deltaX = 0, deltaY = 0;

		if (vertical) {
			// ͼƬС����Ļ��С���������ʾ��������Ļ���Ϸ������������ƣ��·�������������
			int screenHeight = dm.heightPixels;
			if (height < screenHeight) {
				deltaY = (screenHeight - height) / 2 - rect.top;
			} else if (rect.top > 0) {
				deltaY = -rect.top;
			} else if (rect.bottom < screenHeight) {
				deltaY = imgView.getHeight() - rect.bottom;
			}
		}

		if (horizontal) {
			int screenWidth = dm.widthPixels;
			if (width < screenWidth) {
				deltaX = (screenWidth - width) / 2 - rect.left;
			} else if (rect.left > 0) {
				deltaX = -rect.left;
			} else if (rect.right < screenWidth) {
				deltaX = screenWidth - rect.right;
			}
		}
		matrix.postTranslate(deltaX, deltaY);
	}

	/**
	 * ����ľ���
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/**
	 * ������е�
	 */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
}