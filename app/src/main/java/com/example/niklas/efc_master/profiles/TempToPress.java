package com.example.niklas.efc_master.profiles;

public final class TempToPress
{
	private TempToPress()
	{

	}

	private final static int temp[] = { 0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60};
	private final static int press[] = {9, 9,  8,  7,  6,  6,  5,  5,  4,  3,  2,  1,  0};

	public static int myNumOfPress(int x)
	{
		//function
		if (x <= temp[0])
		{
			return press[0];
		}
		else if (x >= temp[9])
		{
			return press[9];
		}

		int inX = 9;
		for (; x < temp[inX]; inX--);
			return (press[inX]+((x-temp[inX])*(press[inX+1]-press[inX]) / (temp[inX+1] - temp[inX])));
	}
}
