package Modules;

import java.util.Arrays;
import java.util.List;

import resources.Modules.ContrESHelper;
import ru.sabstest.Log;
import ru.sabstest.Pack;
import ru.sabstest.Settings;

public class ContrES extends ContrESHelper {

    public void testMain(Object[] args) {
	List<String> st = Arrays.asList((String[]) args[0]);
	String num = (String) args[1];

	if (st.contains("CopyToSABS"))
	    Pack.copyToSABS(num);

	callScript("SABS.VFD", new String[] { Settings.Login.contres.key });
	callScript("SABS.StartSABS", new String[] {
		Settings.Login.contres.user, Settings.Login.contres.pwd,
		Settings.Login.contres.sign });

	if (st.contains("VERnach") || st.contains("VERotv")) {
	    Menutree().click(atName(" онтроль Ё— "));

	    callScript("SABS.VFD", new String[] { Settings.Login.contresGUkey });
	    OKGUbutton().click();

	    profilecomboBox().click(ARROW);
	    profilecomboBox().click(atText(Settings.Login.contresGUprofile));
	    okbutton().click();
	    if (st.contains("VERnach")) {
		ESpanel().click(atPoint(45, 15));
		while (Errorwindow().exists())
		    OKerrorbutton().click();
	    }

	    if (st.contains("VERotv")) {
		ESpanel().click(atPoint(65, 15));
		while (Errorwindow().exists())
		    OKerrorbutton().click();
	    }
	    sleep(2.0);
	    Menutree().click(atName("Ёлектронные расчеты"));
	}

	if (st.contains("UFEBSnach") || st.contains("UFEBSotv")) {
	    Menutree().click(atName(" онтроль Ё— (”‘ЁЅ—)"));

	    if (st.contains("UFEBSnach")) {
		ESpanel().click(atPoint(45, 15));
		while (Errorwindow().exists())
		    OKerrorbutton().click();
	    }

	    if (st.contains("UFEBSotv")) {
		ESpanel().click(atPoint(65, 15));
		while (Errorwindow().exists())
		    OKerrorbutton().click();
	    }
	    sleep(2.0);
	    Menutree().click(atName("Ёлектронные расчеты"));
	}

	Log.msg("Ё— проконтролировано.");

	callScript("SABS.CloseSABS");
    }
}
