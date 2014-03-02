package com.itforhumanity.typingtutor;

import java.util.ArrayList;

import models.Texts;
import utils.CharFilterHelper;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

public class TutorialActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);
		setupView();
	}

	private TextView youtyped1,youtyped2,youtyped3,youtyped4;
	private TextView typeme1,typeme2,typeme3,typeme4;
	private EditText input;
	private void setupView(){
		
		youtyped1 = (TextView) findViewById(R.id.youtyped1);
		typeme1 = (TextView) findViewById(R.id.typeme1);
		youtyped2 = (TextView) findViewById(R.id.youtyped2);
		typeme2 = (TextView) findViewById(R.id.typeme2);
		youtyped3 = (TextView) findViewById(R.id.youtyped3);
		typeme3 = (TextView) findViewById(R.id.typeme3);
		youtyped4 = (TextView) findViewById(R.id.youtyped4);
		typeme4 = (TextView) findViewById(R.id.typeme4);
		input = (EditText) findViewById(R.id.input);
		
		
		//setup Text
		Double d=Math.random()*Texts.list.length;
		Integer textid=d.intValue();
		String text[]=Texts.list[textid];
	    String filter=getIntent().getAction();
		ArrayList<String> texttotype=CharFilterHelper.filter(text,filter,30);
		
		typeme1.setText(texttotype.get(0));
		typeme2.setText(texttotype.get(1));
		typeme3.setText(texttotype.get(2));
		typeme4.setText(texttotype.get(3));
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tutorial, menu);
		return true;
	}

}
