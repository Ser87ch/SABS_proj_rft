package Modules;
import java.util.Arrays;
import java.util.List;

import resources.Modules.GenESISHelper;
import ru.sabstest.ConfirmationDocumentList;
import ru.sabstest.Pack;
import ru.sabstest.Packet;
import ru.sabstest.PacketList;
import ru.sabstest.PaymentDocumentList;
import ru.sabstest.ReturnDocumentList;
import ru.sabstest.Settings;

public class GenESIS extends GenESISHelper
{
	
	public void testMain(Object[] args) 
	{
		//List<String> st = Arrays.asList((String[]) args[0]);
		String num = (String) args[1];
		
		Settings.EsidList.readXML(Settings.testProj + "settings\\generation\\" + num + ".xml");
		
		PaymentDocumentList pdl = new PaymentDocumentList();
		pdl.readEncodedFile(Pack.getSPackPath());
		
		ConfirmationDocumentList cdl = new ConfirmationDocumentList();		
		ReturnDocumentList rdl = new ReturnDocumentList();		
		PacketList pl = new PacketList();

		if(cdl.generateFromPaymentDocumentList(pdl))
			pl.add(cdl);
		
		if(	rdl.generateFromPaymentDocumentList(pdl))
			pl.add(rdl);
		callScript("SABS.CreateSignedXML",new Object[]{Settings.path + "post\\kPuI\\", pl});
		Pack.copyESIS(num);
	}
}

