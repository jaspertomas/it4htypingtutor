package models;

import java.util.ArrayList;

import android.database.Cursor;

public class Test {

	Integer id,user_id,lesson_id,speed,accuracy,completed;
	public Test() {
	}
	public Test(Cursor c) {
		id=c.getInt(c.getColumnIndex("id"));
		user_id = c.getInt(c.getColumnIndex("user_id"));
		lesson_id = c.getInt(c.getColumnIndex("lesson_id"));
		speed = c.getInt(c.getColumnIndex("speed"));
		accuracy = c.getInt(c.getColumnIndex("accuracy"));
		completed = c.getInt(c.getColumnIndex("completed"));
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
	//database functions
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Integer getLesson_id() {
		return lesson_id;
	}
	public void setLesson_id(Integer lesson_id) {
		this.lesson_id = lesson_id;
	}
	public Integer getSpeed() {
		return speed;
	}
	public void setSpeed(Integer speed) {
		this.speed = speed;
	}
	public Integer getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(Integer accuracy) {
		this.accuracy = accuracy;
	}
	public Integer getCompleted() {
		return completed;
	}
	public void setCompleted(Integer completed) {
		this.completed = completed;
	}
	

	public ArrayList<String> implodeFieldValuesHelper(boolean withId)
	{
		ArrayList<String> values=new ArrayList<String>(); 
		if(withId)values.add(id.toString());

		//add values for each field here
		values.add(user_id.toString());
		values.add(lesson_id.toString());
		values.add(speed.toString());
		values.add(accuracy.toString());
		values.add(completed.toString());
		
		return values;
	}
	public void delete()
	{
		Tests.delete(this);
	}
	public void save()
	{
		if(Tests.getById(id)==null)
			Tests.insert(this);
		else
			Tests.update(this);
	}
//	public String toString()
//	{
//		return name;
//	}
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
////		if(!Tests.fieldtypes[0].contentEquals("integer"))
//		insertValues.put(Tests.fields[0], id);
//		insertValues.put(Tests.fields[1], name);
//		insertValues.put(Tests.fields[2], componentname);
//		return insertValues;
//	}		
//
//	public void save()
//	{
//		if(id.isEmpty())
//			//for varchar ids - id is already known before save
//			Tests.insert(this);
//			//for string ids
////			id=Tests.insert(this);
//		else
//			Tests.update(this);
//	}
}
