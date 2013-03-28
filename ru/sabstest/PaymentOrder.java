
package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class PaymentOrder extends PaymentDocument {
	public PaymentOrder()
	{
		super();
	}
	
	@Override
	public Element createED(Document doc)
	{
		Element rootElement = doc.createElement("ED101");		

		addCommonEDElements(doc, rootElement);		

		if(tax != null && !tax.drawerStatus.equals(""))
			rootElement.appendChild(tax.createXMLElement(doc));
		
		return rootElement;
	}

	
	@Override
	public void readED(Element doc)
	{
		if(doc.getTagName() == "ED101")
		{
			readCommonEDElements(doc);
			
			NodeList nl = doc.getElementsByTagName("DepartmentalInfo");
			if(nl.getLength() == 1)
			{
				tax = new DepartmentalInfo();
				tax.readED((Element) nl.item(0));
			}
			
		}
	}
	
	@Override
	public void generateFromXML(Element gendoc, int edNo, String edAuthor)
	{
		this.edNo = edNo;
		edDate = Settings.operDate;
		this.edAuthor = edAuthor;
		paytKind = "1";
		sum = (int) (new Random().nextFloat() * 10000);
		transKind = "01";
		priority = "6";
		accDocNo = edNo;
		accDocDate = Settings.operDate;
		purpose = "Тестовое платежное поручение";
		
		Element el = (Element) gendoc.getElementsByTagName("Payer").item(0);		
		
		payer = Client.createClientFromBICPersonalAcc(XML.getChildValueString("BIC", el), 
				XML.getChildValueString("PersonalAcc", el));
		
		el = (Element) gendoc.getElementsByTagName("Payee").item(0);		
		
		payee = Client.createClientFromBICPersonalAcc(XML.getChildValueString("BIC", el), 
				XML.getChildValueString("PersonalAcc", el));
	}
}
