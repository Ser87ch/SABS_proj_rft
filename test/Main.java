package test;

import ru.sabstest.GenerateFromXMLList;
import ru.sabstest.Init;
import ru.sabstest.Log;
import ru.sabstest.Settings;
import ru.sabstest.XML;

public class Main {
    enum Test {
	O, ED, READ
    }

    public static void main(String[] args) {
	Settings.testProj = "C:\\sabstest\\";

	Test t = Test.O;
	switch (t) {

	case O: {

	    Init.load();
	    Settings.readXML(Settings.testProj + "settings\\general.xml");

	    XML.createXMLFromBase64(
		    "C:\\test\\458200200020130701000001072.ED743_VER",
		    "C:\\test\\1.txt");
	    // XML.createXMLFromBase64("C:\\test\\1\\003700200020130701000000040.ED743_VER",
	    // "C:\\test\\1\\743.xml");
	    // XML.createXMLFromBase64("C:\\test\\1\\458200200020130701000000023.ED708_VER",
	    // "C:\\test\\1\\708.xml");
	    // PaymentDocumentList pdl = new PaymentDocumentList();
	    // pdl.readEncodedFile("C:\\sabstest\\data\\a000002\\input\\003\\458200200020130701000000101.PacketEPDVER",
	    // false);
	    //
	    // ClientList.readFile("C:\\test\\nach\\clients.xml");
	    //
	    // PacketList pl = new PacketList();
	    // pl.generateFromXML("C:\\test\\gen.xml");
	    //
	    // pl.createFile("C:\\test\\");

	    break;
	}
	case ED: {

	    Init.load();
	    Settings.readXML(Settings.testProj + "settings\\general.xml");
	    GenerateFromXMLList pdl = new GenerateFromXMLList();

	    pdl.generateFromXML("C:\\test\\genver.xml");

	    break;
	}
	case READ: {
	    Init.mkDataFolder("008");
	    // ReadEDList pl = new ReadEDList();
	    // pl.readFolder("C:\\test\\1\\");
	    //
	    // out.println(pl.toString());

	    break;
	}

	}

	Log.close();
    }

}
