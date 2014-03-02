package models;

/*
 * From http://www.azlyrics.com/j/jackson.html
 * */

public class Lessons {
	public static String[][] list=
		{
			{"ASDF","asdf",},
			{"JKL;","jkl;",},
			{"Home keys","asdfjkl;",},
			{"QWER","asdfqwer",},
			{"UIOP","jkl;uiop",},
			{"ZXCV","asdfzxcv",},
			{"NM,.","jkl;nm,.",},
			{"Entire Middle Row","asdfjkl;gh",},
			{"Entire Top Row","qwertyuiop",},
			{"Entire Bottom Row","zxcvbnm,./",},
			{"Entire Alphabet","asdfjkl;ghqwertyuiopzxcvbnm,./",},
		};
	public static void main(String args[])
	{
		
		System.out.println(list[1][1]);
	}
	
	
}
