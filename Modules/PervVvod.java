package Modules;

import resources.Modules.PervVvodHelper;
import ru.sabstest.GenerateFromXMLList;
import ru.sabstest.Log;
import ru.sabstest.PacketEPD;
import ru.sabstest.PaymentDocument;
import ru.sabstest.Settings;

public class PervVvod extends PervVvodHelper {

    public void testMain(Object[] args) {

	// List<String> st = Arrays.asList((String[]) args[0]);
	String num = (String) args[1];

	callScript("SABS.VFD", new String[] { Settings.Login.pervvod.key });

	callScript("SABS.StartSABS", new String[] {
		Settings.Login.pervvod.user, Settings.Login.pervvod.pwd,
		Settings.Login.pervvod.sign });

	SABSwindow().waitForExistence();

	Menutree().click(atName("Ввод документов"));
	SABSwindow().inputKeys("{TAB}");
	SABSwindow().inputKeys("{ExtDown}{ExtDown}{ENTER}");

	GenerateFromXMLList rl = new GenerateFromXMLList();
	rl.generateFromXML(Settings.testProj + "settings\\generation\\" + num
		+ ".xml");
	PacketEPD pl = (PacketEPD) rl.pList.get(0);// new PacketEPD();
	// pl.readEncodedFile(Pack.getDocPervVvod(num), false);

	Docswindow().inputKeys("{F2}");

	for (int i = 0; i < pl.size(); i++) {
	    PaymentDocument pd = pl.get(i);
	    String s = pd.toStr("{ENTER}", true);
	    Inputwindow().inputKeys(s);

	    // Inputwindow().inputKeys("{ENTER}{ENTER}{ENTER}");

	    // if(ExistDocwindow().exists())
	    // {
	    // ExistDocOkbutton().click();
	    // Log.msg("Похожий документ уже есть в системе.");
	    // }

	    if (Errorwindow().exists())
		ErrorOkbutton().click();

	    ProvodNextbutton().click();
	    Log.msg("Документ №" + Integer.toString(i + 1) + " введен в САБС.");
	}
	Inputwindow().inputKeys("{ESCAPE}");
	Docswindow().inputKeys("{TAB}{ExtDown}");

	for (int i = 0; i < pl.size(); i++)
	    Docswindow().inputKeys("+{ExtDown}");

	Docswindow().inputKeys("{F7}");

	Folderwindow()
		.inputKeys(
			"{ExtDown}{ExtDown}{ExtDown}{ExtDown}{ExtDown}{ExtDown}{ExtDown}{ExtDown}");

	PrintcheckBox().click();
	FolderOkbutton().click();
	ExistDocOkbutton().click();

	Log.msg("Пачка создана.");

	Docswindow().inputKeys("%{F4}");
	Menutree().click(atName("Первичный ввод документов"));

	callScript("SABS.CloseSABS");
    }
}
