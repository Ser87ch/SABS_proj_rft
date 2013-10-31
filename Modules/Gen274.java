package Modules;

import java.util.Arrays;
import java.util.List;

import resources.Modules.Gen274Helper;
import ru.sabstest.ED273;
import ru.sabstest.ED274;
import ru.sabstest.GenerateFromED273List;
import ru.sabstest.GenerateFromXMLList;
import ru.sabstest.Pack;
import ru.sabstest.ReadED;
import ru.sabstest.ReadEDList;
import ru.sabstest.Settings;

public class Gen274 extends Gen274Helper {

    public void testMain(Object[] args) {
	List<String> st = Arrays.asList((String[]) args[0]);
	String num = (String) args[1];

	ED274.ED274CodeList.readXML(Settings.testProj
		+ "settings\\generation\\" + num + ".xml");
	GenerateFromXMLList rl = new GenerateFromXMLList();

	if (!st.contains("Output"))
	    rl.generateFromXML(Settings.testProj + "settings\\generation\\"
		    + num + ".xml");
	else {
	    ReadEDList rel = new ReadEDList();
	    rel.readFolderByType(Settings.fullfolder + "\\output\\" + num
		    + "\\", "ED273");

	    for (ReadED r : rel.pList)
		rl.add((ED273) r);

	}
	GenerateFromED273List pl = new GenerateFromED273List();
	pl.generateFromGenerateFromXML(rl);
	callScript("SABS.CreateSignedXML", new Object[] {
		Settings.path + "post\\kuFI\\", pl });
	Pack.copyESIS(num);

    }
}
