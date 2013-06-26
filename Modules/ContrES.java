package Modules;
import resources.Modules.ContrESHelper;
import ru.sabstest.Log;
import ru.sabstest.Pack;
import ru.sabstest.Settings;
import ru.sabstest.TestCase;

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


public class ContrES extends ContrESHelper
{
	
	public void testMain(Object[] args) 
	{	
		TestCase.Step st = (TestCase.Step) args[0];
		
		if(st.containsOption("CopyToSABS"))
			Pack.copyFile("C:\\test\\otv\\1\\452500000020130701000000101.PacketEPDVER", "C:\\s_zpd\\post\\kPuI\\452500000020130701000000101.PacketEPDVER");
		
		callScript("SABS.VFD",new String[]{Settings.Login.contres.key});
		callScript("SABS.StartSABS",new String[]{Settings.Login.contres.user, Settings.Login.contres.pwd, Settings.Login.contres.sign});
		
		Menutree().click(atName("Контроль ЭС "));		
		
		if(st.containsOption("VERnach"))
			ESpanel().click(atPoint(45,15));
		
		if(st.containsOption("VERotv"))
			ESpanel().click(atPoint(65,15));
		
		while(Errorwindow().exists())
		{
			OKerrorbutton().click();			
		}
		
		Menutree().click(atName("Электронные расчеты"));
		Log.msg("ЭС проконтролировано.");	
		
		callScript("SABS.CloseSABS");
	}
}

