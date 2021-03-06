package SABS;

import java.io.File;

import resources.SABS.NewDataHelper;
import ru.sabstest.ClientList;
import ru.sabstest.GenerateFromXMLList;
import ru.sabstest.Init;
import ru.sabstest.Log;
import ru.sabstest.Settings;

import com.rational.test.tss.TSSException;
import com.rational.test.tss.TSSUtility;

public class NewData extends NewDataHelper {

    public void testMain(Object[] args) {
	try {
	    Settings.testProj = TSSUtility.getScriptOption("projFolder");

	} catch (TSSException e) {
	    e.printStackTrace();
	    Log.msg(e);
	}

	if (Settings.testProj == null)
	    Settings.testProj = (String) args[0];

	Init.mkDataFolder();

	Settings.readXML(Settings.testProj + "settings\\general.xml");
	Settings.Sign.readXML(Settings.testProj + "settings\\sign.xml");
	ClientList.readFile(Settings.testProj + "settings\\clients.xml");

	File[] files = new File(Settings.testProj + "settings\\generation\\")
		.listFiles();

	for (File f : files) {
	    String src = f.getName();
	    GenerateFromXMLList pl = new GenerateFromXMLList();
	    pl.generateFromXML(Settings.testProj + "settings\\generation\\"
		    + src);
	    new File(Settings.datafolder + "input\\" + src.substring(0, 3))
		    .mkdir();
	    new File(Settings.datafolder + "etalon\\" + src.substring(0, 3))
		    .mkdir();
	    callScript("SABS.CreateSignedXML", new Object[] {
		    Settings.datafolder + "input\\" + src.substring(0, 3), pl });
	}
	Log.close();
    }
}
