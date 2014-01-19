package models;

import java.util.ArrayList;
import java.util.HashMap;

import utils.MyDatabaseHelper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Tests {
	//------------CONSTANTS-----------
	public static final String tablename="texts";
	public static String[] fields={
		"id"
		,"user_id"
		,"lesson_id"
		,"speed"
		,"accuracy"
		,"completed"
		};
	public static String[] fieldtypes={
		"integer"
		,"integer"
		,"integer"
		,"integer"
		,"integer"
		,"integer"
		};//database types, for use by MyDatabaseHelper
	//-----------------------

	private static HashMap<String, Test> items = null;

	// to be called after update, when it is possible that database has been
	// altered
	public static void reset() {
		items = null;
	}

	public static HashMap<String, Test> getItems() {
		if (items == null) {
			SQLiteDatabase db = MyDatabaseHelper.getInstance()
					.getWritableDatabase();

			Cursor cursor = db.rawQuery("SELECT * FROM "+tablename, null);
			items = new HashMap<String, Test>();
			while (cursor.moveToNext()) {
				items.put(cursor.getString(0), new Test(cursor));
			}
		}
		return items;
	}

	//-----------getter functions----------
	public static Test getByName(String name)
	{
		//initialize if not yet initialized
		getItems();
		
		for(Test item:items.values())
		{
			if(item.getName().contentEquals(name))
				return item;
		}
		return null;
	}	
	public static Test getById(Integer id) {
		// initialize if not yet initialized
		getItems();
		return items.get(id);
	}
	
	//-----------database functions--------------
	public static void delete(Test item)
	{
		SQLiteDatabase db = MyDatabaseHelper.getInstance().getWritableDatabase();
		db.execSQL("delete from "+tablename+" where id = '"+item.getId()+"';");
	}
	public static void delete(Integer id)
	{
		SQLiteDatabase db = MyDatabaseHelper.getInstance().getWritableDatabase();
		db.execSQL("delete from "+tablename+" where id = '"+id+"';");
	}
	public static void insert(Test item)
	{
		SQLiteDatabase db = MyDatabaseHelper.getInstance().getWritableDatabase();
		
		if(fieldtypes[0].contentEquals("integer"))
		{
			db.execSQL("INSERT INTO "+tablename+" ("+implodeFields(false)+")VALUES (" 
					+implodeValues(item, false)
					+");");
		}
		else
		if(fieldtypes[0].contains("varchar"))
		{
			db.execSQL("INSERT INTO "+tablename+" ("+implodeFields(true)+")VALUES (" 
					+implodeValues(item, true)
					+");");
		}
	}
	public static void update(Test item)
	{
		SQLiteDatabase db = MyDatabaseHelper.getInstance().getWritableDatabase();

		db.execSQL(
		"update "+tablename+" set "+implodeFieldsWithValues(item,false)+" where id = '"+item.getId()
		+"';");
	}

	//-----------database helper functions--------------
	public static String implodeValues(Test item,boolean withId)
	{
		ArrayList<String> values=item.implodeFieldValuesHelper(withId);
		String output="";
		for(String value:values)
		{
			if(!output.isEmpty())
				output+=",";
			output+="'"+value+"'";
		}
		return output;
	}
	public static String implodeFields(boolean withId)
	{
		String output="";
		for(String field:fields)
		{
			if(!withId && field.contentEquals("id"))continue;
			if(!output.isEmpty())
				output+=",";
			output+=field;
		}
		return output;
	}
	public static String implodeFieldsWithValues(Test item,boolean withId)
	{
		ArrayList<String> values=item.implodeFieldValuesHelper(true);//get entire list of values; whether the id is included will be dealt with later.
		
		if(values.size()!=fields.length)
		{
			Log.e("Tests","implodeFieldsWithValues(): ERROR: values length does not match fields length");
		}
		
		String output="";
		for(int i=0;i<fields.length;i++)
		{
			if(!withId && fields[i].contentEquals("id"))continue;
			if(!output.isEmpty())
				output+=",";
			output+=fields[i]+"='"+values.get(i)+"'";
		}
		return output;
	}	
	public static String implodeFieldsWithTypes()
	{
		String output="";
		for(int i=0;i<fields.length;i++)
		{
			if(fields[i].contentEquals(fields[0]))//fields[0] being the primary key
				output+=fields[i]+" "+fieldtypes[i]+" PRIMARY KEY";
			else
				output+=","+fields[i]+" "+fieldtypes[i];
		}
		return output;
	}	
	public static String createTable()
	{
		return "CREATE TABLE IF NOT EXISTS "+tablename+" ("+implodeFieldsWithTypes()+" );";
	}
	public static String deleteTable()
	{
		return "DROP TABLE IF EXISTS "+tablename;
	}
}
