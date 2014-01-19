package models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.database.Cursor;

public class User {
/*

 * */
	String username="",password="";//,id="";
	Integer id=0;
	public User() {
	}
	
	public User(Integer id,String username, String email, String password, String auth_token) {//, String line
		super();
		this.id=id;
		this.username = username;
		this.password = password;
	}

	public User(Cursor c) {
		id=c.getInt(c.getColumnIndex("id"));
		username = c.getString(c.getColumnIndex("employee_id"));
		password = c.getString(c.getColumnIndex("password"));
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	//database functions
	public ArrayList<String> implodeFieldValuesHelper(boolean withId)
	{
		ArrayList<String> values=new ArrayList<String>(); 
		if(withId)values.add(id.toString());

		//add values for each field here
		values.add(username);
		values.add(password);

		return values;
	}
	public void delete()
	{
		Users.delete(this);
	}
	public void save()
	{
//		if(Users.getByUsername(username)==null)
		if(Users.getById(id)==null)
			Users.insert(this);
		else
			Users.update(this);
	}
	public String toString()
	{
		return username;
	}
}
