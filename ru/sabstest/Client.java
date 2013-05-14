package ru.sabstest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Класс Клиент
 * @author Admin
 */
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

	/**
	 * Изменение контрольного разряда в счете на правильный
	 */
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

	/**
	 * @param doc документ для создания елемента
	 * @param elementName наименование элемента
	 * @return элемент с реквизитами клиента
	 */
	public Element createXMLElement(Document doc, String elementName)
	{
		Element rootElement = doc.createElement(elementName);

		XML.setOptinalAttr(rootElement, "PersonalAcc", personalAcc);
		XML.setOptinalAttr(rootElement, "INN", inn);
		XML.setOptinalAttr(rootElement, "KPP", kpp);
		XML.createNode(doc, rootElement, "Name", name);

		Element bank = doc.createElement("Bank");
		rootElement.appendChild(bank);

		bank.setAttribute("BIC", bic);
		XML.setOptinalAttr(bank, "CorrespAcc", correspAcc);
		return rootElement;
	}
	
	/**
	 * @param doc документ для создания елемента
	 * @param elementName наименование элемента
	 * @return элемент с краткими реквизитами клиента
	 */
	public Element createXMLShortElement(Document doc, String elementName)
	{
		Element rootElement = doc.createElement(elementName);

		XML.setOptinalAttr(rootElement, "PersonalAcc", personalAcc);	

		Element bank = doc.createElement("Bank");
		rootElement.appendChild(bank);

		bank.setAttribute("BIC", bic);
		XML.setOptinalAttr(bank, "CorrespAcc", correspAcc);
		return rootElement;
	}
	
	/**
	 * @param cl элемент с реквизитами клиента
	 * считывает реквизиты клиента из элемента
	 */
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

	/**
	 * Создает нового клиента. @ - текущий БИК. 
	 * В случае если в данных находится sql запрос, он выполняется и в значения БИК подставляется либо первое значение, либо указанное в row, в счет подставляется первое значение.
	 * Если БИК текущий, реквизиты клиента берутся из базы.
	 * @param clEl элемент с данными для генерации реквизитов клиентов
	 * @return нового клиента	 * 
	 */
	public static Client createClientFromBICPersonalAcc(Element clEl)
	{
		Client cl = new Client();

		String bicStr, personalAccStr;
		
		Element bicEl =(Element) clEl.getElementsByTagName("BIC").item(0);
		Element paEl =(Element) clEl.getElementsByTagName("PersonalAcc").item(0);
		
		bicStr = bicEl.getFirstChild().getNodeValue();
		personalAccStr = paEl.getFirstChild().getNodeValue();
		
		String bic = bicStr.replaceAll("@", Settings.bik);
		String personalAcc = personalAccStr.replaceAll("@", Settings.bik);

		if(bic.substring(0, 6).equals("select"))
		{
			int row;
			row = XML.getOptionalIntAttr("row", bicEl);
			cl.bic = DB.selectValueSabsDb(bic,row);
		}
		else 
			cl.bic = bic;

		if(personalAcc.substring(0, 6).equals("select"))
		{
			int row;
			row = XML.getOptionalIntAttr("row", paEl);
			cl.personalAcc = DB.selectValueSabsDb(personalAcc,row);
		}
		else
			cl.personalAcc = personalAcc;

		if(cl.bic.equals(Settings.bik) && !cl.personalAcc.equals(""))
		{
			if(cl.personalAcc.substring(0,1).equals("3") || cl.personalAcc.substring(0,1).equals("4"))
			{
				cl.name = DB.selectFirstValueSabsDb("select c.nameshort from dbo.Account a\r\n" + 
						"inner join dbo.Acc_Link al on a.id_acc = al.id_acc and al.id_link_type = 2\r\n" + 
						"inner join dbo.Clientj c on c.id_jur = al.id_obj\r\n" + 
						"where a.NUM_ACC = '"+ cl.personalAcc + "'"); 
				cl.inn = DB.selectFirstValueSabsDb("select c.inn from dbo.Account a\r\n" + 
						"inner join dbo.Acc_Link al on a.id_acc = al.id_acc and al.id_link_type = 2\r\n" + 
						"inner join dbo.Clientj c on c.id_jur = al.id_obj\r\n" + 
						"where a.NUM_ACC = '"+ cl.personalAcc + "'"); 
			}
			else if(cl.personalAcc.substring(0,1).equals("6"))
			{
				cl.name = DB.selectFirstValueSabsDb("select a.name_short from dbo.Account a where a.NUM_ACC = '" + cl.personalAcc + "'");
			}
		}
		else
		{
			cl.correspAcc = DB.selectFirstValueSabsDb("select top 1 isnull(KSNP,'') ksnp from dbo.BNKSEEK where NEWNUM = '" + cl.bic + "'");

			if(!cl.personalAcc.equals(""))
			{
				cl.inn = "222222222222";
				cl.kpp = "111111111";
				cl.name = "Тестовый клиент";
			}
		}
		
		cl.contrrazr();		
		return cl;
	}
}
