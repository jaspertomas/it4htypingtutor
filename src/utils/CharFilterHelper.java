package utils;

import java.util.ArrayList;

import android.util.Log;

public class CharFilterHelper {
	// public static void main(String args[])
	// {
	// String[] input=Texts.list[0];
	// String filter="asdf";
	// for(String s:filter(input,filter,30))
	// System.out.println(s);
	// }

	public static ArrayList<String> filter(String[] input, String filter,
			Integer maxlinelength) {
		init();
		
		//include both uppercase and lowercase letters in filter
		//example: asdF becomes asdfASDF
		filter=filter.toLowerCase()+filter.toUpperCase();

		ArrayList<String> array = new ArrayList<String>();
		String charstring;
		String filteredstring = "";
		boolean lastwasspace = true; // ensure that text does not begin with a
										// space
		Integer counter=0;

		for (String s : input) {
			//terminate with space - add a space between lines
			s+=" ";
			for (Character c : s.toCharArray()) {
				charstring = c.toString();
				//prevent doublespace;
				//break lines on spaces, when line meets or exceeds maxlinelength
				if (c == ' ') {
					if (lastwasspace) {
						// ignore double spaces
					} else {
						lastwasspace = true;
						if(filter.contains(charstring))	
						{
							filteredstring += " ";
						}					
						//if filter doesn't contain " ", 
						//we'll add one anyway every few words 
						else 
						{
							counter++;
							Log.e("",counter.toString());
							if(counter==4)
							{
								counter=0;
								filteredstring += " ";
								Log.e("","space");
							}
						}
						// add and reset only if adding the next string will
						// exceed length limit
						if (filteredstring.length() >= maxlinelength) {
							array.add(filteredstring);
							filteredstring = "";
						}
					}
				}
				else 
				if (filter.contains(charstring)) {
					lastwasspace = false;
					filteredstring += charstring;
					if (Character.getNumericValue(c.charValue()) >= 0)
						chartotals[Character.getNumericValue(c.charValue())]++;
				} 				
			}
		}
		array.add(filteredstring);

//		for (int i = 0; i < chartotals.length; i++)
//			System.out.println(String.valueOf(i) + " " + chartotals[i]);

		//calculate total number of letters
		totalcharcount=0;
		for(String s:array)
			totalcharcount+=s.length();
		
		return array;
	}
	//this provides total number of letters
	private static Integer totalcharcount = 0;
	

	public static Integer getCharTotal() {
		return totalcharcount;
	}
	public static Integer[] getCharStats() {
		return chartotals;
	}
	//this provides statistics to the number of times each letter appears. 
	//statistics of capital and small letters are combined
	public static Integer[] chartotals = new Integer[255];
	public static void init() {
		for (int i = 0; i < chartotals.length; i++) {
			chartotals[i] = 0;
		}
	}

}