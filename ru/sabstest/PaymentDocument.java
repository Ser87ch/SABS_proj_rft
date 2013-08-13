package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ru.sabstest.ED108.TransactionInfo;




/**
 * Абстрактный класс платежный документ
 * @author Admin
 *
 */
abstract public class PaymentDocument implements Comparable<PaymentDocument> {
	//реквизиты ЭД
	public int edNo; //Номер ЭД в течение опердня
	public Date edDate; //Дата составления ЭД
	public String edAuthor; //Уникальный идентификатор составителя ЭД (УИС)

	//основные реквизиты
	public String paytKind; //вид платежа
	public int sum; //сумма
	public String transKind; //вид операции
	public String priority; //очередность платежа
	public Date receiptDate; //дата поступления в банк плательщика
	public Date fileDate; //дата помещения в картотеку
	public Date chargeOffDate; //дата списания со счета плательщика
	public String systemCode; //признак системы обработки (заполняется если ЭД не составе пакета)

	//реквизиты исходного расчетного документа
	public int accDocNo; //номер расчетного документа
	public Date accDocDate; //Дата выписки расчетного документа

	public Client payer; //плательщика
	public Client payee; //получатель

	public String purpose; //назначение платежа

	//Ведомственная информация
	DepartmentalInfo tax;


	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accDocDate == null) ? 0 : accDocDate.hashCode());
		result = prime * result
				+ ((chargeOffDate == null) ? 0 : chargeOffDate.hashCode());
		result = prime * result
				+ ((edAuthor == null) ? 0 : edAuthor.hashCode());
		result = prime * result + ((edDate == null) ? 0 : edDate.hashCode());
		result = prime * result
				+ ((fileDate == null) ? 0 : fileDate.hashCode());
		result = prime * result + ((payee == null) ? 0 : payee.hashCode());
		result = prime * result + ((payer == null) ? 0 : payer.hashCode());
		result = prime * result
				+ ((paytKind == null) ? 0 : paytKind.hashCode());
		result = prime * result
				+ ((priority == null) ? 0 : priority.hashCode());
		result = prime * result + ((purpose == null) ? 0 : purpose.hashCode());
		result = prime * result
				+ ((receiptDate == null) ? 0 : receiptDate.hashCode());
		result = prime * result + sum;
		result = prime * result + ((tax == null) ? 0 : tax.hashCode());
		result = prime * result
				+ ((transKind == null) ? 0 : transKind.hashCode());
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
		PaymentDocument other = (PaymentDocument) obj;
		if (accDocDate == null) {
			if (other.accDocDate != null)
				return false;
		} else if (!accDocDate.equals(other.accDocDate))
			return false;
		if (chargeOffDate == null) {
			if (other.chargeOffDate != null)
				return false;
		} else if (!chargeOffDate.equals(other.chargeOffDate))
			return false;
		if (edAuthor == null) {
			if (other.edAuthor != null)
				return false;
		} else if (!edAuthor.equals(other.edAuthor))
			return false;
		if (edDate == null) {
			if (other.edDate != null)
				return false;
		} else if (!edDate.equals(other.edDate))
			return false;
		if (fileDate == null) {
			if (other.fileDate != null)
				return false;
		} else if (!fileDate.equals(other.fileDate))
			return false;
		if (payee == null) {
			if (other.payee != null)
				return false;
		} else if (!payee.equals(other.payee))
			return false;
		if (payer == null) {
			if (other.payer != null)
				return false;
		} else if (!payer.equals(other.payer))
			return false;
		if (paytKind == null) {
			if (other.paytKind != null)
				return false;
		} else if (!paytKind.equals(other.paytKind))
			return false;
		if (priority == null) {
			if (other.priority != null)
				return false;
		} else if (!priority.equals(other.priority))
			return false;
		if (purpose == null) {
			if (other.purpose != null)
				return false;
		} else if (!purpose.equals(other.purpose))
			return false;
		if (receiptDate == null) {
			if (other.receiptDate != null)
				return false;
		} else if (!receiptDate.equals(other.receiptDate))
			return false;
		if (sum != other.sum)
			return false;
		if (tax == null) {
			if (other.tax != null)
				return false;
		} else if (!tax.equals(other.tax))
			return false;
		if (transKind == null) {
			if (other.transKind != null)
				return false;
		} else if (!transKind.equals(other.transKind))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(PaymentDocument o) {
		if(sum < o.sum)
			return -1;
		else if(sum > o.sum)
			return 1;
		else
			return 0;
	}

	public PaymentDocument()
	{	
		this.accDocNo = 0;
		this.accDocDate = new Date(0);
		this.transKind = "";
		this.sum = 0;				
		//this.paytKind = "1";
		this.priority = "6";
		tax = new DepartmentalInfo("", "", "", "", "", "", "", "");			
		this.purpose = "Тест";
		//this.chargeOffDate = new Date(0);
		//this.receiptDate = new Date(0);	
		this.edNo = 0;

	}

	@Override
	public String toString()
	{
		return toStr(" ", false);
	}


	/**
	 * @param razd разделитель реквизитов
	 * @param addShift добавляется ли Shift
	 * @return строку с реквизитами документа
	 */
	abstract public String toStr(String razd, boolean addShift);


	/**
	 * @param razd разделитель реквизитов
	 * @return строка для контрольного ввода
	 */
	public String toStrContr(String razd){

		String str = "";

		str = Integer.toString(accDocNo) + razd + new SimpleDateFormat("ddMMyyyy").format(accDocDate) + razd + transKind + razd +
		Integer.toString(sum).substring(0, Integer.toString(sum).length() - 2) + "." + 
		Integer.toString(sum).substring(Integer.toString(sum).length() - 2, Integer.toString(sum).length()) + razd + 
		((paytKind.equals("P") || paytKind.equals("T")) ? " +{TAB}{ExtLeft}" : "") +  (paytKind.equals("P") ? "{ExtLeft}" : "") + 
		((paytKind.equals("P") || paytKind.equals("T")) ? razd : "") + razd + 
		payer.bic + razd + payer.correspAcc + razd + payer.personalAcc + razd + payee.bic + razd + payee.correspAcc + razd +
		payee.personalAcc + razd + priority;

		return str;	
	}

	/**
	 * создает элемент с реквизитами документа
	 * @param doc документ
	 * @return элемент
	 */
	@Deprecated
	abstract public Element createED(Document doc);


	/**
	 * считывает реквизиты из элемента
	 * @param doc элемент
	 */
	abstract public void readED(Element doc);

	/**
	 * генерирует документ на основе элемента из xml генерации
	 * @param gendoc элемент
	 * @param edNo номер документа
	 * @param edAuthor УИС
	 */
	abstract public void generateFromXMLByType(Element gendoc);

	public void generateFromXML(Element gendoc, int edNo, String edAuthor, int sum)
	{
		this.edNo = edNo;
		edDate = Settings.operDate;
		//this.edAuthor = edAuthor;
		//paytKind = "1";
		this.sum = sum;
		if(this.sum == 0)
			this.sum = (int) (new Random().nextFloat() * 10000);		

		priority = "6";
		accDocNo = edNo;
		accDocDate = Settings.operDate;

		chargeOffDate = Settings.operDate;
		receiptDate = Settings.operDate;
		fileDate = Settings.operDate;

		payer = ClientList.getClient(gendoc.getAttribute("IdPayer"));
		payee = ClientList.getClient(gendoc.getAttribute("IdPayee"));	

//		if(edAuthor.substring(0,7).equals(Settings.bik.substring(2)))
//			this.edAuthor = edAuthor;
//		else
//			this.edAuthor = payer.bic.substring(2) + "000";

		this.edAuthor = payer.edAuthor;
		
		generateFromXMLByType(gendoc);
		
		String errorCode = gendoc.getAttribute("ErrorCode");
		
		paytKind = gendoc.getAttribute("PaytKind");
		
		if(errorCode != null && !errorCode.equals(""))
			ErrorCode.addError(this, Integer.parseInt(errorCode));
		
		
	}

	/**
	 * вставка данных в БД для создания xml УЭО
	 * @param idPacet ид пакета
	 * @param pEDNo номер пакета
	 * @param pacDate дата пакета
	 * @param pAuthor УИС
	 * @param filename файл
	 */
	abstract public void insertIntoDbUfebs(int idPacet, int pEDNo, Date pacDate, String pAuthor, String filename);

	/**
	 * вставка данных в БД для создания xml ВЭР
	 * @param idPacet ид пакета
	 * @param filename файл
	 */
	abstract public void insertIntoDbVer(int idPacet, String filename);

	/**
	 * создает xml файл с реквизитами документа
	 * @param полный путь к файлу
	 */
	public void createXML(String fl)
	{
		Document doc = XML.createNewDoc();
		doc.appendChild(createED(doc));
		XML.createXMLFile(doc, fl);
	}


	/**
	 * считать реквизиты из XML
	 * @param src путь к файлу
	 */
	public void readXML(String src)
	{
		readED(XML.getXMLRootElement(src));
	}

	/**
	 * добавляются основные реквизиты платежного документа к элементу
	 * @param doc документ
	 * @param rootElement элемент
	 */
	@Deprecated
	public void addCommonEDElements(Document doc, Element rootElement)
	{
		rootElement.setAttribute("xmlns", "urn:cbr-ru:ed:v2.0");
		rootElement.setAttribute("EDNo", Integer.toString(edNo));	
		rootElement.setAttribute("EDDate", new SimpleDateFormat("yyyy-MM-dd").format(edDate));

		rootElement.setAttribute("EDAuthor", edAuthor);
		rootElement.setAttribute("PaytKind", paytKind);
		rootElement.setAttribute("Sum", Integer.toString(sum));
		rootElement.setAttribute("TransKind", transKind);
		rootElement.setAttribute("Priority", priority);

		XML.setOptinalAttr(rootElement, "ReceiptDate", receiptDate);
		XML.setOptinalAttr(rootElement, "FileDate", fileDate);
		XML.setOptinalAttr(rootElement, "ChargeOffDate", chargeOffDate);
		XML.setOptinalAttr(rootElement, "SystemCode", systemCode);		

		Element accDoc = doc.createElement("AccDoc");
		rootElement.appendChild(accDoc);

		accDoc.setAttribute("AccDocNo", Integer.toString(accDocNo));
		accDoc.setAttribute("AccDocDate", new SimpleDateFormat("yyyy-MM-dd").format(accDocDate));		

		rootElement.appendChild(payer.createXMLElement(doc, "Payer"));
		rootElement.appendChild(payee.createXMLElement(doc, "Payee"));

		XML.createNode(doc, rootElement, "Purpose", purpose);
	}

	/**
	 * считываются основные реквизиты платежного документа
	 * @param doc элемент
	 */
	public void readCommonEDElements(Element doc)
	{
		edNo = Integer.parseInt(doc.getAttribute("EDNo"));
		edDate = Date.valueOf(doc.getAttribute("EDDate"));
		edAuthor = doc.getAttribute("EDAuthor");

		paytKind = doc.getAttribute("PaytKind");
		sum = Integer.parseInt(doc.getAttribute("Sum"));
		transKind = doc.getAttribute("TransKind");
		priority = doc.getAttribute("Priority");
		receiptDate = XML.getOptionalDateAttr("ReceiptDate", doc);
		fileDate = XML.getOptionalDateAttr("FileDate", doc);
		chargeOffDate = XML.getOptionalDateAttr("ChargeOffDate", doc);
		systemCode = doc.getAttribute("SystemCode");

		Element accDoc = (Element) doc.getElementsByTagNameNS("*","AccDoc").item(0);			
		accDocNo = Integer.parseInt(accDoc.getAttribute("AccDocNo"));
		accDocDate = Date.valueOf(accDoc.getAttribute("AccDocDate"));

		payer = new Client();
		payer.readED((Element) doc.getElementsByTagNameNS("*","Payer").item(0));
		payee = new Client();
		payee.readED((Element) doc.getElementsByTagNameNS("*","Payee").item(0));

		purpose = XML.getChildValueString("Purpose", doc);
	}

	/**
	 * @param src полный путь к файлу
	 * @return платежный документ считанный из xml
	 */
	public static PaymentDocument createDocFromXML(Element root)
	{
		PaymentDocument pd = null;
		if(root.getLocalName().equals("ED101"))
			pd = new ED101();
		else if(root.getLocalName().equals("ED103"))
			pd = new ED103();
		else if(root.getLocalName().equals("ED113"))
			pd = new ED103(true);		
		else if(root.getLocalName().equals("ED104"))
			pd = new ED104();
		else if(root.getLocalName().equals("ED114"))
			pd = new ED104(true);
		else if(root.getLocalName().equals("ED105"))
			pd = new ED105();
		else if(root.getLocalName().equals("ED108"))
			pd = new ED108();
			
		if(pd != null)
			pd.readED(root);
		return pd;
	}


	public static PaymentDocument createByType(String type)
	{
		PaymentDocument pd = null;

		if(type.equals("101"))
			pd = new ED101();
		else if(type.equals("103"))
			pd = new ED103();
		else if(type.equals("113"))
			pd = new ED103(true);
		else if(type.equals("104"))
			pd = new ED104();
		else if(type.equals("114"))
			pd = new ED104(true);
		else if(type.equals("105"))
			pd = new ED105();
		else if(type.equals("108"))
			pd = new ED108();

		return pd;
	}
}
