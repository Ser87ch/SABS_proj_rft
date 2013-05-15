package PrProvod.PervVvod;
import resources.PrProvod.PervVvod.ContrVvodHelper;
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
import ru.sabstest.*;

public class ContrVvod extends ContrVvodHelper
{
	
	public void testMain(Object[] args) 
	{
		Menutree().click(atName("Контрольный ввод"));
		SABSwindow().inputKeys("{ExtDown}");
		sleep(1);
		SABSwindow().inputKeys("{TAB}");
		sleep(1);
		SABSwindow().inputKeys("{TAB}");
		sleep(1);
		SABSwindow().inputKeys("{ExtDown}{Enter}");
				
		PaymentDocumentList pl = (PaymentDocumentList) args[0]; 	
		
		ContrDocwindow().inputKeys("{F2}");	
		
		for(int i = 0; i < pl.size(); i++)
		{
			PaymentDocument pd = pl.get(i);
			Inputwindow().inputKeys(pd.toStrContr("{ENTER}"));	
			Inputwindow().inputKeys("{ENTER}{ENTER}{ENTER}");	
			ControlTextwindow().inputKeys("{ENTER}{ENTER}{ENTER}");
			sleep(1);
			//ControlTextwindow().inputKeys("{ENTER}");
			Log.msg("Документ №" + Integer.toString(i + 1) + " проконтролирован в САБС.");
			
		}
		
		Inputwindow().inputKeys("{ESCAPE}");
		ContrDocwindow().inputKeys("%{F4}");
		Menutree().click(atName("Контрольный ввод"));
	}
}

