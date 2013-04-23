package SABS;
import resources.SABS.VFDHelper;
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

public class VFD extends VFDHelper
{
	
	public void testMain(Object[] args) 
	{
		int nWait1 = 4, nWait2 = 2 ;
	
		
		sleep(nWait1);
		run("c:\\vfd\\vfd.exe close b:","c:\\vfd\\");
		
		sleep(nWait1);
		run("c:\\vfd\\vfd.exe open b: c:\\vfd\\" + (String) args[0],"c:\\vfd\\");
		
	    sleep(nWait2);
	    run("c:\\vfd\\vfd.exe open b: c:\\vfd\\" + (String) args[0],"c:\\vfd\\");
	    
	    Log.msg("Ключ " + (String) args[0] + " загружен.");
	}
}

