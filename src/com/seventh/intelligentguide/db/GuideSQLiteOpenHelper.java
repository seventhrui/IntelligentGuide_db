package com.seventh.intelligentguide.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Joshua
 * �÷���
 * DBHelper dbHelper = new DBHelper(this);
 * dbHelper.createDataBase();
 * SQLiteDatabase db = dbHelper.getWritableDatabase();
 * Cursor cursor = db.query()
 * db.execSQL(sqlString);
 * ע�⣺execSQL��֧�ִ�;�Ķ���SQL��䣬ֻ��һ��һ����ִ�У����˺ܾò�����
 * ��execSQL��Դ��ע�� (Multiple statements separated by ;s are not supported.)
 * ����assets�µ����ݿ��ļ�ֱ�Ӹ��Ƶ�DB_PATH�������ݿ��ļ���С������1M����
 * ����г���1M�Ĵ��ļ�������Ҫ�ȷָ�ΪN��С�ļ���Ȼ��ʹ��copyBigDatabase()�滻copyDatabase()
 */
public class GuideSQLiteOpenHelper extends SQLiteOpenHelper {
    //�û����ݿ��ļ��İ汾
    private static final int DB_VERSION    = 1;
    //���ݿ��ļ�Ŀ����·��ΪϵͳĬ��λ�ã�cn.arthur.examples ����İ���
    private static String DB_PATH  = "/data/data/com.seventh.intelligentguide/databases/";
/*
    //�����������ݿ��ļ������SD���Ļ�
    private static String DB_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/arthurcn/drivertest/packfiles/";
*/
    private static String DB_NAME = "jingdian.db";
    private static String ASSETS_NAME = "jingdian.db";

    private SQLiteDatabase myDataBase = null;
    private final Context myContext;

     /** 
      * ������ݿ��ļ��ϴ�ʹ��FileSplit�ָ�ΪС��1M��С�ļ�
      * �����зָ�Ϊ hello.db.101    hello.db.102    hello.db.103
      */
    //��һ���ļ�����׺
    private static final int ASSETS_SUFFIX_BEGIN = 101;
    //���һ���ļ�����׺
    private static final int ASSETS_SUFFIX_END = 103;
    
    /**
     * ��SQLiteOpenHelper�����൱�У������иù��캯��
     * @param context    �����Ķ���
     * @param name        ���ݿ�����
     * @param factory    һ�㶼��null
     * @param version    ��ǰ���ݿ�İ汾��ֵ���������������ǵ�����״̬
     */
    public GuideSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, null, version);
        this.myContext = context;
    }
    
    public GuideSQLiteOpenHelper(Context context, String name, int version){
        this(context,name,null,version);
    }

    public GuideSQLiteOpenHelper(Context context, String name){
        this(context,name,DB_VERSION);
    }
    
    public GuideSQLiteOpenHelper (Context context) {
        this(context, DB_PATH + DB_NAME);
    }
    
    public void createDataBase(){// throws IOException
        boolean dbExist = checkDataBase();
        if(dbExist){
        }else{
            File dir = new File(DB_PATH);
			if(!dir.exists()){
			    dir.mkdirs();
			}
			File dbf = new File(DB_PATH + DB_NAME);
			if(dbf.exists()){
			    dbf.delete();
			}
			SQLiteDatabase.openOrCreateDatabase(dbf, null);
			// ����asseets�е�db�ļ���DB_PATH��
			copyDataBase();
        }
    }
    
    //������ݿ��Ƿ���Ч
    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        String myPath = DB_PATH + DB_NAME;
        try{            
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            //database does't exist yet.
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }
    
    public void copyDataBase(){
        InputStream myInput;
        String outFileName;
        OutputStream myOutput;
        byte[] buffer = new byte[1024];
        int length;
		try {
			myInput = myContext.getAssets().open(ASSETS_NAME);
			outFileName = DB_PATH + DB_NAME;
			myOutput = new FileOutputStream(outFileName);
			while ((length = myInput.read(buffer))>0){
	            myOutput.write(buffer, 0, length);
	        }
	        myOutput.flush();
	        myOutput.close();
	        myInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    //����assets�µĴ����ݿ��ļ�ʱ�����
    private void copyBigDataBase() throws IOException{
        InputStream myInput;
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        for (int i = ASSETS_SUFFIX_BEGIN; i < ASSETS_SUFFIX_END+1; i++) {
            myInput = myContext.getAssets().open(ASSETS_NAME + "." + i);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myInput.close();
        }
        myOutput.close();
    }
    
    @Override
    public synchronized void close() {
        if(myDataBase != null){
            myDataBase.close();
        }
        super.close();
    }
    
    /**
     * �ú������ڵ�һ�δ�����ʱ��ִ�У�
     * ʵ�����ǵ�һ�εõ�SQLiteDatabase�����ʱ��Ż�����������
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    
    /**
     * ���ݿ��ṹ�б仯ʱ����
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}