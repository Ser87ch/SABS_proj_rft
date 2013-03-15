package ObrProvod.Gen;
import java.io.IOException;

import resources.ObrProvod.Gen.GenSpackHelper;
import ru.sabstest.Init;
import ru.sabstest.Log;
import ru.sabstest.Pack;
import ru.sabstest.PayDocList;
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
import com.rational.test.tss.TSSException;
import com.rational.test.tss.TSSUtility;
import com.ibm.rational.test.ft.object.interfaces.sapwebportal.*;

public class GenSpack extends GenSpackHelper
{

	public void testMain(Object[] args) 
	{
		try {
			Settings.testProj = TSSUtility.getScriptOption("projFolder");		
		} catch (TSSException e) {
			e.printStackTrace();
			Log.msg(e);			
		}
		if(Settings.testProj == null)
			Settings.testProj = (String) args[0];

		Init.load();
		Settings.readXML(Settings.fullfolder + "settings\\general.xml");
		Settings.GenSpack.readXML(Settings.fullfolder + "settings\\" + Settings.obrfolder + "\\genspack.xml");
		
		if(Settings.GenSpack.error.equals("00"))
		{
			PayDocList pl = new PayDocList();
			pl.generateS();		
			pl.createSpack();
		}
		else
		{
			try {
				Pack.copyFile(Settings.testProj + "default\\obr\\serror\\" + Settings.GenSpack.error + "\\spack.txt", Settings.testProj + "tests\\" + Settings.folder + "\\input\\spack.txt");
			} catch (IOException e) {				
				e.printStackTrace();
				Log.msg(e);
			}
		}
		
		Log.close();
	}
}

