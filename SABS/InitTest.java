package SABS;

import resources.SABS.InitTestHelper;
import ru.sabstest.Init;
import ru.sabstest.Log;
import ru.sabstest.Settings;

import com.rational.test.tss.TSSException;
import com.rational.test.tss.TSSUtility;

public class InitTest extends InitTestHelper {

    public void testMain(Object[] args) {

	try {
	    Settings.testProj = TSSUtility.getScriptOption("projFolder");

	} catch (TSSException e) {
	    e.printStackTrace();
	    Log.msg(e);
	}

	if (Settings.testProj == null)
	    Settings.testProj = (String) args[0];

	Init.mkfolder();
	Log.close();
    }
}
