package Modules;
import java.io.File;

import resources.Modules.GenESISHelper;
import ru.sabstest.GenerateFromPacketEPDList;
import ru.sabstest.Pack;
import ru.sabstest.PacketEPD;
import ru.sabstest.PacketESIDVER;
import ru.sabstest.Settings;

public class GenESIS extends GenESISHelper
{
	
	public void testMain(Object[] args) 
	{
		//List<String> st = Arrays.asList((String[]) args[0]);
		String num = (String) args[1];
		
		PacketESIDVER.CodeList.readXML(Settings.testProj + "settings\\generation\\" + num + ".xml");
		
		PacketEPD pdl = new PacketEPD();
		pdl.readEncodedFile(new File(Pack.getSPackPath()), false);
		
		GenerateFromPacketEPDList pl = new GenerateFromPacketEPDList();
		pl.generateFromPacketEPD(pdl);
		callScript("SABS.CreateSignedXML",new Object[]{Settings.path + "post\\kPuI\\", pl});
		Pack.copyESIS(num);
	}
}

