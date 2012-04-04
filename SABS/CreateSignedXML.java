package SABS;
import resources.SABS.CreateSignedXMLHelper;
import ru.sabstest.PaymentDocumentList;
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

public class CreateSignedXML extends CreateSignedXMLHelper
{

	public void testMain(Object[] args) 
	{
		String profile = (String) args[0];			
		String dest = (String) args[1];				

		sleep(2);
		run(Settings.path + "\\bin\\ConvXML.exe",Settings.path + "\\bin");

		sleep(2);
		run(Settings.path + "\\bin\\clienXML.exe -i  My c:\\ 0",Settings.path + "\\bin");

		selectProfilecomboBox().click(atText(profile));
		okbutton().click();		
		
		if(loadKeywindow().exists())
		{
			nextbutton().click();
			readybutton().click();
		}

		sleep(2);
		run(Settings.path + "\\bin\\clienXML.exe -wd " + dest + " C:\\  999",Settings.path + "\\bin");
	}
}
