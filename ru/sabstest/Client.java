package ru.sabstest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Client {
	public String bic;
	public String correspAcc;
	public String personalAcc;
	public String inn;
	public String kpp;
	public String name;

	public Client() {
		this.bic = "";	
		this.correspAcc = "";
		this.personalAcc = "";
		this.inn = "";
		this.kpp = "";
		this.name = "";

	}
	
	public Client(String bik, String ls) {
		this.bic = bik;	
		this.correspAcc = "";
		this.personalAcc = ls;
		this.inn = "";
		this.kpp = "";
		this.name = "";

	}

	public Client(String bik, String ks , String ls) {
		this.bic = bik;
		this.correspAcc = ks;
		this.personalAcc = ls;	
		this.inn = "";
		this.kpp = "";
		this.name = "";
	}

	public Client(String bik, String ks , String ls, String inn, String kpp, String name) {
		this.bic = bik;
		this.correspAcc = ks;
		this.personalAcc = ls;
		this.inn = inn;
		this.kpp = kpp;
		this.name = name;
	}

	public void contrrazr() {
		String contrls;

		if(bic.substring(6,9).equals("000") || bic.substring(6,9).equals("001") || bic.substring(6,9).equals("002")) {
			contrls = "0" + bic.substring(4,6) + personalAcc.substring(0, 8) + "0" + personalAcc.substring(9, 20);
		}
		else {
			contrls = bic.substring(6,9) + personalAcc.substring(0, 8) + "0" + personalAcc.substring(9, 20);
		}

		int contr = 0, k;

		for(k = 0; k < 23; k++) {
			switch (k % 3) {
			case 0: contr =  (Character.getNumericValue(contrls.charAt(k)) * 7) % 10 + contr ;
			break;
			case 1: contr =  (Character.getNumericValue(contrls.charAt(k)) * 1) % 10 + contr ;
			break;
			case 2: contr =  (Character.getNumericValue(contrls.charAt(k)) * 3) % 10 + contr ;
			break;
			}

		}

		contr = ((contr % 10) * 3) % 10;

		personalAcc = personalAcc.substring(0, 8) + Integer.toString(contr) + personalAcc.substring(9, 20);

	}

	public Element createXMLElement(Document doc, String elementName)
	{
		Element rootElement = doc.createElement(elementName);

		if(personalAcc != null && !personalAcc.equals(""))
			rootElement.setAttribute("PersonalAcc", personalAcc);

		if(inn != null && !inn.equals(""))
			rootElement.setAttribute("INN", inn);

		if(kpp != null && !kpp.equals(""))
			rootElement.setAttribute("KPP", kpp);
	
		XML.createNode(doc, rootElement, "Name", name);
		
		Element bank = doc.createElement("Bank");
		rootElement.appendChild(bank);
		
		bank.setAttribute("BIC", bic);
		if(correspAcc != null && !correspAcc.equals(""))
			bank.setAttribute("CorrespAcc", correspAcc);
		
		return rootElement;
	}
	
	public void readED(Element cl)
	{
		personalAcc = cl.getAttribute("PersonalAcc");
		inn = cl.getAttribute("INN");
		kpp = cl.getAttribute("KPP");
		
		name = XML.getChildValueString("Name", cl);
		
		Element bank = (Element) cl.getElementsByTagName("Bank").item(0);
		correspAcc = bank.getAttribute("CorrespAcc");
		bic = bank.getAttribute("BIC");		
	}
}
