package utils;

import java.util.ArrayList;

import android.content.Context;
import android.widget.Toast;

/*
 * This helper accumulates messages and shows them on demand. 
 * 
 * It is to be used to accept messages from places in the code where toasts are not safe or desirable
 * to be made and allows them to be shown where it's ok to do so
 * */
public class MyToastHelper {
	private static ArrayList<String> messages=new ArrayList<String>();
	public static boolean hasMessages()
	{
		return messages.size()==0;
	}
	public static void addMessage(String msg)
	{
		messages.add(msg);
	}
	public static void show(Context context, Integer duration)
	{
		if(duration==null)duration=Toast.LENGTH_LONG;
		
		for(String msg:messages)
		{
			Toast.makeText(context, msg, duration).show();
		}
	}
}
