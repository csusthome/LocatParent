package com.myy.parent.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper{
	
	public static final String CREATE_SWITCH="create table switch("
			+ "id integer primary key autoincrement,"
			+ "localstate integer,"
			+ "cutrepeation integer,"
			+ "blackwhitenamelist integer)";
	
	public static final String CREATE_DATA = "create table info("
			+ "id integer primary key autoincrement,"
			+ "checkcode text)";
	
	private Context context=null;
	
	public MyDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO 自动生成的构造函数存根
		this.context=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db){
		// TODO 自动生成的方法存根
		db.execSQL(CREATE_SWITCH);
		db.execSQL(CREATE_DATA);
		initDataTable(db);
		Toast.makeText(context, "创建成功", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自动生成的方法存根	
		db.execSQL("drop table switch;");
		db.execSQL("drop table info;");
	}
	
	private void initDataTable(SQLiteDatabase db)
	{
		ContentValues values = new ContentValues();
    	values.put("localstate", 0);
    	values.put("cutrepeation", 0);
    	values.put("blackwhitenamelist",0);
    	db.insert("switch", null, values); //初始化插入第一条数据
		ContentValues cv=new ContentValues();
//		String checkCode = ImeiDialog.randomCheckCode();
//		cv.put("checkcode",checkCode);
		db.insert("info",null,cv);
	}
	
	public String getCheckCode()
	{
		//获得SQLiteDatabase实例
        SQLiteDatabase db=getWritableDatabase();
        //查询获得Cursor
        Cursor c=db.query("info", null, null, null, null, null, null);
        c.moveToFirst();
        String chcekCode = c.getString(1);
        return chcekCode;
	}
}
