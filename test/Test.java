package test;
import resources.test.TestHelper;
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
		Settings.Sign.readXML(Settings.testProj + "settings\\sign.xml");
		
		PaymentDocumentList pdl = new PaymentDocumentList();
		pdl.generateFromXML(Settings.testProj + "settings\\gen\\generation001.xml");
		pdl.createEPD("C:\\test1.xml");
		pdl.insertIntoDB("testtest.xml");
		
		callScript("SABS.VFD",new String[]{Settings.Sign.keyobr});
		
		callScript("SABS.CreateSignedXML",new String[]{Settings.Sign.signobr, "C:\\"});
		
	}
}

