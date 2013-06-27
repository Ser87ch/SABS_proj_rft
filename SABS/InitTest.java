package SABS;
import resources.SABS.InitTestHelper;
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

import com.rational.test.tss.*;

public class InitTest extends InitTestHelper
{

	public void testMain(Object[] args) 
	{	
		
		try {
			Settings.testProj = TSSUtility.getScriptOption("projFolder");
			
		} catch (TSSException e) {
			e.printStackTrace();
			Log.msg(e);			
		}		
		
		if(Settings.testProj == null)
			Settings.testProj = (String) args[0];	
		
		Init.mkfolder();		
		Log.close();
	}
}

