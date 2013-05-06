package test;
import resources.test.TestHelper;
import ru.sabstest.DB;
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
import com.ibm.rational.test.ft.object.interfaces.sapwebportal.*;


public class Test extends TestHelper
{
	public void testMain(Object[] args) 
	{
		Settings.testProj = (String) args[0];
		Settings.readXML(Settings.testProj + "settings\\general.xml");
		//Settings.readXML("C:\\general.xml");
		Settings.Sign.readXML(Settings.testProj + "settings\\sign.xml");
		
		PaymentDocumentList pdl = new PaymentDocumentList();
		pdl.generateFromXML("C:\\genver.xml");
		pdl.createEPD("C:\\ver.xml");
		//pdl.insertIntoDbUfebs("vertest.xml");
		pdl.insertIntoDbVer("vertest.xml");
			
		//callScript("SABS.CreateSignedXML",new String[]{"OPER_OK_401", "oper_ok_401.fdd", "CONTR_OK_401", "contr_ok_401.fdd", "C:\\"});
		callScript("SABS.CreateSignedXMLVer",new String[]{"OTVETPU", "otvetpu.fdd", "CONTRPU", "contrpu.fdd", "C:\\"});
		
		
		
	}
}

