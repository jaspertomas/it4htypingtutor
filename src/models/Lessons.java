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
			{"G & H","asdfjkl;gh",},
		};
	public static void main(String args[])
	{
		
		System.out.println(list[1][1]);
	}
	
	
}
