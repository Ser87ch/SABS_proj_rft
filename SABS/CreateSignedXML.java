package SABS;

import java.util.Iterator;
import java.util.List;

import resources.SABS.CreateSignedXMLHelper;
import ru.sabstest.Generate;
import ru.sabstest.GenerateList;
import ru.sabstest.Settings;
import ru.sabstest.Sign;

public class CreateSignedXML extends CreateSignedXMLHelper {

    public void testMain(Object[] args) {
	String dest = (String) args[0];
	GenerateList<?> plst = (GenerateList<?>) args[1];

	List<List<Generate<?>>> llG = plst.getSubListBySign();

	Iterator<List<Generate<?>>> it = llG.iterator();

	// if(!plst.pList.get(0).isVER)
	while (it.hasNext()) {

	    List<Generate<?>> lg = it.next();
	    Generate<?> pl = lg.get(0);

	    for (Generate<?> gen : lg)
		gen.insertIntoDB();

	    Sign[] sgn = pl.getSigns();
	    String profile = sgn[0].profile;
	    String key = sgn[0].key;
	    String profile2 = sgn[1].profile;
	    String key2 = sgn[1].key;
	    sleep(2);
	    run(Settings.path + "\\bin\\ConvXML.exe", Settings.path + "\\bin");

	    callScript("SABS.VFD", new String[] { key });
	    sleep(2);
	    run(Settings.path + "\\bin\\clienXML.exe -i  My c:\\ 0",
		    Settings.path + "\\bin");

	    selectProfilecomboBox().select(profile);
	    okbutton().click();

	    // if(loadKeywindow().exists())
	    // {
	    // nextbutton().click();
	    // readybutton().click();
	    // }

	    sleep(2);
	    if (!pl.isVER())
		run(Settings.path + "\\bin\\clienXML.exe -wd " + dest
			+ " C:\\  999", Settings.path + "\\bin");
	    else
		run(Settings.path + "\\bin\\clienXML.exe -wdv " + dest
			+ " C:\\  999", Settings.path + "\\bin");

	    for (Generate<?> gen : lg)
		gen.insertForRead();

	    callScript("SABS.VFD", new String[] { key2 });
	    sleep(2);
	    run(Settings.path + "\\bin\\clienXML.exe -i  My c:\\ 0",
		    Settings.path + "\\bin");

	    selectProfilecomboBox().select(profile2);
	    okbutton().click();

	    // if(loadKeywindow().exists())
	    // {
	    // nextbutton().click();
	    // readybutton().click();
	    // }

	    sleep(2);

	    if (!pl.isVER())
		run(Settings.path + "\\bin\\clienXML.exe -kd " + dest
			+ " C:\\  999", Settings.path + "\\bin");
	    else
		run(Settings.path + "\\bin\\clienXML.exe -kdv " + dest
			+ " C:\\  999", Settings.path + "\\bin");
	}
	// else
	// {
	// for(Packet pl:plst.pList)
	// pl.insertIntoDB();
	//
	// String profile = Settings.Sign.signobr;
	// String key = Settings.Sign.keyobr;
	// String profile2 = Settings.Sign.signcontr;
	// String key2 = Settings.Sign.keycontr;
	// sleep(2);
	// run(Settings.path + "\\bin\\ConvXML.exe",Settings.path + "\\bin");
	//
	// callScript("SABS.VFD",new String[]{key});
	// sleep(2);
	// run(Settings.path + "\\bin\\clienXML.exe -i  My c:\\ 0",Settings.path
	// + "\\bin");
	//
	// selectProfilecomboBox().select(profile);
	// okbutton().click();
	//
	// sleep(2);
	//
	// run(Settings.path + "\\bin\\clienXML.exe -wdv " + dest +
	// " C:\\  999",Settings.path + "\\bin");
	//
	// for(Packet pl:plst.pList)
	// pl.insertForRead();
	//
	// callScript("SABS.VFD",new String[]{key2});
	// sleep(2);
	// run(Settings.path + "\\bin\\clienXML.exe -i  My c:\\ 0",Settings.path
	// + "\\bin");
	//
	// selectProfilecomboBox().select(profile2);
	// okbutton().click();
	//
	// run(Settings.path + "\\bin\\clienXML.exe -kdv " + dest +
	// " C:\\  999",Settings.path + "\\bin");
	// }
    }
}
