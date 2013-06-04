package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.Element;




/**
 * Абстрактный класс платежный документ
 * @author Admin
 *
 */
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

	
//	public String resultCode;

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
	public String toStr(String razd, boolean addShift){
		String str = "";

		str = Integer.toString(accDocNo) + razd + new SimpleDateFormat("ddMMyyyy").format(accDocDate) + razd +
		transKind + razd + Integer.toString(sum).substring(0, Integer.toString(sum).length() - 2) + "." + 
		Integer.toString(sum).substring(Integer.toString(sum).length() - 2, Integer.toString(sum).length()) + razd +
		paytKind.toString() + razd + payer.bic + razd + payer.correspAcc + razd + payer.personalAcc + razd +
		payer.inn + razd + payer.kpp + razd + (addShift ? "+{ExtEnd}" : "") + payer.name + razd + payee.bic + razd + payee.correspAcc + razd +
		payee.personalAcc + razd + payee.inn + razd + payee.kpp + razd + (addShift ? "+{ExtEnd}" : "") + payee.name + razd +
		priority + razd + tax.drawerStatus;
		if(!tax.drawerStatus.equals("") && tax.drawerStatus != null)
			str = str + razd + tax.cbc + razd + tax.okato + razd + tax.paytReason + razd + tax.taxPeriod + razd + tax.docNo + razd + tax.docDate + razd + tax.taxPaytKind;

		str = str + razd + purpose + razd + new SimpleDateFormat("ddMMyyyy").format(chargeOffDate) + razd + new SimpleDateFormat("ddMMyyyy").format(receiptDate);
		return str;	
	}

	
	/**
	 * @param razd разделитель реквизитов
	 * @return строка для контрольного ввода
	 */
	public String toStrContr(String razd){

		String str = "";

		str = Integer.toString(accDocNo) + razd + new SimpleDateFormat("ddMMyyyy").format(accDocDate) + razd + transKind + razd +
		Integer.toString(sum).substring(0, Integer.toString(sum).length() - 2) + "." + 
		Integer.toString(sum).substring(Integer.toString(sum).length() - 2, Integer.toString(sum).length()) + razd + paytKind.toString() + razd + 
		payer.bic + razd + payer.correspAcc + razd + payer.personalAcc + razd + payee.bic + razd + payee.correspAcc + razd +
		payee.personalAcc + razd + priority;

		return str;	
	}

	/**
	 * создает элемент с реквизитами документа
	 * @param doc документ
	 * @return элемент
	 */
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
	
	public void generateFromXML(Element gendoc, int edNo, String edAuthor)
	{
		this.edNo = edNo;
		edDate = Settings.operDate;
		this.edAuthor = edAuthor;
		paytKind = "1";
		sum = XML.getOptionalIntAttr("Sum", gendoc);
		if(sum == 0)
			sum = (int) (new Random().nextFloat() * 10000);		
		
		priority = "6";
		accDocNo = edNo;
		accDocDate = Settings.operDate;
		
		chargeOffDate = Settings.operDate;
		receiptDate = Settings.operDate;

		Element el = (Element) gendoc.getElementsByTagName("Payer").item(0);		

		payer = Client.createClientFromBICPersonalAcc(el);

		el = (Element) gendoc.getElementsByTagName("Payee").item(0);		

		payee = Client.createClientFromBICPersonalAcc(el);
		
	
		
		generateFromXMLByType(gendoc);
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
	public static PaymentDocument createDocFromXML(String src)
	{
		Element root = XML.getXMLRootElement(src);
		PaymentDocument pd = null;
		if(root.getLocalName().equals("ED101"))
		{
			pd = new PaymentOrder();
			pd.readED(root);
		}
		else if(root.getLocalName().equals("ED103"))
		{
			pd = new PaymentRequest();
			pd.readED(root);
		}
		else if(root.getLocalName().equals("ED104"))
		{
			pd = new CollectionOrder();
			pd.readED(root);
		}
		else if(root.getLocalName().equals("ED105"))
		{
			pd = new PaymentWarrant();
			pd.readED(root);
		}
		else if(root.getLocalName().equals("ED108"))
		{
			pd = new PaymentOrderRegister();
			pd.readED(root);
		}
		return pd;
	}

}
