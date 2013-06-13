package test;
import resources.test.TestRHelper;
import ru.sabstest.ConfirmationDocumentList;
import ru.sabstest.Packet;
import ru.sabstest.PacketList;
import ru.sabstest.PaymentDocumentList;
import ru.sabstest.ReturnDocumentList;
import ru.sabstest.Settings;


public class TestR extends TestRHelper
{
	
	public void testMain(Object[] args) 
	{
		Settings.testProj = (String) args[0];
		
		Settings.readXML(Settings.testProj + "settings\\general.xml");
		Settings.EsidList.readXML("C:\\test\\nach\\gen5.xml");
		
		Settings.Sign.readXML(Settings.testProj + "settings\\sign.xml");
		
		PaymentDocumentList pdl = new PaymentDocumentList();
		pdl.readEncodedFile("C:\\s_zpd\\post\\kPuO\\458200200020130701000000027.PacketEPDVER");
		
		ConfirmationDocumentList cdl = new ConfirmationDocumentList();
		cdl.generateFromPaymentDocumentList(pdl);
		
		ReturnDocumentList rdl = new ReturnDocumentList();
		rdl.generateFromPaymentDocumentList(pdl);
		
		PacketList pl = new PacketList();

		pl.add(cdl);
		pl.add(rdl);
		callScript("SABS.CreateSignedXML",new Object[]{"C:\\s_zpd\\post\\kPuI", pl});
	}
}

