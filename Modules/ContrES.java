package Modules;
import resources.Modules.ContrESHelper;
import ru.sabstest.Log;

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
		
		Menutree().click(atName("Контроль ЭС "));		
		ESpanel().click(atPoint(45,15));
		
		while(Errorwindow().exists())
		{
			OKerrorbutton().click();			
		}
		
		Menutree().click(atName("Электронные расчеты"));
		Log.msg("ЭС проконтролировано.");	
		
	}
}

