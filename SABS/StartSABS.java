package SABS;

import resources.SABS.StartSABSHelper;
import ru.sabstest.Log;
import ru.sabstest.Settings;

public class StartSABS extends StartSABSHelper {

    public void testMain(Object[] args) {
	try {
	    String user = (String) args[0];
	    String pwd = (String) args[1];
	    String sign = (String) args[2];

	    run(Settings.path + "\\bin\\purs_loader.exe", Settings.path
		    + "\\bin");

	    Log.msg("Запускается САБС. Логин: " + user + ".  Профиль: " + sign
		    + ".");

	    sleep(3);

	    доступКСистемеwindow()
		    .inputKeys(user + "{ENTER}" + pwd + "{ENTER}");
	    sleep(1);

	    Log.msg("Пароль введен.");
	    logTestResult("Login", true);

	    даbutton().click();

	    sleep(1);

	    comboBoxcomboBox().click(ARROW);

	    comboBoxcomboBox().click(atText(sign));

	    sleep(1);
	    okbutton2().click();

	    Log.msg("САБС запущен.");

	} catch (Exception e) {
	    e.printStackTrace();
	    Log.msg(e);
	}
    }
}
