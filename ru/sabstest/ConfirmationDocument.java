package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Подтверждение для ВЭР
 * @author Admin
 *
 */
public class ConfirmationDocument {
	//реквизиты ЭД
	public int edNo; //Номер ЭД в течение опердня
	public Date edDate; //Дата составления ЭД
	public String edAuthor; //Уникальный идентификатор составителя ЭД (УИС)
	
	public int sum; //сумма
	public String transKind; //вид операции
	public String resultCode; //код результата обработки ЭД
	
	//реквизиты исходного расчетного документа
	public int accDocNo; //номер расчетного документа
	public Date accDocDate; //Дата выписки расчетного документа
	
	public Client payer; //плательщика
	public Client payee; //получатель
	
	//реквизиты исходного ЭД
	public int iEdNo; //Номер ЭД в течение опердня
	public Date iEdDate; //Дата составления ЭД
	public String iEdAuthor; //Уникальный идентификатор составителя ЭД (УИС)
	
	/**
	 * создает элемент ED216
	 * @param doc документ
	 * @return элемент
	 */
	public Element createED(Document doc)
	{
		Element rootElement = doc.createElement("ED216");		

		rootElement.setAttribute("xmlns", "urn:cbr-ru:ed:v2.0");
		rootElement.setAttribute("EDNo", Integer.toString(edNo));
		rootElement.setAttribute("EDDate", new SimpleDateFormat("yyyy-MM-dd").format(edDate));
		rootElement.setAttribute("EDAuthor", edAuthor);

		rootElement.setAttribute("Sum", Integer.toString(sum));
		rootElement.setAttribute("TransKind", transKind);
		
		XML.setOptinalAttr(rootElement, "ResultCode", resultCode);		
		
		Element accDoc = doc.createElement("AccDoc");
		rootElement.appendChild(accDoc);

		accDoc.setAttribute("AccDocNo", Integer.toString(accDocNo));
		accDoc.setAttribute("AccDocDate", new SimpleDateFormat("yyyy-MM-dd").format(accDocDate));	
		
		rootElement.appendChild(payer.createXMLShortElement(doc, "ShortPayer"));
		rootElement.appendChild(payee.createXMLShortElement(doc, "ShortPayee"));
		return rootElement;
	}
	
	/**
	 * считываются основные реквизиты платежного документа
	 * @param doc элемент
	 */
	public void readED(Element doc)
	{
		edNo = Integer.parseInt(doc.getAttribute("EDNo"));
		edDate = Date.valueOf(doc.getAttribute("EDDate"));
		edAuthor = doc.getAttribute("EDAuthor");

		
		sum = Integer.parseInt(doc.getAttribute("Sum"));
		transKind = doc.getAttribute("TransKind");		
		resultCode = doc.getAttribute("ResultCode");

		Element accDoc = (Element) doc.getElementsByTagName("AccDoc").item(0);			
		accDocNo = Integer.parseInt(accDoc.getAttribute("AccDocNo"));
		accDocDate = Date.valueOf(accDoc.getAttribute("AccDocDate"));

		payer = new Client();
		payer.readED((Element) doc.getElementsByTagName("ShortPayer").item(0));
		payee = new Client();
		payee.readED((Element) doc.getElementsByTagName("ShortPayee").item(0));
		
	}
	
	public void generateFromPaymentDocument(PaymentDocument pd, String author)
	{
		resultCode = pd.resultCode;
		
		iEdAuthor = pd.edAuthor;
		iEdDate = pd.edDate;
		iEdNo = pd.edNo;
		
		edNo = pd.edNo + 500;
		edDate = pd.edDate;
		edAuthor = author;
		sum = pd.sum;
		transKind = pd.transKind;
		
		accDocNo = pd.accDocNo;
		accDocDate = pd.accDocDate;
		
		payer = pd.payer;
		payee = pd.payee;
	}
	
}
