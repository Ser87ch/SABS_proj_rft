package SABS;

import resources.SABS.SignFileHelper;
import ru.sabstest.Log;

public class SignFile extends SignFileHelper {

    public void testMain(Object[] args) {
	String profile = (String) args[0];
	String source = (String) args[1];
	String dest = (String) args[2];
	sleep(2);
	run("C:\\Program Files\\MDPREI\\spki\\spki1utl.exe -sign -data "
		+ source + " -out " + dest + " -profile " + profile,
		"C:\\Program Files\\MDPREI\\spki");
	sleep(2);
	// Nextbutton().click();
	// sleep(2);
	// Readybutton().click();
	Log.msg("Файл " + source + " подписан профилем " + profile
		+ " и сохранен в " + dest);

    }
}
