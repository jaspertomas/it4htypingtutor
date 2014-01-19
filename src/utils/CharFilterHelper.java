package utils;

import java.util.ArrayList;

public class CharFilterHelper {
	// public static void main(String args[])
	// {
	// String[] input=Texts.list[0];
	// String filter="asdfASDF";
	// for(String s:filter(input,filter,30))
	// System.out.println(s);
	// }

	public static ArrayList<String> filter(String[] input, String filter,
			Integer maxlinelength) {
		init();

		ArrayList<String> array = new ArrayList<String>();
		String charstring;
		String filteredstring = "";
		boolean lastwasspace = true; // ensure that text does not begin with a
										// space

		for (String s : input) {
			for (Character c : s.toCharArray()) {
				charstring = c.toString();
				if (filter.contains(charstring)) {
					lastwasspace = false;
					filteredstring += charstring;
					if (Character.getNumericValue(c.charValue()) >= 0)
						chartotals[Character.getNumericValue(c.charValue())]++;
				} else if (c == ' ') {
					if (lastwasspace) {
						// ignore
					} else {
						lastwasspace = true;
						filteredstring += " ";
						// add and reset only if adding the next string will
						// exceed length limit
						if (filteredstring.length() >= maxlinelength) {
							array.add(filteredstring);
							filteredstring = "";
						}
					}
				}
			}
		}
		array.add(filteredstring);

		for (int i = 0; i < chartotals.length; i++)
			System.out.println(String.valueOf(i) + " " + chartotals[i]);

		return array;
	}

	//this provides statistics to the number of times each letter appears. 
	//statistics of capital and small letters are combined
	private static Integer[] chartotals = new Integer[255];
	private static void init() {
		for (int i = 0; i < chartotals.length; i++) {
			chartotals[i] = 0;
		}
	}

}