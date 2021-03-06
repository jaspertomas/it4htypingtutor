package com.itforhumanity.typingtutor;

import java.util.ArrayList;
import java.util.List;

import models.Lessons;
import utils.MyApplicationContextHolder;
import utils.MyDatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		TextView lblName = (TextView) findViewById(R.id.text_view);
		lblName.setText("Choose a lesson.");
		
		//initialize resources
		MyApplicationContextHolder.setAppContext(getApplicationContext());
		MyDatabaseHelper.initialize(MyApplicationContextHolder.getAppContext());
	
		setupView();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
//	public void showLogin(View button) {
//		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//        startActivity(intent);
//	}

	

	private void setupView()
	{
		
		List<String> listitems;
        ListView listView = (ListView) findViewById (R.id.list_view);
        
		SQLiteDatabase db=MyDatabaseHelper.getInstance().getWritableDatabase();
        

		//=====read doctors table and add results to listview=======
		listitems= new ArrayList<String>(Lessons.list.length);

        for(String[] lesson:Lessons.list)
        {
        	
        	listitems.add(lesson[0]);
       	}

        if (listView != null) {
            listView.setAdapter(new ArrayAdapter<String>(MenuActivity.this,
              android.R.layout.simple_list_item_1, listitems));


            listView.setOnItemClickListener(new OnItemClickListener() {
    			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
    			    {
    			      String selectedFromList = (parent.getItemAtPosition(position).toString());
    					//Toast.makeText(MenuActivity.this, selectedFromList, Toast.LENGTH_LONG).show();
    					Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
    			        intent.setAction(selectedFromList);
    					startActivity(intent);
    			      

    			    }});	
        
        }	   
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle item selection
//		switch (item.getItemId()) {
//		case R.id.menu_cancel:
//			finish();
//			return true;
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//	}
	
}
