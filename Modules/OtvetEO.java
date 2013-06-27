package Modules;
import java.util.Arrays;
import java.util.List;

import resources.Modules.OtvetEOHelper;
import ru.sabstest.Log;
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

public class OtvetEO extends OtvetEOHelper
{

	public void testMain(Object[] args) 
	{
		List<String> st = Arrays.asList((String[]) args[0]);
		//String num = (String) args[1];
		
		callScript("SABS.VFD",new String[]{Settings.Login.eootvet.key});
		callScript("SABS.StartSABS",new String[]{Settings.Login.eootvet.user, Settings.Login.eootvet.pwd, Settings.Login.eootvet.sign});
	
		Menutree().click(atName("ќбработка Ё—", 1));

		if(st.contains("Otv"))
			ESpanel().click(atPoint(65,15));	

		if(st.contains("Nach"))
			ESpanel().click(atPoint(45,15));

		while(Errorwindow().exists())
		{
			OKerrorbutton().click();			
		}

		Menutree().click(atName("Ёлектронные расчеты"));
		Log.msg("Ё— создано.");
		
		callScript("SABS.CloseSABS");
	}
}

