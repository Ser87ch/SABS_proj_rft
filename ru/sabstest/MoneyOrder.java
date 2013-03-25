
package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class MoneyOrder extends PayDoc {
	public MoneyOrder()
	{
		super();
	}
	public Element createED(Document doc)
	{
		Element rootElement = doc.createElement("ED101");

		rootElement.setAttribute("xmlns", "urn:cbr-ru:ed:v2.0");

		rootElement.setAttribute("EDNo", Integer.toString(edNo));
		rootElement.setAttribute("EDDate", new SimpleDateFormat("yyyy-MM-dd").format(edDate));
		rootElement.setAttribute("EDAuthor", edAuthor);
		rootElement.setAttribute("PaytKind", paytKind);
		rootElement.setAttribute("Sum", Integer.toString(sum));
		rootElement.setAttribute("TransKind", transKind);
		rootElement.setAttribute("Priority", priority);
		if(receiptDate != null)
			rootElement.setAttribute("ReceiptDate", new SimpleDateFormat("yyyy-MM-dd").format(receiptDate));
		if(fileDate != null)
			rootElement.setAttribute("FileDate", new SimpleDateFormat("yyyy-MM-dd").format(fileDate));
		if(chargeOffDate != null)
			rootElement.setAttribute("ChargeOffDate", new SimpleDateFormat("yyyy-MM-dd").format(chargeOffDate));
		if(systemCode != null && !systemCode.equals(""))
			rootElement.setAttribute("SystemCode", systemCode);

		Element accDoc = doc.createElement("AccDoc");
		rootElement.appendChild(accDoc);

		accDoc.setAttribute("AccDocNo", Integer.toString(accDocNo));
		accDoc.setAttribute("AccDocDate", new SimpleDateFormat("yyyy-MM-dd").format(accDocDate));		

		rootElement.appendChild(payer.createXMLElement(doc, "Payer"));
		rootElement.appendChild(payee.createXMLElement(doc, "Payee"));

		XML.createNode(doc, rootElement, "Purpose", purpose);

		if(tax != null && !tax.drawerStatus.equals(""))
			rootElement.appendChild(tax.createXMLElement(doc));
		
		return rootElement;
	}

	public void readED(Element doc)
	{
		if(doc.getTagName() == "ED101")
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
			payer.readED(doc);
			payee = new Client();
			payee.readED(doc);
			
			purpose = XML.getChildValueString("Purpose", doc);
			
			NodeList nl = doc.getElementsByTagName("DepartmentalInfo");
			if(nl.getLength() == 1)
			{
				tax = new DepartmentalInfo();
				tax.readED((Element) nl.item(0));
			}
			
		}
	}	
}
