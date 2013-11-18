package Modules;

import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Element;

import resources.Modules.OtvetEOHelper;
import ru.sabstest.ED273;
import ru.sabstest.ED274;
import ru.sabstest.ED773_VER;
import ru.sabstest.Generate;
import ru.sabstest.GenerateFromED273List;
import ru.sabstest.GenerateFromXMLList;
import ru.sabstest.Log;
import ru.sabstest.Pack;
import ru.sabstest.Settings;

public class OtvetEO extends OtvetEOHelper {

    public void testMain(Object[] args) {
	List<String> st = Arrays.asList((String[]) args[0]);
	String num = (String) args[1];

	callScript("SABS.VFD", new String[] { Settings.Login.eootvet.key });
	callScript("SABS.StartSABS", new String[] {
		Settings.Login.eootvet.user, Settings.Login.eootvet.pwd,
		Settings.Login.eootvet.sign });

	Menutree().click(atName("ќбработка Ё—", 1));

	if (st.contains("Otv"))
	    ESpanel().click(atPoint(65, 15));

	while (Errorwindow().exists())
	    OKerrorbutton().click();

	if (st.contains("Nach"))
	    ESpanel().click(atPoint(45, 15));

	while (Errorwindow().exists())
	    OKerrorbutton().click();

	if (st.contains("274")) {
	    ED274.ED274CodeList.readXML(Settings.testProj
		    + "settings\\generation\\" + num + ".xml", true);
	    GenerateFromXMLList rl = new GenerateFromXMLList();
	    // ReadEDList rel = new ReadEDList();
	    // rel.readFolderByType(Settings.datafolder + "input\\" + num,
	    // "ED773_VER");
	    //
	    // for (ReadED r : rel.pList)
	    // rl.add(((ED773_VER) r).ed)
	    GenerateFromXMLList rl2 = new GenerateFromXMLList();
	    rl2.generateFromXML(Settings.testProj + "settings\\generation\\"
		    + num + ".xml");

	    for (Generate<Element> g : rl2.pList)
		rl.add(((ED773_VER) g).ed);

	    GenerateFromED273List pl = new GenerateFromED273List();
	    pl.generateFromGenerateFromXML(rl);

	    for (Generate<ED273> g : pl.pList) {
		ESpanel().click(atPoint(400, 13));
		String str = ((ED274) g).toString("{TAB}");
		ED274window().inputKeys(str);
		ED274window().inputKeys("{ENTER}");
	    }
	}

	Menutree().click(atName("Ёлектронные расчеты"));
	Log.msg("Ё— создано.");

	callScript("SABS.CloseSABS");

	if (st.contains("CopyFromSABS"))
	    Pack.copyFromSABS(num, false);
    }
}
