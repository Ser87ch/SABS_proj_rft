package ru.sabstest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ClientList {
    private static Map<String, Client> clList;

    public static void readFile(String src) {
	Element el = XML.getXMLRootElement(src);

	clList = new HashMap<String, Client>();

	NodeList nl = el.getElementsByTagName("Client");
	for (int i = 0; i < nl.getLength(); i++) {
	    String id, bic, ls, uic, profile1, sign1, profile2, sign2;
	    Sign s1, s2;

	    Element clEl = (Element) nl.item(i);

	    id = clEl.getAttribute("Id");
	    bic = clEl.getAttribute("BIC");
	    ls = clEl.getAttribute("LS");
	    uic = clEl.getAttribute("UIC");
	    profile1 = clEl.getAttribute("profile1");
	    sign1 = clEl.getAttribute("key1");
	    profile2 = clEl.getAttribute("profile2");
	    sign2 = clEl.getAttribute("key2");

	    if (profile1.equals("")) {
		s2 = new Sign(Settings.Sign.keycontr, Settings.Sign.signcontr);
		s1 = new Sign(Settings.Sign.keyobr, Settings.Sign.signobr);
	    } else {
		s1 = new Sign(sign1, profile1);
		s2 = new Sign(sign2, profile2);
	    }
	    Client cl = Client.createClientFromBICPersonalAcc(bic, ls, uic, s1,
		    s2);

	    clList.put(id, cl);
	}
    }

    public static Client getClient(String id) {
	return new Client(clList.get(id));
    }

    public static Sign[] getSignByUIC(String uic) {
	Iterator<Client> it = clList.values().iterator();

	while (it.hasNext()) {
	    Client cl = it.next();

	    if (cl.edAuthor.equals(uic)) {
		Sign[] as = { cl.firstSign, cl.secondSign };
		return as;
	    }
	}
	return null;
    }
}
