package SABS;
import java.io.File;

import resources.SABS.NewTestCaseDataHelper;
import ru.sabstest.ClientList;
import ru.sabstest.GenerateFromXMLList;
import ru.sabstest.Init;
import ru.sabstest.Log;
import ru.sabstest.ReadEDList;
import ru.sabstest.PacketEPD;
import ru.sabstest.Settings;

import com.rational.test.tss.TSSException;
import com.rational.test.tss.TSSUtility;

public class NewTestCaseData extends NewTestCaseDataHelper
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
		{
			Settings.testProj = (String) args[0];
			num = (String) args[1];
		}
		Init.mkDataFolder(num);

		Settings.readXML(Settings.testProj + "settings\\general.xml");
		Settings.Sign.readXML(Settings.testProj + "settings\\sign.xml");
		ClientList.readFile(Settings.testProj + "settings\\clients.xml");

		File f = new File(Settings.testProj + "settings\\generation\\" + num + ".xml");
		String src = f.getName();			
		GenerateFromXMLList pl = new GenerateFromXMLList();
		pl.generateFromXML(Settings.testProj + "settings\\generation\\" + src);
		new File(Settings.datafolder + "input\\" + src.substring(0,3)).mkdir();
		new File(Settings.datafolder + "etalon\\" + src.substring(0,3)).mkdir();
		callScript("SABS.CreateSignedXML",new Object[]{Settings.datafolder + "input\\" + src.substring(0,3), pl});

		Log.close();
	}
}

