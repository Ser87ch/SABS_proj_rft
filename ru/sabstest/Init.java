package ru.sabstest;

import java.io.File;
import java.text.DecimalFormat;


/**
 * Класс для инициализации теста
 * @author Admin
 *
 */
public class Init {
	
	/**
	 * создает папки для нового теста
	 */
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

					
			File[] files = new File(Settings.testProj + "settings\\test\\").listFiles();

			for(File f:files)
			{
				String src = f.getName();			
						
				(new File(Settings.fullfolder + "\\input\\" + src.substring(0,3))).mkdir();
				(new File(Settings.fullfolder + "\\output\\" + src.substring(0,3))).mkdir();
			}

		} catch(Exception e) {
			e.printStackTrace();
			Log.msg(e);
		}

	}

	private static String getNewDataFolderName()
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
		return fld;
	}
	
	/**
	 * создает папки для новых данных
	 */
	public static void mkDataFolder()
	{		
		String  fld = getNewDataFolderName();
		
		Settings.datafolder = Settings.testProj + "data\\" + fld + "\\";

		(new File(Settings.testProj + "data\\" + fld)).mkdir();
		(new File(Settings.testProj + "data\\" + fld + "\\input")).mkdir();
		(new File(Settings.testProj + "data\\" + fld + "\\etalon")).mkdir();
		Log.createGen();
		Log.msg("Папка для теста новых данных " + Settings.testProj + "data\\" + fld + " создана.");
		Log.msg("Папка входящих данных для теста " + Settings.testProj + "data\\" + fld +  "\\input создана.");		
		Log.msg("Папка эталонных данных для теста "+ Settings.testProj + "data\\" + fld +  "\\etalon создана.");

	}
	
	/**
	 * копирует предыдущие данные, кроме номер теста
	 * @param num номер теста
	 */
	public static void mkDataFolder(String num)
	{
		String  fld = getNewDataFolderName();
		
		Settings.datafolder = Settings.testProj + "data\\" + fld + "\\";
		
		File dir = new File(Settings.testProj + "data\\");
		String[] children = dir.list();
		if (children == null || children.length == 0)
			return;
	
		String filename = Settings.testProj + "data\\" + children[children.length - 1] + "\\";
		
		Pack.copy(filename, Settings.datafolder);		
		
		Pack.clearFolder(new File(Settings.datafolder + "\\etalon\\" + num));
		Pack.clearFolder(new File(Settings.datafolder + "\\input\\" + num));
		new File(Settings.datafolder + "log.txt").delete();
		Log.createGen();
		
	}

	/**
	 * находит папку с последним тестом, создает лог файл в ней
	 */
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
