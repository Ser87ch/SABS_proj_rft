package test;
import resources.test.TestHelper;
import ru.sabstest.ClientList;
import ru.sabstest.GenerateFromXMLList;
import ru.sabstest.Init;
import ru.sabstest.ModuleList;
import ru.sabstest.Settings;
import ru.sabstest.TestCase;




public class Test extends TestHelper
{
	public void testMain(Object[] args) 
	{
		Settings.testProj = (String) args[0];
		
		
		Init.load();
		Settings.readXML(Settings.testProj + "settings\\general.xml");
		Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
		ModuleList.readFile(Settings.testProj + "settings\\modules.xml");
		ClientList.readFile(Settings.testProj + "settings\\clients.xml");
		Settings.Sign.readXML(Settings.testProj + "settings\\sign.xml");
		
//		TestCase t = new TestCase();
//		t.readFile("C:\\test\\test1.xml");
//		
//		for(int i = 0; i < t.getSize(); i++)
//		{
//			callScript(t.getScript(i), new Object[]{t.getStep(i)});
//		}
		
		//Settings.readXML("C:\\general.xml");
		
		GenerateFromXMLList pl = new GenerateFromXMLList();
		pl.generateFromXML("C:\\sabstest\\settings\\generation\\007.xml");
	
		
		callScript("SABS.CreateSignedXML",new Object[]{"C:\\sabstest\\data\\a000002\\input\\007\\", pl});
		
		
		
	}
}

