package com.seventh.intelligentguide.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class GuideSQLiteOpenHelper extends SQLiteOpenHelper {
    //用户数据库文件的版本
    private static final int DB_VERSION    = 1;
    //数据库文件目标存放路径为系统默认位置，cn.arthur.examples 是你的包名
    private static String DB_PATH  = "/data/data/com.seventh.intelligentguide/databases/";

    private static String DB_NAME = "jingdian.db";

    private SQLiteDatabase myDataBase = null;
    private final Context myContext;
    
    /**
     * 在SQLiteOpenHelper的子类当中，必须有该构造函数
     * @param context    上下文对象
     * @param name        数据库名称
     * @param factory    一般都是null
     * @param version    当前数据库的版本，值必须是整数并且是递增的状态
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
     * 该函数是在第一次创建的时候执行，
     * 实际上是第一次得到SQLiteDatabase对象的时候才会调用这个方法
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    
    /**
     * 数据库表结构有变化时采用
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}