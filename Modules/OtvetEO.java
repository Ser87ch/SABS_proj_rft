package Modules;

import java.util.Arrays;
import java.util.List;

import resources.Modules.OtvetEOHelper;
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

	Menutree().click(atName("Ёлектронные расчеты"));
	Log.msg("Ё— создано.");

	callScript("SABS.CloseSABS");

	if (st.contains("CopyFromSABS"))
	    Pack.copyFromSABS(num, false);
    }
}
