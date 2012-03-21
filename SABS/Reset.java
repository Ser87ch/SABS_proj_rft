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
		
		Log.msg("Подготовка САБС к тестированию.");
		
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
		
		run(Settings.path + "\\bin\\sabs_set.exe",Settings.path + "\\bin");	
		
		SABSSetwindow().waitForExistence(15.0,2.0);
		SABSSetwindow().maximize();
		Menutree().doubleClick(atName("Управление БД"));		
			
		Menutree().doubleClick(atName("Восстановление БД из страховой копии БД"));
		
		DBpasswindow().inputKeys("1");
		Passokbutton().click();
		
		Dumptree().click(atName("0402open.bkp"));
		FileOKbutton().click();
		sleep(2);
		Confirmbutton().click();
		
		sleep(60);
		Endwindow().waitForExistence(1200.0,10.0);
		sleep(2);
		окbutton().click();
				
		SABSSetwindow(ANY,MAY_EXIT).click(CLOSE_BUTTON);
		Log.msg("БД восстановлена из страховой копии 0402open.bkp");
		
	}
}

