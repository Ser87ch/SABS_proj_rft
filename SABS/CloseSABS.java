package SABS;

import resources.SABS.CloseSABSHelper;
import ru.sabstest.Log;

public class CloseSABS extends CloseSABSHelper {

    public void testMain(Object[] args) {
	try {

	    if (SABSwindow().exists()) {
		if (SKADerbutton().exists())
		    SKADerbutton().click();

		SABSwindow(ANY, MAY_EXIT).click(CLOSE_BUTTON);
		Closebutton().click();

		if (SKADerbutton().exists())
		    SKADerbutton().click();

		if (SABSwindow().exists()) {
		    SABSwindow(ANY, MAY_EXIT).click(CLOSE_BUTTON);
		    Closebutton().click();
		}

		Log.msg("ÑÀÁÑ çàêðûò.");
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	    Log.msg(e);
	}

    }
}
