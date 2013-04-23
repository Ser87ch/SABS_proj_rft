package ru.sabstest;

import java.io.File;
import java.text.DecimalFormat;


public class Init {
	
	public static void mkfolder()
	{
		try {		

			File dir = new File(Settings.testProj + "tests\\");

			String[] children = dir.list();
			if (children == null || children.length == 0) {
				Settings.folder = "a000001";
			} else {
				String filename = children[children.length - 1];
				if (filename.substring(1, 6).equals("999999"))
				{
					char s = (char)(filename.charAt(0) +  1);
					Settings.folder = Character.toString(s) + "000001";
				} else {
					int i = Integer.parseInt(filename.substring(1, 7)) + 1;
					DecimalFormat myFormat = new java.text.DecimalFormat("000000");
					Settings.folder = filename.substring(0, 1) + myFormat.format(new Integer(i));
				}
			}
			(new File(Settings.testProj + "tests\\" + Settings.folder)).mkdir();
			Settings.fullfolder = Settings.testProj + "tests\\" + Settings.folder + "\\";

			Log.create();
			Log.msg("Папка теста " + Settings.fullfolder + " создана.");
			
			(new File(Settings.fullfolder + "input")).mkdir();
			(new File(Settings.fullfolder + "output")).mkdir();			

			//DM
			//Log.msg("Папка промежуточных данных для теста " + Settings.fullfolder + "input создана.");
			//Log.msg("Папка исходящих данных для теста " + Settings.fullfolder + "output создана.");
			Log.msg("Папка для входящих данных теста (" + Settings.fullfolder + "input) создана.");
			Log.msg("Папка для исходящих данных теста (" + Settings.fullfolder + "output) создана.");

		
			//DM+: 6-->7
			//for(int i = 1 ; i < 6; i++)
			for(int i = 1 ; i < 7; i++)	
			{
				(new File(Settings.fullfolder + "\\input\\" + String.format("%03d", i))).mkdir();
				(new File(Settings.fullfolder + "\\output\\" + String.format("%03d", i))).mkdir();
			}			

		} catch(Exception e) {
			e.printStackTrace();
			Log.msg(e);
		}

	}

	public static void mkDataFolder()
	{
		File dir = new File(Settings.testProj + "data\\");
		String  fld;
		String[] children = dir.list();
		if (children == null || children.length == 0) {
			fld = "a000001";
		} else {
			String filename = children[children.length - 1];
			if (filename.substring(1, 6).equals("999999"))
			{
				char s = (char)(filename.charAt(0) +  1);
				fld = Character.toString(s) + "000001";
			} else {
				int i = Integer.parseInt(filename.substring(1, 7)) + 1;
				DecimalFormat myFormat = new java.text.DecimalFormat("000000");
				fld = filename.substring(0, 1) + myFormat.format(new Integer(i));
			}
		}

		Settings.datafolder = Settings.testProj + "data\\" + fld + "\\";

		(new File(Settings.testProj + "data\\" + fld)).mkdir();
		(new File(Settings.testProj + "data\\" + fld + "\\input")).mkdir();
		(new File(Settings.testProj + "data\\" + fld + "\\etalon")).mkdir();
		Log.createGen();
		Log.msg("Папка для теста новых данных " + Settings.testProj + "data\\" + fld + " создана.");
		Log.msg("Папка входящих данных для теста " + Settings.testProj + "data\\" + fld +  "\\input создана.");		
		Log.msg("Папка эталонных данных для теста "+ Settings.testProj + "data\\" + fld +  "\\etalon создана.");

		//DM+: 6-->7
		//for(int i = 1 ; i < 6; i++)
		for(int i = 1 ; i < 7; i++)	
		{
			(new File(Settings.testProj + "data\\" + fld + "\\input\\" + String.format("%03d", i))).mkdir();
			(new File(Settings.testProj + "data\\" + fld + "\\etalon\\" + String.format("%03d", i))).mkdir();
		}

	}

	public static void load()
	{
		
		File dir = new File(Settings.testProj + "tests\\");
		String[] children = dir.list();
		String filename = children[children.length - 1];
		
		Settings.folder = filename;
		Settings.fullfolder = Settings.testProj + "tests\\" + Settings.folder + "\\";
		
		Log.create();
		
	}


}
