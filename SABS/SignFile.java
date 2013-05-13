package SABS;
import resources.SABS.SignFileHelper;
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


public class SignFile extends SignFileHelper
{


	public void testMain(Object[] args) 
	{
		String profile = (String) args[0];		
		String source = (String) args[1];
		String dest = (String) args[2];
		sleep(2);
		run("C:\\Program Files\\MDPREI\\spki\\spki1utl.exe -sign -data " + source + " -out " + dest + " -profile " + profile 
				, "C:\\Program Files\\MDPREI\\spki");
		sleep(2);			
//		Nextbutton().click();
//		sleep(2);
//		Readybutton().click();
		Log.msg("Файл " + source + " подписан профилем " + profile + " и сохранен в " + dest);
		
	}
}

	