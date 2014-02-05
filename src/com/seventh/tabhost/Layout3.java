package com.seventh.tabhost;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.seventh.R;
import com.seventh.main.daoyouqi;
import com.seventh.util.Player;
import com.seventh.vo.Meohe;

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

	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();
	DisplayMetrics dm;
	ImageView imgView;
	Bitmap map;
	
	ArrayList<Meohe> plist;//景点信息列表

	float minScaleR;// 最小缩放比例
	float wmin, hmin;
	static final float MAX_SCALE = 4f;// 最大缩放比例

	static final int NONE = 0;// 初始状态
	static final int DRAG = 1;// 拖动
	static final int ZOOM = 2;// 缩放
	
	static final int SPACE = 15; // 精确度
	int mode = NONE;

	private String keyString = "";// 景点信息

	PointF prev = new PointF();
	PointF mid = new PointF();

	float dist = 1f;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout3);
		plist=list();
		imgView = (ImageView) findViewById(R.id.imag);// 获取控件
		if(daoyouqi.file.equals("01泰山")||daoyouqi.file.equals("01taishan"))
		//if(zhongmeng.file.equals("taishan"))
			map=readBitMap(this,R.drawable.ts);
		else if(daoyouqi.file.equals("02岱庙")||daoyouqi.file.equals("02daimiao"))
		//else if(zhongmeng.file.equals("daimiao"))
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
	private void checkPoint(MotionEvent event) {
		float[] coords = new float[] { event.getX(), event.getY() };
		Matrix matrix2 = new Matrix();
		this.imgView.getImageMatrix().invert(matrix2);
		matrix2.postTranslate(this.imgView.getScrollX(),
				this.imgView.getScrollY());
		matrix2.mapPoints(coords);

		for (Meohe m : plist) {
			if (m.getX() + SPACE >= coords[0] && m.getX() - SPACE <= coords[0]
					&& m.getY() + SPACE >= coords[1] && m.getY() - SPACE <= coords[1]) {
				Toast.makeText(getApplicationContext(), R.string.waiting, 1)
						.show();
				Intent mainIntent = new Intent("android.intent.action.SQUARE", null);
				mainIntent.addCategory("android.intent.category.SQUARE");
				Intent in = new Intent(Layout3.this, Player.class);
				for (Object o : Layout1.assetsList) {
					if (o.toString().startsWith(m.getN() + ".")) {
						keyString = o.toString();
					}
				}
				MyTabHostFive.strText = keyString;
				startActivity(in);
				keyString = "";
			}
		}
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
	//读取景点信息列表
	private ArrayList<Meohe> list() {
		String s = "";
		ArrayList<Meohe> aList = new ArrayList<Meohe>();// 景点信息列表
		String[] temp = new String[5];
		InputStream in=null;
		try {
			if(daoyouqi.file.equals("01泰山")||daoyouqi.file.equals("01taishan"))
			//if(zhongmeng.file.equals("taishan"))
				in = getResources().getAssets().open("taishan.sce");
			else if(daoyouqi.file.equals("02岱庙")||daoyouqi.file.equals("02daimiao"))
			//else if(zhongmeng.file.equals("daimiao"))
				in = getResources().getAssets().open("daimiao.sce");
			//Log.v("测试", zhongmeng.file+".sce");
			BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
			while ((s = bfr.readLine()) != null) {
				//Log.v(null, s);
				temp = s.split(",");
				aList.add(new Meohe(temp[0], temp[1], temp[2],temp[3], temp[4]));
			}
			bfr.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return aList;
	}
}