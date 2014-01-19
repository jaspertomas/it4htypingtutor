package models;

import java.util.ArrayList;

import android.database.Cursor;

public class App {

	String name="",componentname="",id="";
	public App() {
	}
	public App(Cursor c) {
		id=c.getString(c.getColumnIndex("id"));
		name = c.getString(c.getColumnIndex("name"));
		componentname = c.getString(c.getColumnIndex("componentname"));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
//	public String getUuid()
//	{
//		return "app-"+id.toString()+"-";
//	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComponentname() {
		return componentname;
	}
	public void setComponentname(String componentname) {
		this.componentname = componentname;
	}
	//database functions
	public ArrayList<String> implodeFieldValuesHelper(boolean withId)
	{
		ArrayList<String> values=new ArrayList<String>(); 
		if(withId)values.add(id.toString());

		//add values for each field here
		values.add(name);
		values.add(componentname);
		
		return values;
	}
	public void delete()
	{
		Apps.delete(this);
	}
	public void save()
	{
		if(Apps.getById(id)==null)
			Apps.insert(this);
		else
			Apps.update(this);
	}
	public String toString()
	{
		return name;
	}
//	public ContentValues insertValues()
//	{
//		ContentValues insertValues = new ContentValues();
//
////		"id"
////		,"name"
////		,"componentname"
//		
//		//add id only if id is varchar
//		//assuming id types are only integer and varchar
////		if(!Apps.fieldtypes[0].contentEquals("integer"))
//		insertValues.put(Apps.fields[0], id);
//		insertValues.put(Apps.fields[1], name);
//		insertValues.put(Apps.fields[2], componentname);
//		return insertValues;
//	}		
//
//	public void save()
//	{
//		if(id.isEmpty())
//			//for varchar ids - id is already known before save
//			Apps.insert(this);
//			//for string ids
////			id=Apps.insert(this);
//		else
//			Apps.update(this);
//	}
}
