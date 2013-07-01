package ru.sabstest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;


/**
 * класс для работы с пакетами
 * @author Admin
 *
 */
public class Pack {


	/**
	 * копирует файл
	 * @param sourcestr откуда копируется
	 * @param deststr куда копируется
	 * @throws IOException
	 */
	public static void copyFile(String sourcestr, String deststr)
	{
		FileChannel source = null;
		FileChannel destination = null;
		try {
			File sourceFile = new File(sourcestr);
			File destFile = new File(deststr);
			destFile.createNewFile();


			if(!destFile.exists()) {
				destFile.createNewFile();
			}	    


			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
			Log.msg("Файл " + sourcestr + " скопирован в " + deststr + " .");
		} 
		catch(Exception e)
		{
			e.printStackTrace();
			Log.msg(e);
		}
		finally {
			try {
				if(source != null) {
					source.close();
				}
				if(destination != null) {
					destination.close();
				}
			} 
			catch(Exception e)
			{
				e.printStackTrace();
				Log.msg(e);
			}
		}
	}	


	/**
	 * удаление содержимого папки
	 * @param fld папка
	 */
	public static void clearFolder(File fld)
	{
		File[] files = fld.listFiles();
		if(files!=null) 
		{ 
			for(File f: files) 
			{
				if(f.isDirectory())
				{
					clearFolder(f);
				} else {
					f.delete();
				}
			}
		}
	}
	
	public static void copyToSABS(String num)
	{
		File[] files = new File(Settings.datafolder + "input\\" + num).listFiles();
		
		for(File fl:files)
		{
			if(fl.getName().length() == 40)
				copyFile(fl.getAbsolutePath(), Settings.path + "post\\kPuI\\" + fl.getName());
			else
				copyFile(fl.getAbsolutePath(), Settings.path + "post\\kUfI\\" + fl.getName());
		}
	}

	
	public static void copyFromSABS(String num)
	{
		File[] files = new File(Settings.path + "post\\kPuO\\").listFiles();
		
		for(File fl:files)
			copyFile(fl.getAbsolutePath(), Settings.fullfolder + "\\output\\" + num + "\\" + fl.getName());
		
		files = new File(Settings.path + "post\\kUfO\\").listFiles();
		
		for(File fl:files)
			copyFile(fl.getAbsolutePath(), Settings.fullfolder + "\\output\\" + num + "\\" + fl.getName());
	}
	
	public static String getSPackPath()
	{
		File[] files = new File(Settings.path + "post\\kPuO\\").listFiles();
		return files[0].getAbsolutePath();
	}
	
	public static String getDocPervVvod(String num)
	{
		File[] files = new File(Settings.datafolder + "input\\" + num).listFiles();
		return files[0].getAbsolutePath();
	}
	
	public static void copyESIS(String num)
	{
		File[] files = new File(Settings.path + "post\\kPuI\\").listFiles();
		Pack.copyFile(files[0].getAbsolutePath(), Settings.fullfolder + "\\input\\" + num + "\\" + files[0].getName());
	}
	
	
}
