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
			
			//DM+ \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
			/*
			SABSwindow(ANY,MAY_EXIT).click(CLOSE_BUTTON);
			Closebutton().click();
			
			if(SKADerbutton().exists())
				SKADerbutton().click();
			
			if(SABSwindow().exists())
			{
				SABSwindow(ANY,MAY_EXIT).click(CLOSE_BUTTON);
				Closebutton().click();
			}
			*/	
		
			// Window: purs_loader.exe: 044582002 œ” ¡¿Õ ¿ –Œ——»» «¿œ¿ƒÕŒ≈
			SABSwindow(ANY,MAY_EXIT).click(CLOSE_BUTTON);
		
			// Window: purs_loader.exe: ¬ÓÔÓÒ
			//Closebutton(ANY,MAY_EXIT).click(atPoint(47,13));
			Closebutton(ANY,MAY_EXIT).click();
			//DM+ /////////////////////////////////////			
				
			
			Log.msg("—¿¡— Á‡Í˚Ú.");
			
		} catch(Exception e) {
			e.printStackTrace();
			Log.msg(e);
		}

	}
}

