package SABS;
import resources.SABS.CreateSignedXMLVerHelper;
import ru.sabstest.DB;
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

public class CreateSignedXMLVer extends CreateSignedXMLVerHelper
{
	
	public void testMain(Object[] args) 
	{
		String profile = (String) args[0];		
		String key = (String) args[1];
		String profile2 = (String) args[2];
		String key2 = (String) args[3];
		String dest = (String) args[4];	
		String flName = (String) args[5];

		sleep(2);
		run(Settings.path + "\\bin\\ConvXML.exe",Settings.path + "\\bin");

		callScript("SABS.VFD",new String[]{key});
		sleep(2);
		run(Settings.path + "\\bin\\clienXML.exe -i  My c:\\ 0",Settings.path + "\\bin");

		selectProfilecomboBox().select(profile);
		okbutton().click();		
//		
//		if(loadKeywindow().exists())
//		{
//			nextbutton().click();
//			readybutton().click();
//		}

		sleep(2);
		run(Settings.path + "\\bin\\clienXML.exe -wdv " + dest + " C:\\  999",Settings.path + "\\bin");
		
		DB.insertPacetForReadVer(flName);
		
		callScript("SABS.VFD",new String[]{key2});
		sleep(2);
		run(Settings.path + "\\bin\\clienXML.exe -i  My c:\\ 0",Settings.path + "\\bin");

		selectProfilecomboBox().select(profile2);
		okbutton().click();		
//		
//		if(loadKeywindow().exists())
//		{
//			nextbutton().click();
//			readybutton().click();
//		}
		
		sleep(2);
		run(Settings.path + "\\bin\\clienXML.exe -kdv " + dest + " C:\\  999",Settings.path + "\\bin");
	}
}

