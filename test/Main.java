package test;



import org.w3c.dom.Element;

import ru.sabstest.GenerateFromXMLList;
import ru.sabstest.GenerateList;
import ru.sabstest.Init;
import ru.sabstest.Log;
import ru.sabstest.ReadEDList;
import ru.sabstest.Settings;
import ru.sabstest.XML;




public class Main {
	enum Test {O,ED,READ}

	public static void main(String[] args)
	{
		Settings.testProj = "C:\\sabstest\\";

		Test t = Test.READ;
		switch(t)
		{
					
		case O:
		{

			Init.load();
			Settings.readXML(Settings.testProj + "settings\\general.xml");
			
			
			XML.createXMLFromBase64("C:\\test\\2\\X0010107.401", "C:\\test\\2\\x.xml");
//			PaymentDocumentList pdl = new PaymentDocumentList();
//			pdl.readEncodedFile("C:\\sabstest\\data\\a000002\\input\\003\\458200200020130701000000101.PacketEPDVER", false);
//			
//			ClientList.readFile("C:\\test\\nach\\clients.xml");		
//								
//			PacketList pl = new PacketList();
//			pl.generateFromXML("C:\\test\\gen.xml");
//			
//			pl.createFile("C:\\test\\");

		
			
			break;
		}
		case ED:
		{

			Init.load();
			Settings.readXML(Settings.testProj + "settings\\general.xml");
			GenerateFromXMLList pdl = new GenerateFromXMLList();

			pdl.generateFromXML("C:\\test\\genver.xml");
			

			break;
		}
		case READ:
		{
			ReadEDList pl = new ReadEDList();
			pl.readFolder("C:\\test\\2\\");			

			ReadEDList pl2 = new ReadEDList();
			pl2.readFolder("C:\\test\\3\\");
			
			System.out.println(pl.equals(pl2));
			break;
		}

		}

		Log.close();
	}

}
