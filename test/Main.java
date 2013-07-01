package test;



import ru.sabstest.Client;
import ru.sabstest.ClientList;
import ru.sabstest.DB;
import ru.sabstest.DeltaDB;
import ru.sabstest.Init;
import ru.sabstest.Log;
import ru.sabstest.ModuleList;
import ru.sabstest.Pack;
import ru.sabstest.PacketList;
import ru.sabstest.PaymentDocumentList;
import ru.sabstest.Settings;
import ru.sabstest.XML;




public class Main {
	enum Test {INIT, GEN, PERVVOD,DDB,RPACK,CMP,CMPDELTA,O,ED,DBCONNECT}

	public static void main(String[] args)
	{
		Settings.testProj = "C:\\sabstest\\";

		Test t = Test.O;
		switch(t)
		{

		case DBCONNECT:
		{

			DB db = new DB(	"SER-ADA508913EF\\ATLANT:1033", "sabs_zapd", "sa", "1");			

			try 
			{
				db.connect();

				db.close();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}

			break;
		}
		case INIT:
		{
			Init.mkfolder();	

			break;
		}
		case GEN:
		{			
			Init.load();
			Settings.readXML(Settings.testProj + "settings\\general.xml");

			PacketList pl = new PacketList();
			pl.generateFromXML("C:\\generation001.xml");

			pl.createFile("C:\\epd001.xml");			
			//pl.createSpack("C:\\1.txt");
			break;
		}

		case PERVVOD:
		{			
			Init.load();
			Settings.readXML(Settings.testProj + "settings\\general.xml");
			Settings.GenDoc.readXML(Settings.testProj + "settings\\" + Settings.pervfolder + "\\pervvod.xml");
			PaymentDocumentList pl = new PaymentDocumentList();
			pl.readFile(Settings.testProj + "input\\" + Settings.pervfolder + "\\paydocs.xml");
			//System.out.println(pl.toString());
			System.out.println(pl.get(0).toStr("{ENTER}",true));
			break;
		}
		case DDB:
		{
			Init.load();
			Settings.readXML(Settings.testProj + "settings\\general.xml");
			DeltaDB.readXMLSettings(Settings.testProj + "settings\\deltadb.xml");
			//			DeltaDB.createDBLog();
			//			DeltaDB.createXML("vvod.xml");		
			DeltaDB.deleteDBLog();
			break;
		}
		
		case CMPDELTA:
		{
			Init.load();
			Settings.readXML(Settings.testProj + "settings\\general.xml");
			System.out.println(DeltaDB.cmpDeltaDB("C:\\sabstest\\tests\\a000018\\etalon\\rpack.xml", "C:\\sabstest\\tests\\a000018\\output\\rpack.xml"));
			break;
		}				
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

		}

		Log.close();
	}

}
