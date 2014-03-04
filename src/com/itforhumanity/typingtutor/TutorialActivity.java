package com.itforhumanity.typingtutor;

import java.util.ArrayList;

import models.Lessons;
import models.Texts;
import utils.CharFilterHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
public class TutorialActivity extends Activity {

	static TutorialActivity instance;
	public static TutorialActivity getInstance()
	{
		return instance;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance=TutorialActivity.this;
		setContentView(R.layout.activity_tutorial);
		setupView();
	}

	private TextView youtyped1,youtyped2,youtyped3,youtyped4;
	private TextView typeme1,typeme2,typeme3,typeme4;
	private EditText input;
	TextView[] youtypeds;
	TextView[] typemes;
	BlinkerRunnable blinker;
	final Integer rowcount=4; 
	Boolean success=false;
	ArrayList<String> texttotype;
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
		input.addTextChangedListener(new TutorialTextWatcher(input));
		youtypeds=new TextView[rowcount];
		typemes=new TextView[rowcount];
		youtypeds[0]=youtyped1;
		youtypeds[1]=youtyped2;
		youtypeds[2]=youtyped3;
		youtypeds[3]=youtyped4;
		typemes[0]=typeme1;
		typemes[1]=typeme2;
		typemes[2]=typeme3;
		typemes[3]=typeme4;
		
		//setup Text
		Double d=Math.random()*Texts.list.length;
		Integer textid=d.intValue();
		String text[]=Texts.list[textid];
	    String filter=Lessons.getMap().get(getIntent().getAction());
		texttotype=CharFilterHelper.filter(text,filter,60);

		try{
			typeme1.setText(texttotype.get(0).trim());
			typeme2.setText(texttotype.get(1).trim());
			typeme3.setText(texttotype.get(2).trim());
			typeme4.setText(texttotype.get(3).trim());
		}catch(IndexOutOfBoundsException e)
		{
			//too few letters; do nothing
			Log.i("","too few letters");
		}
		
		blinker=new BlinkerRunnable(this);
		Thread t=new Thread(blinker);
		t.start();
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tutorial, menu);
		return true;
	}

	class TutorialTextWatcher implements TextWatcher
	{

		EditText input;
		TutorialTextWatcher(EditText input)
		{
			this.input=input;
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if(s.length()==0)return;
//			Toast.makeText(TutorialActivity.getInstance(), s, Toast.LENGTH_SHORT).show();
			input.setText("");
			TutorialActivity.getInstance().processInput(s);
			
		}
	}

	Integer row=0, column=0;
	public void processInput(CharSequence s) {
		if(success)return;
		CharSequence c=typemes[row].getText().subSequence(column, column+1);
//		Toast.makeText(TutorialActivity.getInstance(), c, Toast.LENGTH_SHORT).show();
		
		if(!s.toString().contentEquals(c.toString()))return;
		
		if(blinked)blink();
		youtypeds[row].setText(youtypeds[row].getText().toString()+s.toString());
		column++;
		if(youtypeds[row].getText().length()==typemes[row].getText().length())
		{
			if(row==texttotype.size()-1)
			{
				blinker.close();
//				Toast.makeText(TutorialActivity.this, "SUCCESS!", Toast.LENGTH_LONG);
				success=true;
				runOnUiThread(new Runnable() {
			        public void run()
			        {
			        	showAlertDialogUsingBuilder(  TutorialActivity.this, 
			        			"Typing Tutor", 
			        			"SUCCESS!!!", 
			        			"OK");
			        }
			    });
			}
			else
			{
				column=0;
				row++;
			}
		}
	}
	Boolean blinked=false;//if blinked is true, underscore is present
	public void blink()
	{
		String s=youtypeds[row].getText().toString();
		if(blinked==false)
		{
			youtypeds[row].setText(s+"_");
			blinked=true;
		}
		else
		{
			youtypeds[row].setText(s.substring(0, s.length()-1));
			blinked=false;
		}
	}

	public void showAlertDialogUsingBuilder(Context context, String title, String message, String posButtonMsg) {
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(title);
		alertDialogBuilder
		.setMessage(message).setPositiveButton(posButtonMsg,new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, close
				// current activity
				Log.d("JPC","OK Buton inside alert Clicked");
			}
		  });
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.back:
	        	finish();
	            return true;
	        default:
	            return false;
	    }
	}		
}
class BlinkerRunnable implements Runnable
{
	Boolean running=false;
	TutorialActivity context;
	public BlinkerRunnable(TutorialActivity context)
	{
		this.context=context;
	}
	public void close()
	{
		running=false;
	}
    public void run()
    {
    	running=true;
    	Looper.prepare();
    	while(running)
    	{
			context.runOnUiThread(new Runnable() {
		        public void run()
		        {
		    		context.blink();
		        }
				});
    		try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
}