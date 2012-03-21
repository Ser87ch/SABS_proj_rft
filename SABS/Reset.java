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
		
		Log.msg("���������� ���� � ������������.");
		
		Pack.clearFolder(new File(Settings.path + "\\post\\archive"));
		Log.msg("����� " + Settings.path + "\\post\\archive �������.");
		
		Pack.clearFolder(new File(Settings.path + "\\post\\kPuI"));
		Log.msg("����� " + Settings.path + "\\post\\kPuI �������.");
		
		Pack.clearFolder(new File(Settings.path + "\\post\\kPuO"));
		Log.msg("����� " + Settings.path + "\\post\\kPuO �������.");
		
		Pack.clearFolder(new File(Settings.path + "\\post\\oPuO"));
		Log.msg("����� " + Settings.path + "\\post\\oPuO �������.");
		
		Pack.clearFolder(new File(Settings.path + "\\post\\oPuI"));
		Log.msg("����� " + Settings.path + "\\post\\oPuI �������.");
		
		run(Settings.path + "\\bin\\sabs_set.exe",Settings.path + "\\bin");	
		
		SABSSetwindow().waitForExistence(15.0,2.0);
		SABSSetwindow().maximize();
		Menutree().doubleClick(atName("���������� ��"));		
			
		Menutree().doubleClick(atName("�������������� �� �� ��������� ����� ��"));
		
		DBpasswindow().inputKeys("1");
		Passokbutton().click();
		
		Dumptree().click(atName("0402open.bkp"));
		FileOKbutton().click();
		sleep(2);
		Confirmbutton().click();
		
		sleep(60);
		Endwindow().waitForExistence(1200.0,10.0);
		sleep(2);
		��button().click();
				
		SABSSetwindow(ANY,MAY_EXIT).click(CLOSE_BUTTON);
		Log.msg("�� ������������� �� ��������� ����� 0402open.bkp");
		
	}
}

