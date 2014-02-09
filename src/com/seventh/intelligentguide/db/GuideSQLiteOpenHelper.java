package com.seventh.intelligentguide.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class GuideSQLiteOpenHelper extends SQLiteOpenHelper {
    //�û����ݿ��ļ��İ汾
    private static final int DB_VERSION    = 1;
    //���ݿ��ļ�Ŀ����·��ΪϵͳĬ��λ�ã�cn.arthur.examples ����İ���
    private static String DB_PATH  = "/data/data/com.seventh.intelligentguide/databases/";

    private static String DB_NAME = "jingdian.db";

    private SQLiteDatabase myDataBase = null;
    private final Context myContext;
    
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