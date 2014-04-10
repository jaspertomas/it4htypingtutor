package utils;

import models.Tests;
import models.Users;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "itfhtypingtutor";
	private static final int latestVersion=1;
	private static MyDatabaseHelper instance;
	public static void initialize(Context context)
	{
		instance=new MyDatabaseHelper(context);
	}
	public static MyDatabaseHelper getInstance()
	{
		return instance;
	}

	private MyDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, latestVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(Users.createTable());		
		database.execSQL(Tests.createTable());		
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {


		
		if(oldVersion<2)
		{
		}
		
	}
	public void emptyDatabase() {
		SQLiteDatabase database=instance.getWritableDatabase();
//		database.execSQL("delete from doctors;");
			database.close();
		
		
	}
}