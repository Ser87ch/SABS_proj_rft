package Modules;

import resources.Modules.GenESISHelper;
import ru.sabstest.GenerateFromPacketEPDList;
import ru.sabstest.Pack;
import ru.sabstest.PacketEPD;
import ru.sabstest.PacketESIDVER;
import ru.sabstest.ReadEDList;
import ru.sabstest.Settings;

public class GenESIS extends GenESISHelper {

    public void testMain(Object[] args) {
	// List<String> st = Arrays.asList((String[]) args[0]);
	String num = (String) args[1];

	PacketESIDVER.CodeList.readXML(Settings.testProj
		+ "settings\\generation\\" + num + ".xml");

	ReadEDList rl = new ReadEDList();

	rl.readFolderByType(Settings.fullfolder + "\\output\\" + num + "\\",
		"PacketEPDVER");

	PacketEPD pdl = (PacketEPD) rl.pList.get(0);

	GenerateFromPacketEPDList pl = new GenerateFromPacketEPDList();
	pl.generateFromPacketEPD(pdl);
	callScript("SABS.CreateSignedXML", new Object[] {
		Settings.path + "post\\kPuI\\", pl });
	Pack.copyESIS(num);
    }
}
