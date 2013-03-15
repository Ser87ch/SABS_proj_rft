package ObrProvod.Vvod;
import resources.ObrProvod.Vvod.ObrSpackHelper;
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

public class ObrSpack extends ObrSpackHelper
{

	public void testMain(Object[] args) 
	{
		Menutree().click(atName("ќбработка Ё—"));	
		ESpanel().click(atPoint(65,15));	
		Log.msg("S-пакет обработан.");
		
		while(Errorwindow().exists())
		{
			OKerrorbutton().click();			
		}			
		
		ESpanel().click(atPoint(45,15));
		Log.msg("R-пакет обработан.");
		
		while(Errorwindow().exists())
		{
			OKerrorbutton().click();			
		}	
		
		Menutree().click(atName("Ёлектронные расчеты"));
		
		
	}
}

