package SABS;
import resources.SABS.RunTestCaseHelper;
import ru.sabstest.ClientList;
import ru.sabstest.DeltaDB;
import ru.sabstest.Init;
import ru.sabstest.Log;
import ru.sabstest.ModuleList;
import ru.sabstest.Settings;
import ru.sabstest.TestCase;

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
import com.rational.test.tss.TSSException;
import com.rational.test.tss.TSSUtility;
import com.ibm.rational.test.ft.object.interfaces.sapwebportal.*;

public class RunTestCase extends RunTestCaseHelper
{

	public void testMain(Object[] args) 
	{
		String num="";
		try {
			Settings.testProj = TSSUtility.getScriptOption("projFolder");
			num = TSSUtility.getScriptOption("num");
		} catch (TSSException e) {
			e.printStackTrace();
			Log.msg(e);			
		}		

		if(Settings.testProj == null)
			Settings.testProj = (String) args[0];


		if(num == null)
			num = (String) args[1];

		String src = Settings.testProj + "settings\\test\\" + num + ".xml";

		Init.load();
		Settings.readXML(Settings.testProj + "settings\\general.xml");
		Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
		ModuleList.readFile(Settings.testProj + "settings\\modules.xml");
		Settings.Sign.readXML(Settings.testProj + "settings\\sign.xml");
		ClientList.readFile(Settings.testProj + "settings\\clients.xml");		
		DeltaDB.readXMLSettings(Settings.testProj + "settings\\deltadb.xml");

		TestCase t = new TestCase();
		t.readFile(src);

		for(int i = 0; i < t.getSize(); i++)
		{
			callScript(t.getScript(i), new Object[]{t.getStep(i).options, num});
		}

	}
}

