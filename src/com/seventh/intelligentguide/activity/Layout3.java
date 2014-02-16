package com.seventh.intelligentguide.activity;

import java.io.InputStream;

import com.seventh.intelligentguide.R;
import com.seventh.intelligentguide.dao.impl.IntelligentGuideDaoImpl;
import com.seventh.intelligentguide.util.Player;

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
 * 图片浏览、缩放、拖动、自动居中
 */
public class Layout3 extends Activity implements OnTouchListener {

	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private DisplayMetrics dm;
	private ImageView imgView;
	private Bitmap map;

	private float minScaleR;// 最小缩放比例
	private float wmin, hmin;
	private static final float MAX_SCALE = 4f;// 最大缩放比例

	private static final int NONE = 0;// 初始状态
	private static final int DRAG = 1;// 拖动
	private static final int ZOOM = 2;// 缩放
	private int mode = NONE;
	
	private String scenicName=null;//景区名称

	PointF prev = new PointF();
	PointF mid = new PointF();

	float dist = 1f;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout3);
		
		scenicName=getIntent().getStringExtra("scenicname");
		
		imgView = (ImageView) findViewById(R.id.imag);// 获取控件
		if(scenicName.equals("泰山")||scenicName.equals("01taishan"))
			map=readBitMap(this,R.drawable.ts);
		else if(scenicName.equals("岱庙")||scenicName.equals("02daimiao"))
			map=readBitMap(this,R.drawable.dm);
		else
			map=readBitMap(this,R.drawable.icon);
		imgView.setImageBitmap(map);// 填充控件
		imgView.setOnTouchListener(this);// 设置触屏监听
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);// 获取分辨率
		minZoom();
		center();
		imgView.setImageMatrix(matrix);
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
	 * 触屏监听
	 */
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// 第一个手指按下
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			prev.set(event.getX(), event.getY());
			mode = DRAG;
			break;
		// 手指拖动
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - prev.x, event.getY()
						- prev.y);
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float tScale = newDist / dist;// 倍数
					matrix.postScale(tScale, tScale, mid.x, mid.y);
				}
			}
			break;
		// 最后一个手指离开
		case MotionEvent.ACTION_UP:
			mode = NONE;
			checkPoint(event);
			break;
		// 手指离开屏幕，但还有手指按下屏幕
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		// 已有一个手指按下，再有一个手指按下
		case MotionEvent.ACTION_POINTER_DOWN:
			mode = ZOOM;
			dist = spacing(event);
			// 如果连续两点距离大于10，则判定为多点模式
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
	 * 检测点击位置
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
			Toast.makeText(getApplicationContext(), R.string.waiting, 0).show();
			
			Bundle bundle=new Bundle();
	        bundle.putCharSequence("yinpin", SpotsName);
	        in.putExtras(bundle);
			
			startActivity(in);
		}
	}
	/**
	 * 获取当前点的景点名
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
		SpotsName=igdi.getSpotsNameByXandY(scenicName, coords[0], coords[1]);
		return SpotsName;
	}
	/**
	 * 限制最大最小缩放比例，自动居中
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
	 * 最小缩放比例，最大为100%
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
	 * 横向、纵向居中
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
			// 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
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
	 * 两点的距离
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/**
	 * 两点的中点
	 */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
}