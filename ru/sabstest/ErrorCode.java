package ru.sabstest;

import java.util.Arrays;

public class ErrorCode {
	private static final String[] codes = {"39","49"};
	
	public static boolean contains(String s)
	{
		return Arrays.asList(codes).contains(s);
	}
}
