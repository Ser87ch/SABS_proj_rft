package Modules;

import resources.Modules.Gen708774Helper;
import ru.sabstest.ED274;
import ru.sabstest.GenerateFromED773List;
import ru.sabstest.Pack;
import ru.sabstest.ReadEDList;
import ru.sabstest.Settings;

public class Gen708774 extends Gen708774Helper {

    public void testMain(Object[] args) {
	// List<String> st = Arrays.asList((String[]) args[0]);
	String num = (String) args[1];

	ED274.ED274CodeList.readXML(Settings.testProj
		+ "settings\\generation\\" + num + ".xml");

	ReadEDList rl = new ReadEDList();
	rl.readFolderByType(Settings.path + "post\\kPuO\\", "ED773_VER");

	GenerateFromED773List pl = new GenerateFromED773List();
	pl.generateFromReadEDList(rl);
	callScript("SABS.CreateSignedXML", new Object[] {
		Settings.path + "post\\kPuI\\", pl });
	Pack.copyESIS(num);
    }
}
