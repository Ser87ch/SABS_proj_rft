package PrProvod.PervVvod;
import resources.PrProvod.PervVvod.ObrRpackHelper;
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

public class ObrRpack extends ObrRpackHelper
{

	public void testMain(Object[] args) 
	{
		Menutree().click(atName("ќбработка Ё—"));	
		ESpanel().click(atPoint(65,15));	
		
		while(Errorwindow().exists())
		{
			OKerrorbutton().click();			
		}			
		Log.msg("R-пакет обработан.");
		
		ESpanel().click(atPoint(88,12));
		SABSwindow().inputKeys("{TAB}");
		SABSwindow().inputKeys("{TAB}");
		SABSwindow().inputKeys("{ExtRight}{ExtRight}");			
		
		if(KvitCancelbutton().exists())
		{
			KvitCancelbutton().click();
			OKcancelbutton().click();
		}
		Kvitbutton().click();		
		OKcancelbutton().click();
				
		Menutree().click(atName("Ёлектронные расчеты"));
		Log.msg(" витовка прошла.");
	}
}

