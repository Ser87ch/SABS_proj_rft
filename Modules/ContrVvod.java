package Modules;

import resources.Modules.ContrVvodHelper;
import ru.sabstest.Log;
import ru.sabstest.Pack;
import ru.sabstest.PacketEPD;
import ru.sabstest.PaymentDocument;
import ru.sabstest.Settings;

public class ContrVvod extends ContrVvodHelper {

    public void testMain(Object[] args) {
	// List<String> st = Arrays.asList((String[]) args[0]);
	String num = (String) args[1];

	callScript("SABS.VFD", new String[] { Settings.Login.contrvvod.key });
	callScript("SABS.StartSABS", new String[] {
		Settings.Login.contrvvod.user, Settings.Login.contrvvod.pwd,
		Settings.Login.contrvvod.sign });

	Menutree().click(atName("Контрольный ввод"));
	sleep(1);
	SABSwindow().inputKeys("{ExtDown}");
	sleep(1);
	SABSwindow().inputKeys("{TAB}");
	sleep(1);
	SABSwindow().inputKeys("{TAB}");
	sleep(1);
	SABSwindow().inputKeys("{ExtDown}{Enter}");

	PacketEPD pl = new PacketEPD();
	pl.readEncodedFile(Pack.getDocPervVvod(num), false);

	ContrDocwindow().inputKeys("{F2}");

	for (int i = 0; i < pl.size(); i++) {
	    PaymentDocument pd = pl.get(i);
	    Inputwindow().inputKeys(pd.toStrContr("{ENTER}"));
	    Inputwindow().inputKeys("{ENTER}{ENTER}{ENTER}");
	    ControlTextwindow().inputKeys("{ENTER}{ENTER}");
	    sleep(1.0);
	    ControlTextwindow().inputKeys("{ENTER}");

	    if (pd.transKind.equals("02") || pd.transKind.equals("06"))
		ControlTextwindow().inputKeys("{ENTER}");

	    Log.msg("Документ №" + Integer.toString(i + 1)
		    + " проконтролирован в САБС.");

	}

	Inputwindow().inputKeys("{ESCAPE}");
	ContrDocwindow().inputKeys("%{F4}");
	Menutree().click(atName("Контрольный ввод"));

	callScript("SABS.CloseSABS");
    }
}
