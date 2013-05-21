package SABS;
import resources.SABS.CreateSignedXMLVerHelper;
import ru.sabstest.DB;
import ru.sabstest.PacketList;
import ru.sabstest.Settings;

public class CreateSignedXMLVer extends CreateSignedXMLVerHelper
{
	
	public void testMain(Object[] args) 
	{
		String profile = (String) args[0];		
		String key = (String) args[1];
		String profile2 = (String) args[2];
		String key2 = (String) args[3];
		String dest = (String) args[4];	
		PacketList pl = (PacketList) args[5];

		pl.insertIntoDB();
		
		sleep(2);
		run(Settings.path + "\\bin\\ConvXML.exe",Settings.path + "\\bin");

		callScript("SABS.VFD",new String[]{key});
		sleep(2);
		run(Settings.path + "\\bin\\clienXML.exe -i  My c:\\ 0",Settings.path + "\\bin");

		selectProfilecomboBox().select(profile);
		okbutton().click();		
//		
//		if(loadKeywindow().exists())
//		{
//			nextbutton().click();
//			readybutton().click();
//		}

		sleep(2);
		run(Settings.path + "\\bin\\clienXML.exe -wdv " + dest + " C:\\  999",Settings.path + "\\bin");
		
//		pl.insertForRead();
//		
//		callScript("SABS.VFD",new String[]{key2});
//		sleep(2);
//		run(Settings.path + "\\bin\\clienXML.exe -i  My c:\\ 0",Settings.path + "\\bin");
//
//		selectProfilecomboBox().select(profile2);
//		okbutton().click();		
////		
////		if(loadKeywindow().exists())
////		{
////			nextbutton().click();
////			readybutton().click();
////		}
//		
//		sleep(2);
//		run(Settings.path + "\\bin\\clienXML.exe -kdv " + dest + " C:\\  999",Settings.path + "\\bin");
	}
}

