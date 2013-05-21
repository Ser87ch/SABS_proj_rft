package test;



import java.lang.System;


import ru.sabstest.*;




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
		case RPACK:
		{
			Init.load();
			Settings.readXML(Settings.testProj + "settings\\general.xml");

			String spack = Pack.copySPack("001");
			//XML.validate("C:\\sabstest\\XMLschema\\output\\deltadb.xsd", "C:\\sabstest\\tests\\a000001\\output\\rpack.xml");
			Settings.Sign.readXML(Settings.testProj + "settings\\genrpack.xml");
			//Pack.createRpackError49();
			//Pack.createBpackError49();
			String s = Pack.getRPackName();
			System.out.println(spack + s);
			break;
		}
		
		case CMP:
		{
			Init.load();
			Settings.readXML(Settings.testProj + "settings\\general.xml");
			System.out.println(Pack.compareSPack(Settings.fullfolder + "etalon\\spack.txt", Settings.fullfolder + "output\\spack.txt"));
			System.out.println(Pack.compareRPack(Settings.fullfolder + "etalon\\rpack.txt", Settings.fullfolder + "output\\rpack.txt"));
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
			XML.createXMLFromBase64("C:\\test\\1s.xml", "C:\\test\\1sde.xml");
			XML.createXMLFromBase64("C:\\test\\1r.xml", "C:\\test\\1rde.xml");
//			PaymentDocumentList pdl = new PaymentDocumentList();
//			pdl.readFile("C:\\test\\de.xml");
			
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
