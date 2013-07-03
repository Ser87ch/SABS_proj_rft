package test;
import resources.test.TestRHelper;
import ru.sabstest.PacketESIDVER;
import ru.sabstest.Packet;
import ru.sabstest.PacketList;
import ru.sabstest.PacketEPD;
import ru.sabstest.PacketEPDVER_B;
import ru.sabstest.Settings;


public class TestR extends TestRHelper
{
	
	public void testMain(Object[] args) 
	{
		Settings.testProj = (String) args[0];
		
		Settings.readXML(Settings.testProj + "settings\\general.xml");
		Settings.EsidList.readXML("C:\\test\\nach\\gen5.xml");
		
		Settings.Sign.readXML(Settings.testProj + "settings\\sign.xml");
		
		PacketEPD pdl = new PacketEPD();
		pdl.readEncodedFile("C:\\s_zpd\\post\\kPuO\\458200200020130701000000027.PacketEPDVER", true);
		
		PacketESIDVER cdl = new PacketESIDVER();
		cdl.generateFromPaymentDocumentList(pdl);
		
		PacketEPDVER_B rdl = new PacketEPDVER_B();
		rdl.generateFromPaymentDocumentList(pdl);
		
		PacketList pl = new PacketList();

		pl.add(cdl);
		pl.add(rdl);
		callScript("SABS.CreateSignedXML",new Object[]{"C:\\s_zpd\\post\\kPuI", pl});
	}
}

