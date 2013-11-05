package SABS;

import java.io.File;

import resources.SABS.RunTestCaseHelper;
import ru.sabstest.ClientList;
import ru.sabstest.DeltaDB;
import ru.sabstest.Init;
import ru.sabstest.Log;
import ru.sabstest.ModuleList;
import ru.sabstest.Pack;
import ru.sabstest.Settings;
import ru.sabstest.TestCase;

import com.rational.test.tss.TSSException;
import com.rational.test.tss.TSSUtility;

public class RunTestCase extends RunTestCaseHelper {

    public void testMain(Object[] args) {
	String num = "";
	try {
	    Settings.testProj = TSSUtility.getScriptOption("projFolder");
	    num = TSSUtility.getScriptOption("num");
	} catch (TSSException e) {
	    e.printStackTrace();
	    Log.msg(e);
	}

	if (Settings.testProj == null)
	    Settings.testProj = (String) args[0];

	if (num == null)
	    num = (String) args[1];

	String src = Settings.testProj + "settings\\test\\" + num + ".xml";

	Init.load();
	Settings.readXML(Settings.testProj + "settings\\general.xml");
	Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
	ModuleList.readFile(Settings.testProj + "settings\\modules.xml");
	Settings.Sign.readXML(Settings.testProj + "settings\\sign.xml");
	ClientList.readFile(Settings.testProj + "settings\\clients.xml");
	DeltaDB.readXMLSettings(Settings.testProj + "settings\\deltadb.xml");

	Pack.clearFolder(new File(Settings.path + "\\post\\kPuO"));
	Pack.clearFolder(new File(Settings.path + "\\post\\kUfO"));

	TestCase t = new TestCase();
	t.readFile(src);

	try {
	    for (int i = 0; i < t.getSize(); i++)
		callScript(t.getScript(i), new Object[] { t.getStep(i).options,
			num });
	} catch (Exception e) {
	    callScript("SABS.CloseSABS");
	    e.printStackTrace();
	    Log.msg(e);
	}

    }
}
