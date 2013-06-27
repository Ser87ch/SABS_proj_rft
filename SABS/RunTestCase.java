package SABS;
import resources.SABS.RunTestCaseHelper;
import ru.sabstest.ClientList;
import ru.sabstest.Init;
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
import com.ibm.rational.test.ft.object.interfaces.sapwebportal.*;

public class RunTestCase extends RunTestCaseHelper
{

	public void testMain(Object[] args) 
	{
		Settings.testProj = (String) args[0];
		String src = "C:\\sabstest\\settings\\test\\" + (String) args[1] + ".xml";

		Init.load();
		Settings.readXML(Settings.testProj + "settings\\general.xml");
		Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
		ModuleList.readFile(Settings.testProj + "settings\\modules.xml");
		ClientList.readFile(Settings.testProj + "settings\\clients.xml");
		Settings.Sign.readXML(Settings.testProj + "settings\\sign.xml");

		TestCase t = new TestCase();
		t.readFile(src);

		for(int i = 0; i < t.getSize(); i++)
		{
			callScript(t.getScript(i), new Object[]{t.getStep(i).options, (String) args[1]});
		}

	}
}

