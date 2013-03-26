package test;



import java.lang.System;


import ru.sabstest.*;




public class Main {
	enum Test {INIT, GEN, PERVVOD,DDB,RPACK,SPACK,CMP,CMPDELTA,O,ED}

	public static void main(String[] args)
	{
		Settings.testProj = "C:\\sabstest\\";

		Test t = Test.ED;
		switch(t)
		{


		case INIT:
		{
			Init.mkfolder();	

			break;
		}
		case GEN:
		{			
			Init.load();
			Settings.readXML(Settings.testProj + "settings\\general.xml");
			Settings.GenDoc.readXML(Settings.testProj + "settings\\gendoc.xml");

			PaymentDocumentList pl = new PaymentDocumentList();
			pl.generate();
			//System.out.println(pl.toString());
			pl.createXML();
			break;
		}

		case PERVVOD:
		{			
			Init.load();
			Settings.readXML(Settings.testProj + "settings\\general.xml");
			Settings.GenDoc.readXML(Settings.testProj + "settings\\" + Settings.pervfolder + "\\pervvod.xml");
			PaymentDocumentList pl = new PaymentDocumentList();
			pl.readXML(Settings.testProj + "input\\" + Settings.pervfolder + "\\paydocs.xml");
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
			break;
		}
		case SPACK:
		{
			Init.load();
			Settings.readXML(Settings.testProj + "settings\\general.xml");			
			Settings.GenSpack.readXML(Settings.testProj + "settings\\" + Settings.obrfolder + "\\genspack.xml");

			PaymentDocumentList pl = new PaymentDocumentList();
			pl.generateS();
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
			Init.load();
			Settings.readXML(Settings.testProj + "settings\\general.xml");

			Client cl = new Client("044552989","40116810100000000037");
			cl.contrrazr();
			System.out.println(cl.personalAcc);


			break;
		}
		case ED:
		{

//			PaymentDocumentList pdl = new PaymentDocumentList();
//			pdl.readEPD("C:\\PacketEPD.xml");
//			
//			pdl.createEPD("C:\\3.xml");
			
			CollectionOrder pq = new CollectionOrder();
			
			pq.readXML("C:\\ED104.xml");
			
			pq.createXML("C:\\4.xml");
		}
		}


		Log.close();



	}

}
