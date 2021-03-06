package Modules;

import java.util.Arrays;
import java.util.List;

import resources.Modules.FormESHelper;
import ru.sabstest.Log;
import ru.sabstest.Settings;

public class FormES extends FormESHelper {

    public void testMain(Object[] args) {
	List<String> st = Arrays.asList((String[]) args[0]);
	// String num = (String) args[1];

	callScript("SABS.VFD", new String[] { Settings.Login.formes.key });
	callScript("SABS.StartSABS", new String[] { Settings.Login.formes.user,
		Settings.Login.formes.pwd, Settings.Login.formes.sign });

	Menutree().click(atName("��������� ��"));

	if (st.contains("Otv")) {
	    sleep(1.0);
	    if (OKGUbutton().exists()) {
		callScript("SABS.VFD",
			new String[] { Settings.Login.formesGUkey });
		run(Settings.path + "\\bin\\clienXML.exe -ipv1 "
			+ Settings.Login.formesGUprofile + " 0", Settings.path
			+ "\\bin");

		sleep(1);
		OKGUbutton().click();
		// profilecomboBox().click(ARROW);
		// profilecomboBox().click(atText(Settings.Login.formesGUprofile));
		// okbutton2().click();
	    }
	    ESpanel().click(atPoint(65, 15));

	    sleep(1.0);

	    while (Errorwindow().exists())
		OKerrorbutton().click();
	}

	if (st.contains("Nach")) {
	    ESpanel().click(atPoint(25, 15));

	    sleep(2.0);

	    ESpanel().click(atPoint(45, 15));

	    sleep(2.0);
	    if (OKGUbutton().exists()) {
		callScript("SABS.VFD",
			new String[] { Settings.Login.formesGUkey });
		run(Settings.path + "\\bin\\clienXML.exe -ipv1 "
			+ Settings.Login.formesGUprofile + " 0", Settings.path
			+ "\\bin");

		sleep(1);

		OKGUbutton().click();
		// profilecomboBox().click(ARROW);
		// profilecomboBox().click(atText(Settings.Login.formesGUprofile));
		// okbutton2().click();
	    }

	    sleep(1.0);

	    while (Errorwindow().exists())
		OKerrorbutton().click();
	}

	Menutree().click(atName("����������� �������"));

	if (st.contains("Vozvr")) {
	    Menutree().click(atName("�� ������������ ��"));
	    sleep(2.0);
	    SABSwindow().inputKeys("{TAB}");

	    SABSwindow().inputKeys("{ExtDown}{Num+}{ExtDown}{Num+}{ExtDown}");
	    ESpanel().click(atPoint(87, 6));
	    SABSwindow().inputKeys("{TAB}{TAB}{TAB}{ExtLeft}");

	    while (returnDocbutton(ANY, NO_FLAGS).exists()
		    && returnDocbutton(ANY, NO_FLAGS).isEnabled()) {
		returnDocbutton().click();
		payClientbutton().click();

		Menutree().click(atName("����������� �������"));
		Menutree().click(atName("�� ������������ ��"));
		sleep(2.0);
		SABSwindow().inputKeys("{TAB}");

		SABSwindow().inputKeys(
			"{ExtDown}{Num+}{ExtDown}{Num+}{ExtDown}");
		ESpanel().click(atPoint(87, 6));
		SABSwindow().inputKeys("{TAB}{TAB}{TAB}{ExtLeft}");
		// SABSwindow().inputKeys("{TAB}{TAB}{TAB}{TAB}");
		// SABSwindow().inputKeys("{ExtDown}{Num+}{ExtDown}{Num+}{ExtDown}");
	    }

	    Menutree().click(atName("����������� �������"));
	}

	Log.msg("�� �������.");

	callScript("SABS.CloseSABS");

	// if (st.contains("CopyFromSABS"))
	// Pack.copyFromSABS(num, true);
    }
}
