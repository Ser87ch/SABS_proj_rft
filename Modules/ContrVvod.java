package Modules;
import resources.Modules.ContrVvodHelper;
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
		//List<String> st = Arrays.asList((String[]) args[0]);
		String num = (String) args[1];
		
		callScript("SABS.VFD",new String[]{Settings.Login.contrvvod.key});
		callScript("SABS.StartSABS",new String[]{Settings.Login.contrvvod.user, Settings.Login.contrvvod.pwd, Settings.Login.contrvvod.sign});
		
		Menutree().click(atName("Контрольный ввод"));
		sleep(1);
		SABSwindow().inputKeys("{ExtDown}");
		sleep(1);
		SABSwindow().inputKeys("{TAB}");
		sleep(1);
		SABSwindow().inputKeys("{TAB}");
		sleep(1);
		SABSwindow().inputKeys("{ExtDown}{Enter}");
				
		PaymentDocumentList pl = new PaymentDocumentList();
		pl.readEncodedFile(Pack.getDocPervVvod(num), false);	
		
		ContrDocwindow().inputKeys("{F2}");	
		
		for(int i = 0; i < pl.size(); i++)
		{
			PaymentDocument pd = pl.get(i);
			Inputwindow().inputKeys(pd.toStrContr("{ENTER}"));	
			Inputwindow().inputKeys("{ENTER}{ENTER}{ENTER}");	
			ControlTextwindow().inputKeys("{ENTER}{ENTER}");
			sleep(1.0);
			ControlTextwindow().inputKeys("{ENTER}");
			
			if(pd.transKind.equals("02") || pd.transKind.equals("06"))
				ControlTextwindow().inputKeys("{ENTER}");
			
			Log.msg("Документ №" + Integer.toString(i + 1) + " проконтролирован в САБС.");
			
		}
		
		Inputwindow().inputKeys("{ESCAPE}");
		ContrDocwindow().inputKeys("%{F4}");
		Menutree().click(atName("Контрольный ввод"));
		
		callScript("SABS.CloseSABS");
	}
}

