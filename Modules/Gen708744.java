package Modules;

import resources.Modules.Gen708744Helper;
import ru.sabstest.ED208;
import ru.sabstest.ED244;
import ru.sabstest.GenerateFromED743List;
import ru.sabstest.Pack;
import ru.sabstest.ReadEDList;
import ru.sabstest.Settings;

public class Gen708744 extends Gen708744Helper {

    public void testMain(Object[] args) {
	// List<String> st = Arrays.asList((String[]) args[0]);
	String num = (String) args[1];

	ED208.CodeList.readXML(Settings.testProj + "settings\\generation\\"
		+ num + ".xml");
	ED244.CodeList.readXML(Settings.testProj + "settings\\generation\\"
		+ num + ".xml");

	ReadEDList rl = new ReadEDList();
	// rl.readFolderByType(Settings.path + "post\\kPuO\\", "ED743_VER");
	rl.readFolderByType(Settings.fullfolder + "\\output\\" + num + "\\",
		"ED743_VER");
	GenerateFromED743List pl = new GenerateFromED743List();
	pl.generateFromReadEDList(rl);
	callScript("SABS.CreateSignedXML", new Object[] {
		Settings.path + "post\\kPuI\\", pl });
	Pack.copyESIS(num);
    }
}
