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
		// TODO �Զ����ɵĹ��캯�����
		this.context=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db){
		// TODO �Զ����ɵķ������
		db.execSQL(CREATE_SWITCH);
		db.execSQL(CREATE_DATA);
		initDataTable(db);
		Toast.makeText(context, "�����ɹ�", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO �Զ����ɵķ������	
		db.execSQL("drop table switch;");
		db.execSQL("drop table info;");
	}
	
	private void initDataTable(SQLiteDatabase db)
	{
		ContentValues values = new ContentValues();
    	values.put("localstate", 0);
    	values.put("cutrepeation", 0);
    	values.put("blackwhitenamelist",0);
    	db.insert("switch", null, values); //��ʼ�������һ������
		ContentValues cv=new ContentValues();
//		String checkCode = ImeiDialog.randomCheckCode();
//		cv.put("checkcode",checkCode);
		db.insert("info",null,cv);
	}
	
	public String getCheckCode()
	{
		//���SQLiteDatabaseʵ��
        SQLiteDatabase db=getWritableDatabase();
        //��ѯ���Cursor
        Cursor c=db.query("info", null, null, null, null, null, null);
        c.moveToFirst();
        String chcekCode = c.getString(1);
        return chcekCode;
	}
}
