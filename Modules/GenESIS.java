package Modules;
import java.util.Arrays;
import java.util.List;

import resources.Modules.GenESISHelper;
import ru.sabstest.PacketESIDVER;
import ru.sabstest.Pack;
import ru.sabstest.Packet;
import ru.sabstest.PacketList;
import ru.sabstest.PacketEPD;
import ru.sabstest.PacketEPDVER_B;
import ru.sabstest.Settings;

public class GenESIS extends GenESISHelper
{
	
	public void testMain(Object[] args) 
	{
		//List<String> st = Arrays.asList((String[]) args[0]);
		String num = (String) args[1];
		
		Settings.EsidList.readXML(Settings.testProj + "settings\\generation\\" + num + ".xml");
		
		PacketEPD pdl = new PacketEPD();
		pdl.readEncodedFile(Pack.getSPackPath(), false);
		
		PacketESIDVER cdl = new PacketESIDVER();		
		PacketEPDVER_B rdl = new PacketEPDVER_B();		
		PacketList pl = new PacketList();

		if(cdl.generateFromPaymentDocumentList(pdl))
			pl.add(cdl);
		
		if(	rdl.generateFromPaymentDocumentList(pdl))
			pl.add(rdl);
		callScript("SABS.CreateSignedXML",new Object[]{Settings.path + "post\\kPuI\\", pl});
		Pack.copyESIS(num);
	}
}

