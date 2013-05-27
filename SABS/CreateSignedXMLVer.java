package SABS;
import resources.SABS.CreateSignedXMLVerHelper;
import ru.sabstest.DB;
import ru.sabstest.Packet;
import ru.sabstest.PacketList;
import ru.sabstest.Settings;

public class CreateSignedXMLVer extends CreateSignedXMLVerHelper
{

	public void testMain(Object[] args) 
	{

		String dest = (String) args[0];	
		PacketList plst = (PacketList) args[1];


		for(Packet pl:plst.pList)
		{
			String profile = pl.firstSign.profile;		
			String key = pl.firstSign.key;
			String profile2 = pl.secondSign.profile;
			String key2 = pl.secondSign.key;

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

			pl.insertForRead();

			callScript("SABS.VFD",new String[]{key2});
			sleep(2);
			run(Settings.path + "\\bin\\clienXML.exe -i  My c:\\ 0",Settings.path + "\\bin");

			selectProfilecomboBox().select(profile2);
			okbutton().click();		
			//		
			//		if(loadKeywindow().exists())
			//		{
			//			nextbutton().click();
			//			readybutton().click();
			//		}

			sleep(2);
			run(Settings.path + "\\bin\\clienXML.exe -kdv " + dest + " C:\\  999",Settings.path + "\\bin");
		}
	}
}

