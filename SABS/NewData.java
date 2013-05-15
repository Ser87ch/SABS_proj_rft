package SABS;
import resources.SABS.NewDataHelper;
import ru.sabstest.Init;
import ru.sabstest.Log;
import ru.sabstest.PacketList;
import ru.sabstest.PaymentDocumentList;
import ru.sabstest.Settings;

import com.rational.test.tss.TSSException;
import com.rational.test.tss.TSSUtility;


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
		

		PacketList pl = new PacketList();
		pl.generateFromXML(Settings.testProj + "settings\\gen\\generation001.xml");		
		pl.createFile(Settings.datafolder + "input\\001\\paydocs.xml");
		
		PacketList plb = new PacketList();
		plb.generateFromXML(Settings.testProj + "settings\\gen\\generation002.xml");	
		plb.createFile(Settings.datafolder + "input\\002\\paydocs.xml");
		
	
		
		PacketList pls = new PacketList();
		pls.generateFromXML(Settings.testProj + "settings\\gen\\generation003.xml");		
		((PaymentDocumentList) pls.get(0)).createSpack(Settings.datafolder + "input\\003\\spack.txt");	
		
		pls = new PacketList();
		pls.generateFromXML(Settings.testProj + "settings\\gen\\generation004.xml");
		((PaymentDocumentList) pls.get(0)).createSpack(Settings.datafolder + "input\\004\\spack.txt");
		
		pls = new PacketList();
		pls.generateFromXML(Settings.testProj + "settings\\gen\\generation005.xml");
		((PaymentDocumentList) pls.get(0)).createSpack(Settings.datafolder + "input\\005\\spack.txt");
		Log.close();
	}
}

