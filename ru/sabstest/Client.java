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
	
	public String edAuthor;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bic == null) ? 0 : bic.hashCode());
		result = prime * result
				+ ((correspAcc == null) ? 0 : correspAcc.hashCode());
		result = prime * result
				+ ((personalAcc == null) ? 0 : personalAcc.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		if (bic == null) {
			if (other.bic != null)
				return false;
		} else if (!bic.equals(other.bic))
			return false;
		if (correspAcc == null) {
			if (other.correspAcc != null)
				return false;
		} else if (!correspAcc.equals(other.correspAcc))
			return false;
		if (personalAcc == null) {
			if (other.personalAcc != null)
				return false;
		} else if (!personalAcc.equals(other.personalAcc))
			return false;
		return true;
	}

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
	
	public Client(Client cl)
	{
		this.bic = cl.bic;
		this.correspAcc = cl.correspAcc;
		this.personalAcc = cl.personalAcc;
		this.inn = cl.inn;
		this.kpp = cl.kpp;
		this.name = cl.name;
		this.edAuthor = cl.edAuthor;
	}

	/**
	 * Изменение контрольного разряда в счете на правильный
	 */
	public void contrrazr() {
		if(!personalAcc.equals("") && personalAcc != null)
		{
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
	}

	/**
	 * @param doc документ для создания елемента
	 * @param elementName наименование элемента
	 * @return элемент с реквизитами клиента
	 */
	@Deprecated
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
	@Deprecated
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

		Element bank = (Element) cl.getElementsByTagNameNS("*", "Bank").item(0);
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
	@Deprecated
	public static Client createClientFromBICPersonalAcc(Element clEl)
	{
		Client cl = new Client();

		String bicStr, personalAccStr;

		Element bicEl =(Element) clEl.getElementsByTagNameNS("*", "BIC").item(0);
		Element paEl =(Element) clEl.getElementsByTagNameNS("*", "PersonalAcc").item(0);

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

		cl.contrrazr();	

		cl.loadFromDB();


		return cl;
	}

	public void loadFromDB()
	{
		if(bic.equals(Settings.bik) && !personalAcc.equals(""))
		{
			if(personalAcc.substring(0,1).equals("3") || personalAcc.substring(0,1).equals("4"))
			{
				name = DB.selectFirstValueSabsDb("select c.nameshort from dbo.Account a\r\n" + 
						"inner join dbo.Acc_Link al on a.id_acc = al.id_acc and al.id_link_type = 2\r\n" + 
						"inner join dbo.Clientj c on c.id_jur = al.id_obj\r\n" + 
						"where a.NUM_ACC = '"+ personalAcc + "'"); 
				inn = DB.selectFirstValueSabsDb("select c.inn from dbo.Account a\r\n" + 
						"inner join dbo.Acc_Link al on a.id_acc = al.id_acc and al.id_link_type = 2\r\n" + 
						"inner join dbo.Clientj c on c.id_jur = al.id_obj\r\n" + 
						"where a.NUM_ACC = '"+ personalAcc + "'"); 
			}
			else if(personalAcc.substring(0,1).equals("6"))
			{
				name = DB.selectFirstValueSabsDb("select a.name_short from dbo.Account a where a.NUM_ACC = '" + personalAcc + "'");
			}
		}
		else
		{
			correspAcc = DB.selectFirstValueSabsDb("select top 1 isnull(KSNP,'') ksnp from dbo.BNKSEEK where NEWNUM = '" + bic + "'");


			inn = "222222222222";
			kpp = "111111111";
			name = "Тестовый клиент";

		}
	}

	public static Client createClientFromBICPersonalAcc(String bic, String ls, String uic)
	{
		Client cl = new Client(bic, ls);
		cl.edAuthor = uic;
		cl.contrrazr();
		cl.loadFromDB();

		return cl;

	}
	
	
}
