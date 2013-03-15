package PrProvod.Gen;
import resources.PrProvod.Gen.GenDocHelper;
import ru.sabstest.*;

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

public class GenDoc extends GenDocHelper
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
		Settings.GenDoc.readXML(Settings.fullfolder + "settings\\" + Settings.pervfolder + "\\gendoc.xml");

		PayDocList pl = new PayDocList();
		pl.generate();
		//System.out.println(pl.toString());
		pl.createXML();
		Log.close();
	}
}

