package test;
import resources.test.TestHelper;
import ru.sabstest.PacketList;
import ru.sabstest.Settings;




public class Test extends TestHelper
{
	public void testMain(Object[] args) 
	{
		Settings.testProj = (String) args[0];
		
		Settings.readXML(Settings.testProj + "settings\\general.xml");
		//Settings.readXML("C:\\general.xml");
		//Settings.Sign.readXML(Settings.testProj + "settings\\sign.xml");
		
		PacketList pl = new PacketList();
		pl.generateFromXML("C:\\test\\genver2.xml");
		//pl.createFile("C:\\test\\");
	//	pl.insertIntoDB();
		callScript("SABS.CreateSignedXMLVer",new Object[]{"C:\\test", pl});		
		
	}
}

