package test;
import resources.test.TestHelper;
import ru.sabstest.Init;
import ru.sabstest.Settings;
import ru.sabstest.TestCase;




public class Test extends TestHelper
{
	public void testMain(Object[] args) 
	{
		Settings.testProj = (String) args[0];
		
		
		Init.load();
		Settings.readXML(Settings.testProj + "settings\\general.xml");
		
		TestCase t = new TestCase();
		t.readFile("C:\\test\\test1.xml");
		
		for(int i = 0; i < t.getSize(); i++)
		{
			callScript("SABS.RunTest", new Object[]{t.getStep(i)});
		}
		
		//Settings.readXML("C:\\general.xml");
//		ClientList.readFile("C:\\test\\nach\\clients.xml");
//		Settings.Sign.readXML(Settings.testProj + "settings\\sign.xml");
//		
//		PacketList pl = new PacketList();
//		pl.generateFromXML("C:\\test\\otv\\gen5.xml");
//	
//						
//		callScript("SABS.CreateSignedXML",new Object[]{"C:\\test\\otv\\5\\", pl});
		
		
	}
}

