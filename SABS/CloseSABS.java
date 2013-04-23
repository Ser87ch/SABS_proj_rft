package SABS;
import resources.SABS.CloseSABSHelper;
import ru.sabstest.*;

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

public class CloseSABS extends CloseSABSHelper
{

	public void testMain(Object[] args) 
	{
		try{
			
			
			SABSwindow(ANY,MAY_EXIT).click(CLOSE_BUTTON);
			Closebutton().click();
			
			if(SKADerbutton().exists())
				SKADerbutton().click();
			
			if(SABSwindow().exists())
			{
				SABSwindow(ANY,MAY_EXIT).click(CLOSE_BUTTON);
				Closebutton().click();
			}
			
				
			
			Log.msg("ÑÀÁÑ çàêðûò.");
			
		} catch(Exception e) {
			e.printStackTrace();
			Log.msg(e);
		}

	}
}

