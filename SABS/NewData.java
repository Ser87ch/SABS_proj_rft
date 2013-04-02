package SABS;
import resources.SABS.NewDataHelper;
import ru.sabstest.Init;
import ru.sabstest.Log;
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
import com.rational.test.tss.TSSException;
import com.rational.test.tss.TSSUtility;
import com.ibm.rational.test.ft.object.interfaces.sapwebportal.*;


public class NewData extends NewDataHelper
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
		
		Init.mkDataFolder();
		
		Settings.readXML(Settings.testProj + "settings\\general.xml");
		Settings.GenDoc.readXML(Settings.testProj + "settings\\gen\\gendoc.xml");

		PaymentDocumentList pl = new PaymentDocumentList();
		pl.generateFromXML(Settings.testProj + "settings\\gen\\generation001.xml");		
		pl.createEPD(Settings.datafolder + "input\\001\\paydocs.xml");
		
		PaymentDocumentList plb = new PaymentDocumentList();
		plb.generateFromXML(Settings.testProj + "settings\\gen\\generation002.xml");	
		plb.createEPD(Settings.datafolder + "input\\002\\paydocs.xml");
		
		Settings.GenSpack.readXML(Settings.testProj + "settings\\gen\\genspack.xml");
		
		PaymentDocumentList pls = new PaymentDocumentList();
		pls.generateFromXML(Settings.testProj + "settings\\gen\\generation003.xml");		
		pls.createSpack(Settings.datafolder + "input\\003\\spack.txt");	
		
		pls = new PaymentDocumentList();
		pls.generateFromXML(Settings.testProj + "settings\\gen\\generation004.xml");
		pls.createSpack(Settings.datafolder + "input\\004\\spack.txt");
		
		pls = new PaymentDocumentList();
		pls.generateFromXML(Settings.testProj + "settings\\gen\\generation005.xml");
		pls.createSpack(Settings.datafolder + "input\\005\\spack.txt");
		Log.close();
	}
}

