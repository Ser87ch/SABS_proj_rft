package ru.sabstest;

import java.io.File;
import java.io.FileFilter;
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
	 * @param dest папка, содержимое которой копируется
	 * @param src папка, в которую копируется
	 */
	public static void copyDest(File src, File dest)
	{
		if(src.isDirectory())
		{
			if(!dest.exists())
				dest.mkdir();
			
			String files[] = src.list();
			
			for(String fl:files)
				copyDest(new File(src,fl), new File(dest,fl));
		}
		else
			copyFile(src.getAbsolutePath(), dest.getAbsolutePath());
	}
	
	public static void copy(String src, String dest)
	{
		copyDest(new File(src), new File(dest));
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
			if(fl.getName().length() >= 33)
				copyFile(fl.getAbsolutePath(), Settings.path + "post\\kPuI\\" + fl.getName());
			else
				copyFile(fl.getAbsolutePath(), Settings.path + "post\\kUfI\\" + fl.getName());
		}
	}


	public static void copyFromSABS(String num, boolean isVER)
	{
		if(isVER)
		{
			File[] files = new File(Settings.path + "post\\oPuO\\").listFiles();

			for(File fl:files)
				copyFile(fl.getAbsolutePath(), Settings.fullfolder + "\\output\\" + num + "\\" + fl.getName());
		}
		else
		{
			File[] files = new File(Settings.path + "post\\oUfO\\").listFiles();

			for(File fl:files)
				copyFile(fl.getAbsolutePath(), Settings.fullfolder + "\\output\\" + num + "\\" + fl.getName());
		}
	}

	public static String getSPackPath()
	{
		FileFilter ff = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if(pathname.getName().endsWith("PacketEPDVER"))
					return true;				
				return false;
			}			
		};
		File[] files = new File(Settings.path + "post\\kPuO\\").listFiles(ff);
		return getLastModifiedFile(files);
	}
	
	public static String getLastModifiedFile(File[] files)
	{
		String fl = files[0].getAbsolutePath();
		
		for(int i = 1; i < files.length; i++)
			if(files[i].lastModified() >= files[i - 1].lastModified())
				fl = files[i].getAbsolutePath();
				
		return fl;
	}

	public static File getDocPervVvod(String num)
	{
		File[] files = new File(Settings.datafolder + "input\\" + num).listFiles();
		return files[0];
	}

	public static void copyESIS(String num)
	{
		File[] files = new File(Settings.path + "post\\kPuI\\").listFiles();
		for(File fl:files)
			Pack.copyFile(fl.getAbsolutePath(), Settings.fullfolder + "\\input\\" + num + "\\" + fl.getName());
	}


}
