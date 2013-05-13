package SABS;
import java.io.File;

import resources.SABS.ResetHelper;
import ru.sabstest.Log;
import ru.sabstest.Pack;
import ru.sabstest.Settings;

import com.rational.test.ft.*;
import com.rational.test.ft.object.interfaces.*;
import com.rational.test.ft.object.interfaces.SAP.*;
import com.rational.test.ft.object.interfaces.WPF.*;
import com.rational.test.ft.object.interfaces.dojo.*;
import com.rational.test.ft.object.interfaces.siebel.*;
import com.rational.test.ft.object.interfaces.flex.*;
import com.rational.test.ft.object.interfaces.generichtmlsubdomain.*;
import com.rational.test.ft.script.*;
import com.rational.test.ft.value.*;
import com.rational.test.ft.vp.*;
import com.ibm.rational.test.ft.object.interfaces.sapwebportal.*;

public class Reset extends ResetHelper
{
	
	public void testMain(Object[] args) 
	{
		if(Settings.path == "")
			Settings.path = "C:\\sabs_zapd\\";
		
		//DM+
		if(Settings.dumpname == "")
			Settings.dumpname = "0402open_eo0.bkp";
		
		//DM+
		Log.msg("---------Подготовка САБС к тестированию.---------");
		
		Pack.clearFolder(new File(Settings.path + "\\post\\archive"));
		Log.msg("Папка " + Settings.path + "\\post\\archive очищена.");
		
		Pack.clearFolder(new File(Settings.path + "\\post\\kPuI"));
		Log.msg("Папка " + Settings.path + "\\post\\kPuI очищена.");
		
		Pack.clearFolder(new File(Settings.path + "\\post\\kPuO"));
		Log.msg("Папка " + Settings.path + "\\post\\kPuO очищена.");
		
		Pack.clearFolder(new File(Settings.path + "\\post\\oPuO"));
		Log.msg("Папка " + Settings.path + "\\post\\oPuO очищена.");
		
		Pack.clearFolder(new File(Settings.path + "\\post\\oPuI"));
		Log.msg("Папка " + Settings.path + "\\post\\oPuI очищена.");
		

		//DM+ папки ЭО
		// контролер
		Pack.clearFolder(new File(Settings.path + "\\post\\kUfI"));
		Log.msg("Папка " + Settings.path + "\\post\\kPuI очищена.");
		
		Pack.clearFolder(new File(Settings.path + "\\post\\kUfO"));
		Log.msg("Папка " + Settings.path + "\\post\\kPuO очищена.");
		
		// ответисполнитель
		Pack.clearFolder(new File(Settings.path + "\\post\\oUfI"));
		Log.msg("Папка " + Settings.path + "\\post\\oPuO очищена.");
		
		Pack.clearFolder(new File(Settings.path + "\\post\\oUfO"));
		Log.msg("Папка " + Settings.path + "\\post\\oPuI очищена.");
		
		//"дискеты"
		Pack.clearFolder(new File(Settings.path + "\\post\\ufebs\\in"));
		Log.msg("Папка " + Settings.path + "\\post\\ufebs\\in очищена.");
		
		Pack.clearFolder(new File(Settings.path + "\\post\\ufebs\\out"));
		Log.msg("Папка " + Settings.path + "\\post\\ufebs\\out очищена.");
		
		
		run(Settings.path + "\\bin\\sabs_set.exe",Settings.path + "\\bin");	
		
		SABSSetwindow().waitForExistence(15.0,2.0);
		SABSSetwindow().maximize();
		sleep(2);
		Menutree().doubleClick(atName("Управление БД"));		
		sleep(2);
		Menutree().doubleClick(atName("Восстановление БД из страховой копии БД"));
		
		DBpasswindow().inputKeys("1");
		Passokbutton().click();
		
		//DM+
		//Dumptree().click(atName("0402open.bkp"));
		//Dumptree().click(atName("0402open_eo0.bkp"));
		//
		//String dmpnm = "0402open_eo0.bkp";
		//Dumptree().click(atName(dmpnm));
		//
		Dumptree().click(atName(Settings.dumpname));

		
		FileOKbutton().click();
		sleep(2);
		Confirmbutton().click();
		
		sleep(60);
		Endwindow().waitForExistence(1200.0,10.0);
		sleep(2);
		окbutton().click();
				
		SABSSetwindow(ANY,MAY_EXIT).click(CLOSE_BUTTON);
		
		//DM
		Log.msg("БД восстановлена из СК " + Settings.dumpname);
		
	}
}

