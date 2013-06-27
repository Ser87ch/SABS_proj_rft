package Modules;
import resources.Modules.FormESHelper;
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


public class FormES extends FormESHelper
{

	public void testMain(Object[] args) 
	{
		TestCase.Step st = (TestCase.Step) args[0];
		//String num = (String) args[1];
		
		callScript("SABS.VFD",new String[]{Settings.Login.formes.key});
		callScript("SABS.StartSABS",new String[]{Settings.Login.formes.user, Settings.Login.formes.pwd, Settings.Login.formes.sign});

		Menutree().click(atName("ќбработка Ё—"));	

		if(st.containsOption("Otv"))
			ESpanel().click(atPoint(65,15));	

		if(st.containsOption("Nach"))
			ESpanel().click(atPoint(45,15));

		while(Errorwindow().exists())
		{
			OKerrorbutton().click();			
		}
		
		if(st.containsOption("Vozvr"))
			;//подумать над реализацией

		Menutree().click(atName("Ёлектронные расчеты"));
		Log.msg("Ё— создано.");
		
		callScript("SABS.CloseSABS");
	}
}

