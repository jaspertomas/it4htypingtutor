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
			{"QWER","asdfqwer",},
			{"UIOP","jkl;:'\"uiop",},
			{"ZXCV","asdfzxcv",},
			{"NM,.","jkl;:'\"nm,.",},
			{"Entire Middle Row","asdfjkl;:'\"gh",},
			{"Entire Top Row","qwertyuiop",},
			{"Entire Bottom Row","zxcvbnm,./",},
			{"Entire Alphabet","asdfjkl;:'\"ghqwertyuiopzxcvbnm,./",},
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
