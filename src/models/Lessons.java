package models;

import java.util.HashMap;

/*
 * From http://www.azlyrics.com/j/jackson.html
 * */

public class Lessons {
	public static String[][] list=
		{
			{"ASDF","asdf",},
			{"JKL;","jkl;:'\"",},
			{"Home keys","asdfjkl;:'\"",},
			{"Home Row","asdfjkl;:'\"gh",},
			{"QWER","asdfqwer",},
			{"UIOP","jkl;:'\"uiop",},
			{"Top Row","asdfjkl;:'\"ghqwertyuiop",},
			{"ZXCV","asdfzxcv",},
			{"NM,.","jkl;:'\"nm,.<>?/",},
			{"Bottom Row","asdfjkl;:'\"ghzxcvbnm,./<>/?",},
			{"Entire Alphabet"," asdfjkl;:'\"ghqwertyuiopzxcvbnm,./<>/?",},
		};
	private static HashMap<String,String> map=new HashMap<String,String>();
	public static HashMap<String,String> getMap()
	{
		if(map.size()==0)
		{
			init();
		}
		return map;
	}
	private static void init()
	{
		for(String[] pair:list)
		{
			map.put(pair[0], pair[1]);
		}
	}
	public static void main(String args[])
	{
		
		System.out.println(list[1][1]);
	}
	
	
}
