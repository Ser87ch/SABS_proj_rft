package test;



import java.lang.System;


import ru.sabstest.*;




public class Main {
	enum Test {INIT, GEN, PERVVOD,DDB,RPACK,SPACK,CMP,CMPDELTA,O,ED,VER,DBCONNECT}

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

			PaymentDocumentList pl = new PaymentDocumentList();
			pl.generateFromXML("C:\\generation001.xml");
		
			//pl.createEPD("C:\\epd001.xml");			
			pl.createSpack("C:\\1.txt");
			break;
		}

		case PERVVOD:
		{			
			Init.load();
			Settings.readXML(Settings.testProj + "settings\\general.xml");
			Settings.GenDoc.readXML(Settings.testProj + "settings\\" + Settings.pervfolder + "\\pervvod.xml");
			PaymentDocumentList pl = new PaymentDocumentList();
			pl.readEPD(Settings.testProj + "input\\" + Settings.pervfolder + "\\paydocs.xml");
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
		case SPACK:
		{
			Init.load();
			Settings.readXML(Settings.testProj + "settings\\general.xml");			
	//		Settings.GenSpack.readXML(Settings.testProj + "settings\\" + Settings.obrfolder + "\\genspack.xml");

			PaymentDocumentList pl = new PaymentDocumentList();
			pl.generateFromXML(Settings.testProj + "settings\\gen\\generation001.xml");
			pl.createSpack();

			String s = Pack.getSPackName();
			System.out.println(s);
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
		
			XML.createXMLFromBase64("C:\\vertest.xml", "C:\\qqqtestde.xml");
			
			break;
		}
		case ED:
		{

			Init.load();
			Settings.readXML(Settings.testProj + "settings\\general.xml");
			PaymentDocumentList pdl = new PaymentDocumentList();
			
			pdl.generateFromXML("C:\\gen.xml");
			pdl.createEPD("C:\\qqq.xml");

			break;
		}
		case VER:
		{
			Init.load();
			Settings.readXML("C:\\stend.xml");
			
			PaymentDocumentList pdl = new PaymentDocumentList();
			pdl.generateFromXML("C:\\gen1.xml");
			pdl.createEPD("C:\\1.xml");
			pdl.insertIntoDbVer("1.xml");
			
			pdl.generateFromXML("C:\\gen2.xml");
			pdl.createEPD("C:\\2.xml");
			pdl.insertIntoDbVer("2.xml");
			
			pdl.generateFromXML("C:\\gen3.xml");
			pdl.createEPD("C:\\3.xml");
			pdl.insertIntoDbVer("3.xml");
			break;
		}
		}

		Log.close();
	}

}
