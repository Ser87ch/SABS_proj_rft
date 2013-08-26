package test;
import java.io.File;

import resources.test.TestRHelper;
import ru.sabstest.GenerateFromPacketEPDList;
import ru.sabstest.GenerateList;
import ru.sabstest.PacketEPD;
import ru.sabstest.PacketEPDVER_B;
import ru.sabstest.PacketESIDVER;
import ru.sabstest.ReadEDList;
import ru.sabstest.Settings;


public class TestR extends TestRHelper
{
	
	public void testMain(Object[] args) 
	{
		Settings.testProj = (String) args[0];
		
		Settings.readXML(Settings.testProj + "settings\\general.xml");
		PacketESIDVER.CodeList.readXML("C:\\test\\nach\\gen5.xml");
		
		Settings.Sign.readXML(Settings.testProj + "settings\\sign.xml");
		
		PacketEPD pdl = new PacketEPD();
		pdl.readEncodedFile(new File("C:\\s_zpd\\post\\kPuO\\458200200020130701000000027.PacketEPDVER"), true);
		
		GenerateFromPacketEPDList pl = new GenerateFromPacketEPDList();
		
		pl.generateFromPacketEPD(pdl);
		
		callScript("SABS.CreateSignedXML",new Object[]{"C:\\s_zpd\\post\\kPuI", pl});
	}
}

