package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ReturnDocument {
	public PaymentOrder po;
	
	//реквизиты исходного ЭД
	public int iEdNo; //Номер ЭД в течение опердня
	public Date iEdDate; //Дата составления ЭД
	public String iEdAuthor; //Уникальный идентификатор составителя ЭД (УИС)
	
	public Element createED(Document doc)
	{
		Element el = doc.createElement("VERReturnPayt");
		el.appendChild(po.createED(doc));
		
		Element ied = doc.createElement("EDRefID");
		ied.setAttribute("EDNo", Integer.toString(iEdNo));
		ied.setAttribute("EDDate", new SimpleDateFormat("yyyy-MM-dd").format(iEdDate));
		ied.setAttribute("EDAuthor", iEdAuthor);
		
		el.appendChild(ied);
		
		return el;
	}
	
	public void readED(Element doc)
	{
		po = new PaymentOrder();
		po.readED((Element) doc.getElementsByTagNameNS("*","ED101").item(0));
		
		Element el = (Element) doc.getElementsByTagNameNS("*","EDRefID").item(0);
		iEdNo = Integer.parseInt(el.getAttribute("EDNo"));
		iEdDate = Date.valueOf(el.getAttribute("EDDate"));
		iEdAuthor = el.getAttribute("EDAuthor");
	}
	
	public void generateFromPaymentDocument(PaymentDocument pd, String author, String resultCode)
	{
		iEdAuthor = pd.edAuthor;
		iEdDate = pd.edDate;
		iEdNo = pd.edNo;
		
		po = new PaymentOrder();
		
		po.generateReturnDocument(pd, author, resultCode);
	}
	
	public void insertIntoDbVer(int idPacet, String filename) 
	{
		po.insertIntoDbVer(idPacet, filename, iEdNo, iEdDate, iEdAuthor);	
	}
}
