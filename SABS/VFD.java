package SABS;

import resources.SABS.VFDHelper;
import ru.sabstest.Log;

public class VFD extends VFDHelper {

    public void testMain(Object[] args) {
	int nWait1 = 4, nWait2 = 2;

	sleep(nWait1);
	run("c:\\vfd\\vfd.exe close b:", "c:\\vfd\\");

	sleep(nWait1);
	run("c:\\vfd\\vfd.exe open b: c:\\vfd\\" + (String) args[0],
		"c:\\vfd\\");

	sleep(nWait2);
	run("c:\\vfd\\vfd.exe open b: c:\\vfd\\" + (String) args[0],
		"c:\\vfd\\");

	Log.msg("Ключ " + (String) args[0] + " загружен.");
    }
}
