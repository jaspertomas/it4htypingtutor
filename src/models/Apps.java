package models;

import java.util.ArrayList;
import java.util.HashMap;

import utils.MyDatabaseHelper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Apps {
	//------------CONSTANTS-----------
	public static final String dbname="apps";
	public static String[] fields={
		"id"
		,"name"
		,"componentname"
		};
	public static String[] fieldtypes={
		"varchar(40)"
		,"varchar(100)"
		,"varchar(500)"
		};//database types, for use by MyDatabaseHelper
	//-----------------------

	private static HashMap<String, App> items = null;

	// to be called after update, when it is possible that database has been
	// altered
	public static void reset() {
		items = null;
	}

	public static HashMap<String, App> getItems() {
		if (items == null) {
			SQLiteDatabase db = MyDatabaseHelper.getInstance()
					.getWritableDatabase();

			Cursor cursor = db.rawQuery("SELECT * FROM "+dbname, null);
			items = new HashMap<String, App>();
			while (cursor.moveToNext()) {
				items.put(cursor.getString(0), new App(cursor));
			}
		}
		return items;
	}

	//-----------getter functions----------
	public static App getByName(String name)
	{
		//initialize if not yet initialized
		getItems();
		
		for(App item:items.values())
		{
			if(item.getName().contentEquals(name))
				return item;
		}
		return null;
	}	
	public static App getById(String id) {
		// initialize if not yet initialized
		getItems();
		return items.get(id);
	}
	
	//-----------database functions--------------
	public static void delete(App item)
	{
		SQLiteDatabase db = MyDatabaseHelper.getInstance().getWritableDatabase();
		db.execSQL("delete from "+dbname+" where id = '"+item.getId()+"';");
	}
	public static void delete(Integer id)
	{
		SQLiteDatabase db = MyDatabaseHelper.getInstance().getWritableDatabase();
		db.execSQL("delete from "+dbname+" where id = '"+id+"';");
	}
	public static void insert(App item)
	{
		SQLiteDatabase db = MyDatabaseHelper.getInstance().getWritableDatabase();
		
		if(fieldtypes[0].contentEquals("integer"))
		{
			db.execSQL("INSERT INTO "+dbname+" ("+implodeFields(false)+")VALUES (" 
					+implodeValues(item, false)
					+");");
		}
		else
		if(fieldtypes[0].contains("varchar"))
		{
			db.execSQL("INSERT INTO "+dbname+" ("+implodeFields(true)+")VALUES (" 
					+implodeValues(item, true)
					+");");
		}
	}
	public static void update(App item)
	{
		SQLiteDatabase db = MyDatabaseHelper.getInstance().getWritableDatabase();

		db.execSQL(
		"update "+dbname+" set "+implodeFieldsWithValues(item,false)+" where id = '"+item.getId()
		+"';");
	}

//	//for string ids
////	public static String insert(App item)
////	{
////		SQLiteDatabase db = MyDatabaseHelper.getInstance().getWritableDatabase();
////		Long l=db.insert(dbname, null, item.insertValues()); 
////		String[] queryfields={"guid"};
////		Cursor c=db.query(dbname, queryfields, "id='"+l+"'",null,null,null,null);
////		c.moveToNext();
////		return c.getString(0);
////	}
//	//for integer ids
//	public static Integer insert(App item)
//	{
//		SQLiteDatabase db = MyDatabaseHelper.getInstance().getWritableDatabase();
//		return (int) db.insert(dbname, null, item.insertValues()); 
//	}	
//	public static void update(App item)
//	{
//		SQLiteDatabase db = MyDatabaseHelper.getInstance().getWritableDatabase();
//		db.update(dbname,item.insertValues(),"id='"+item.getId()+"'",null);
//	}	
	//-----------database helper functions--------------
	public static String implodeValues(App item,boolean withId)
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
	public static String implodeFieldsWithValues(App item,boolean withId)
	{
		ArrayList<String> values=item.implodeFieldValuesHelper(true);//get entire list of values; whether the id is included will be dealt with later.
		
		if(values.size()!=fields.length)
		{
			Log.e("Apps","implodeFieldsWithValues(): ERROR: values length does not match fields length");
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
		return "CREATE TABLE IF NOT EXISTS "+dbname+" ("+implodeFieldsWithTypes()+" );";
	}
	public static String deleteTable()
	{
		return "DROP TABLE IF EXISTS "+dbname;
	}
}
