package test;



import ru.sabstest.Init;
import ru.sabstest.Log;
import ru.sabstest.PacketList;
import ru.sabstest.PaymentDocumentList;
import ru.sabstest.Settings;




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
			
			
	//		XML.createXMLFromBase64("C:\\sabstest\\data\\a000002\\input\\003\\458200200020130701000000101.PacketEPDVER", "C:\\sabstest\\data\\a000001\\input\\003\\ver.xml");
			PaymentDocumentList pdl = new PaymentDocumentList();
			pdl.readEncodedFile("C:\\sabstest\\data\\a000002\\input\\003\\458200200020130701000000101.PacketEPDVER", false);
//			
//			ClientList.readFile("C:\\test\\nach\\clients.xml");		
//								
//			PacketList pl = new PacketList();
//			pl.generateFromXML("C:\\test\\gen.xml");
//			
//			pl.createFile("C:\\test\\");

		
			pdl.get(26).toStr("{ENTER}", true);
			break;
		}
		case ED:
		{

			Init.load();
			Settings.readXML(Settings.testProj + "settings\\general.xml");
			PacketList pdl = new PacketList();

			pdl.generateFromXML("C:\\test\\genver.xml");
			pdl.createFile("C:\\test\\");

			break;
		}
		case READ:
		{
			PacketList pl = new PacketList();
			pl.readFolder("C:\\sabstest\\tests\\a000010\\output\\004\\");			
			
			break;
		}

		}

		Log.close();
	}

}
