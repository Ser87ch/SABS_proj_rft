package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;




abstract public class PaymentDocument {
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


	public PaymentDocument()
	{	
		this.accDocNo = 0;
		this.accDocDate = new Date(0);
		this.transKind = "";
		this.sum = 0;				
		this.paytKind = "1";
		this.priority = "6";
		tax = new DepartmentalInfo("", "", "", "", "", "", "", "");			
		this.purpose = "Тест";
		this.chargeOffDate = new Date(0);
		this.receiptDate = new Date(0);	
		this.edNo = 0;
	}

	@Override
	public String toString()
	{
		return toStr(" ", false);
	}


	public String toStr(String razd, boolean addShift){
		String str = "";

		str = Integer.toString(accDocNo) + razd + new SimpleDateFormat("ddMMyyyy").format(accDocDate) + razd + transKind + razd + Float.toString(sum) + razd + paytKind.toString() + razd + 
		payer.bic + razd + payer.correspAcc + razd + payer.personalAcc + razd + payer.inn + razd + payer.kpp + razd + payer.name + razd + payee.bic + razd + payee.correspAcc + razd + payee.personalAcc + razd + payee.inn + razd + payee.kpp + razd 
		+ (addShift ? "+{ExtEnd}" : "") + payee.name + razd +
		priority + razd + tax.drawerStatus;
		if(tax.drawerStatus != "" && tax.drawerStatus != null)
			str = str + razd + tax.cbc + razd + tax.okato + razd + tax.paytReason + razd + tax.taxPeriod + razd + tax.docNo + razd + tax.docDate + razd + tax.taxPaytKind;

		str = str + razd + purpose + razd + new SimpleDateFormat("ddMMyyyy").format(chargeOffDate) + razd + new SimpleDateFormat("ddMMyyyy").format(receiptDate);
		return str;	
	}

	public String toStrContr(String razd){

		String str = "";

		str = Integer.toString(accDocNo) + razd + new SimpleDateFormat("ddMMyyyy").format(accDocDate) + razd + transKind + razd + Float.toString(sum) + razd + paytKind.toString() + razd + 
		payer.bic + razd + payer.correspAcc + razd + payer.personalAcc + razd + payee.bic + razd + payee.correspAcc + razd + payee.personalAcc + razd + priority;

		return str;	
	}

	abstract public Element createED(Document doc);


	abstract public void readED(Element doc);


	public void createXML(String fl)
	{
		Document doc = XML.createNewDoc();
		doc.appendChild(createED(doc));
		XML.createXMLFile(doc, fl);
	}

	public void readXML(String src)
	{
		readED(XML.getXMLRootElement(src));
	}

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

		Element accDoc = (Element) doc.getElementsByTagName("AccDoc").item(0);			
		accDocNo = Integer.parseInt(accDoc.getAttribute("AccDocNo"));
		accDocDate = Date.valueOf(accDoc.getAttribute("AccDocDate"));

		payer = new Client();
		payer.readED((Element) doc.getElementsByTagName("Payer").item(0));
		payee = new Client();
		payee.readED((Element) doc.getElementsByTagName("Payee").item(0));

		purpose = XML.getChildValueString("Purpose", doc);
	}

	public static PaymentDocument createDocFromXML(String src)
	{
		Element root = XML.getXMLRootElement(src);
		PaymentDocument pd = null;
		if(root.getNodeName().equals("ED101"))
		{
			pd = new PaymentOrder();
			pd.readED(root);
		}
		else if(root.getNodeName().equals("ED103"))
		{
			pd = new PaymentRequest();
			pd.readED(root);
		}
		else if(root.getNodeName().equals("ED104"))
		{
			pd = new CollectionOrder();
			pd.readED(root);
		}
		else if(root.getNodeName().equals("ED105"))
		{
			pd = new PaymentWarrant();
			pd.readED(root);
		}
		return pd;
	}

}
